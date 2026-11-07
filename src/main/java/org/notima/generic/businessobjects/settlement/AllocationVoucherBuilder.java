package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;

import org.notima.generic.businessobjects.AccountingType;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.Payment;

/**
 * Builds the voucher of a {@link PaymentAllocation} made after its payments were posted.
 *
 * <p>A payment that isn't allocated when it's posted is posted to prepayments
 * ({@link AccountingType#LIABILITY_CUSTOMER_PREPAYMENT} for a customer's,
 * {@link AccountingType#ASSET_VENDOR_PREPAYMENT} for a vendor's; see
 * {@link PaymentVoucherBuilder}). Allocating it moves the amount from prepayments to the
 * receivable or payable of the invoice, or to the line's charge account. Discounts and
 * write-offs move the amount from the receivable or payable to their accounts. Lines that
 * only offset each other (a credit note against an invoice) net to nothing on the same
 * account and leave no line.</p>
 *
 * <p>A {@link PaymentAllocation#createReversal(java.time.LocalDate) reversal}, having negated
 * amounts, gives the reversed voucher. Discounts and write-offs are posted without any VAT
 * correction.</p>
 */
public class AllocationVoucherBuilder {

	private final PostingAccounts accounts;

	public AllocationVoucherBuilder(PostingAccounts accounts) {
		this.accounts = accounts != null ? accounts : new PostingAccounts();
	}

	/**
	 * @param ledger	Where the payments of payment-only lines are looked up, to tell a
	 * 					customer's from a vendor's; may be null if there are no such lines.
	 */
	public AccountingVoucher build(PaymentAllocation a, AllocationLedger ledger) {
		AccountingVoucher v = new AccountingVoucher();
		v.setAcctDate(a.getAllocationDate());
		v.setSourceCurrency(a.getCurrency());
		v.setDescription(a.getDescription() != null ? a.getDescription()
				: (a.isReversal() ? "Reversed allocation" : "Allocation"));
		addLines(v, a, ledger);
		VoucherLines.merge(v);
		return v;
	}

	/** Adds the lines of {@code a} to {@code v}, without merging. */
	void addLines(AccountingVoucher v, PaymentAllocation a, AllocationLedger ledger) {
		if (a.getLines() == null) return;
		for (PaymentAllocationLine l : a.getLines()) {
			boolean sales = isSales(l, ledger);
			// Customer side: prepayment (liability) debited, receivable credited. Vendor side mirrored.
			BigDecimal s = sales ? BigDecimal.ONE : BigDecimal.ONE.negate();
			String prepaymentRole = sales ? AccountingType.LIABILITY_CUSTOMER_PREPAYMENT : AccountingType.ASSET_VENDOR_PREPAYMENT;
			String openRole = sales ? AccountingType.LIQUID_ASSET_AR : AccountingType.LIABILITY_VENDOR;
			BigDecimal amount = VoucherLines.nvl(l.getAmount());

			if (l.hasPayment()) {
				VoucherLines.add(v, amount.multiply(s), prepaymentRole, accounts.get(prepaymentRole));
			}
			if (l.hasInvoice()) {
				VoucherLines.add(v, amount.multiply(s).negate(), openRole, accounts.get(openRole));
			} else if (l.hasChargeAccount()) {
				VoucherLines.add(v, amount.multiply(s).negate(), null, l.getChargeAccountNo());
			}

			if (l.hasInvoice()) {
				String discountRole = sales ? AccountingType.DISCOUNT_GRANTED : AccountingType.DISCOUNT_RECEIVED;
				BigDecimal discount = VoucherLines.nvl(l.getDiscountAmount());
				VoucherLines.add(v, discount.multiply(s), discountRole, accounts.get(discountRole));
				VoucherLines.add(v, discount.multiply(s).negate(), openRole, accounts.get(openRole));

				String writeOffRole = sales ? AccountingType.WRITE_OFF_CUSTOMER : AccountingType.WRITE_OFF_VENDOR;
				String writeOffAcct = VoucherLines.blankToNull(l.getWriteOffAccountNo());
				BigDecimal writeOff = VoucherLines.nvl(l.getWriteOffAmount());
				VoucherLines.add(v, writeOff.multiply(s), writeOffRole, writeOffAcct != null ? writeOffAcct : accounts.get(writeOffRole));
				VoucherLines.add(v, writeOff.multiply(s).negate(), openRole, accounts.get(openRole));
			}
		}
	}

	/** A line is on the customer side if its invoice is a customer invoice, or else its payment a customer's. */
	private static boolean isSales(PaymentAllocationLine l, AllocationLedger ledger) {
		if (l.hasInvoice()) return l.isSalesTransaction();
		Payment<?> p = ledger != null && l.hasPayment() ? ledger.getPayment(l.getPaymentId()) : null;
		return p != null ? p.isCustomerPayment() : l.isSalesTransaction();
	}

}
