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
 * <p>The accounts come from the payment's bank account ({@link BankAccounts}): the ledger
 * account of the bank account gets what actually reached it ({@link Payment#getAmount()});
 * what the payer paid ({@link Payment#getOriginalAmount()}) is posted to the bank account's
 * account for unallocated payments; the difference is fees. The payment's
 * {@link Payment#getPaymentWriteOffs() write-offs} are posted to their accounts, and
 * whatever of the difference they don't cover to the bank account's fee account, so the
 * voucher always balances.</p>
 *
 * <p>{@link #build(Payment, PaymentAllocation, AllocationLedger) Built together} with an
 * allocation made at the same time, the allocation's lines are added
 * ({@link AllocationVoucherBuilder}) and the unallocated amounts net out, so the payment goes
 * straight to the receivable or payable: one voucher, Bank / Accounts receivable.</p>
 *
 * <p>A payment to a vendor is the mirror image: the bank is credited and the unallocated
 * account (an asset) debited. A refund, with a negative amount, gives the opposite.</p>
 */
public class PaymentVoucherBuilder {

	private final PostingAccounts accounts;
	private final BankAccounts bankAccounts;

	/** A builder for payments without known bank accounts: the accounts of the roles are used. */
	public PaymentVoucherBuilder(PostingAccounts accounts) {
		this(accounts, null);
	}

	/**
	 * @param accounts		Accounts per role, for what the bank accounts don't say.
	 * @param bankAccounts	The company's bank accounts; may be null.
	 */
	public PaymentVoucherBuilder(PostingAccounts accounts, BankAccounts bankAccounts) {
		this.accounts = accounts != null ? accounts : new PostingAccounts();
		this.bankAccounts = bankAccounts != null ? bankAccounts : new BankAccounts(null, this.accounts);
	}

	/** The voucher of a payment that isn't allocated (yet): bank against prepayments. */
	public AccountingVoucher build(Payment<?> p) {
		return build(p, null, null);
	}

	/**
	 * The voucher of a payment and the allocation made together with it.
	 *
	 * @param allocation	The allocation, or null for none.
	 * @param ledger		For other payments of the allocation, see
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

		VoucherLines.add(v, received.multiply(s), AccountingType.LIQUID_ASSET_CASH, bankAccounts.cashAccount(p));
		String feeAcct = bankAccounts.feeAccount(p);

		BigDecimal fees = paid.subtract(received).multiply(s);
		if (p.hasPaymentWriteOffs()) {
			for (PaymentWriteOff pwo : p.getPaymentWriteOffs().getPaymentWriteOff()) {
				if (pwo.getAmount() == null) continue;
				String acct = VoucherLines.blankToNull(pwo.getAccountNo());
				BigDecimal amt = BigDecimal.valueOf(pwo.getAmount()).multiply(s);
				VoucherLines.add(v, amt, AccountingType.OTHER_EXPENSES_SALES, acct != null ? acct : feeAcct);
				fees = fees.subtract(amt);
			}
		}
		// Fees not covered by write-offs
		VoucherLines.add(v, fees, AccountingType.OTHER_EXPENSES_SALES, feeAcct);

		String prepaymentRole = customer ? AccountingType.LIABILITY_CUSTOMER_PREPAYMENT : AccountingType.ASSET_VENDOR_PREPAYMENT;
		VoucherLines.add(v, paid.multiply(s).negate(), prepaymentRole, bankAccounts.unallocatedAccount(p));

		if (allocation != null) {
			// This payment is known even if the ledger doesn't have it yet
			new AllocationVoucherBuilder(accounts, bankAccounts).addLines(v, allocation, id ->
					id.equals(p.getPaymentId()) ? p : (ledger != null ? ledger.getPayment(id) : null));
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
