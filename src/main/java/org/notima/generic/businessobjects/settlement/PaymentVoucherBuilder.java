package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;

import org.notima.generic.businessobjects.AccountingType;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Payment;
import org.notima.generic.businessobjects.PaymentWriteOff;
import org.notima.util.LocalDateUtils;

/**
 * Builds the voucher of a payment, on its own or together with its allocation.
 *
 * <p>The bank account ({@link Payment#getGlAccountNo()}, else
 * {@link AccountingType#LIQUID_ASSET_CASH}) gets what actually reached it
 * ({@link Payment#getAmount()}). What the payer paid ({@link Payment#getOriginalAmount()}) is
 * posted to prepayments; the difference is fees. The payment's
 * {@link Payment#getPaymentWriteOffs() write-offs} are posted to their accounts, and
 * whatever of the difference they don't cover to {@link AccountingType#OTHER_EXPENSES_SALES},
 * so the voucher always balances.</p>
 *
 * <p>{@link #build(Payment, PaymentAllocation, AllocationLedger) Built together} with an
 * allocation made at the same time, the allocation's lines are added
 * ({@link AllocationVoucherBuilder}) and the prepayments net out, so the payment goes
 * straight to the receivable or payable: one voucher, Bank / Accounts receivable.</p>
 *
 * <p>A payment to a vendor is the mirror image: the bank is credited and prepayments (an
 * asset) debited. A refund, with a negative amount, gives the opposite.</p>
 */
public class PaymentVoucherBuilder {

	private final PostingAccounts accounts;

	public PaymentVoucherBuilder(PostingAccounts accounts) {
		this.accounts = accounts != null ? accounts : new PostingAccounts();
	}

	/** The voucher of a payment that isn't allocated (yet): bank against prepayments. */
	public AccountingVoucher build(Payment<?> p) {
		return build(p, null, null);
	}

	/**
	 * The voucher of a payment and the allocation made together with it.
	 *
	 * @param allocation	The allocation, or null for none.
	 * @param ledger		For the allocation's payment-only lines, see
	 * 						{@link AllocationVoucherBuilder#build(PaymentAllocation, AllocationLedger)}.
	 */
	public AccountingVoucher build(Payment<?> p, PaymentAllocation allocation, AllocationLedger ledger) {
		boolean customer = p.isCustomerPayment();
		BigDecimal s = customer ? BigDecimal.ONE : BigDecimal.ONE.negate();
		BigDecimal paid = AllocationLedger.paymentAmount(p);
		BigDecimal received = p.getAmount() != null ? BigDecimal.valueOf(p.getAmount()) : paid;

		AccountingVoucher v = new AccountingVoucher();
		v.setAcctDate(LocalDateUtils.asLocalDate(p.getPaymentDate()));
		v.setSourceCurrency(p.getCurrency());
		v.setDescription(description(p));

		String bankAcct = VoucherLines.blankToNull(p.getGlAccountNo());
		VoucherLines.add(v, received.multiply(s), AccountingType.LIQUID_ASSET_CASH,
				bankAcct != null ? bankAcct : accounts.get(AccountingType.LIQUID_ASSET_CASH));

		BigDecimal fees = paid.subtract(received).multiply(s);
		if (p.hasPaymentWriteOffs()) {
			for (PaymentWriteOff pwo : p.getPaymentWriteOffs().getPaymentWriteOff()) {
				if (pwo.getAmount() == null) continue;
				String acct = VoucherLines.blankToNull(pwo.getAccountNo());
				BigDecimal amt = BigDecimal.valueOf(pwo.getAmount()).multiply(s);
				VoucherLines.add(v, amt, AccountingType.OTHER_EXPENSES_SALES,
						acct != null ? acct : accounts.get(AccountingType.OTHER_EXPENSES_SALES));
				fees = fees.subtract(amt);
			}
		}
		// Fees not covered by write-offs
		VoucherLines.add(v, fees, AccountingType.OTHER_EXPENSES_SALES, accounts.get(AccountingType.OTHER_EXPENSES_SALES));

		String prepaymentRole = customer ? AccountingType.LIABILITY_CUSTOMER_PREPAYMENT : AccountingType.ASSET_VENDOR_PREPAYMENT;
		VoucherLines.add(v, paid.multiply(s).negate(), prepaymentRole, accounts.get(prepaymentRole));

		if (allocation != null) {
			new AllocationVoucherBuilder(accounts).addLines(v, allocation, ledger);
			VoucherLines.merge(v);
		}
		return v;
	}

	/** "Payment Acme AB OCR 1234", "Payment to Supplier AB". */
	static String description(Payment<?> p) {
		BusinessPartner<?> bp = p.getBusinessPartner();
		String name = bp != null ? bp.getName() : null;
		String ref = p.getDestinationSystemReference() != null ? p.getDestinationSystemReference() : p.getInvoiceNo();
		return VoucherLines.join(p.isCustomerPayment() ? "Payment" : "Payment to", name, ref);
	}

}
