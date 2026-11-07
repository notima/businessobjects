package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;
import java.util.function.Function;

import org.notima.generic.businessobjects.AccountingType;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.Payment;

/**
 * Builds the voucher of a {@link PaymentAllocation} made after its payments were posted.
 *
 * <p>A payment that isn't allocated when it's posted is posted to the unallocated account of
 * its bank account ({@link BankAccounts#unallocatedAccount(Payment)}: the bank account's own,
 * else prepayments from customers or to vendors; see {@link PaymentVoucherBuilder}).
 * Allocating it moves the amount from that same account to the receivable or payable of the
 * invoice, or to the line's charge account. Discounts and
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
	private final BankAccounts bankAccounts;

	/** A builder for payments without known bank accounts: unallocated amounts go to prepayments. */
	public AllocationVoucherBuilder(PostingAccounts accounts) {
		this(accounts, null);
	}

	/**
	 * @param accounts		Accounts per role.
	 * @param bankAccounts	The company's bank accounts, whose unallocated accounts are used; may be null.
	 */
	public AllocationVoucherBuilder(PostingAccounts accounts, BankAccounts bankAccounts) {
		this.accounts = accounts != null ? accounts : new PostingAccounts();
		this.bankAccounts = bankAccounts != null ? bankAccounts : new BankAccounts(null, this.accounts);
	}

	/**
	 * @param ledger	Where the allocation's payments are looked up, for their bank account
	 * 					and to tell a customer's from a vendor's; may be null, the
	 * 					prepayment accounts are then used.
	 */
	public AccountingVoucher build(PaymentAllocation a, AllocationLedger ledger) {
		AccountingVoucher v = new AccountingVoucher();
		v.setAcctDate(a.getAllocationDate());
		v.setSourceCurrency(a.getCurrency());
		v.setDescription(a.getDescription() != null ? a.getDescription()
				: (a.isReversal() ? "Reversed allocation" : "Allocation"));
		addLines(v, a, id -> ledger != null ? ledger.getPayment(id) : null);
		VoucherLines.merge(v);
		return v;
	}

	/** Adds the lines of {@code a} to {@code v}, without merging; payments are looked up by id. */
	void addLines(AccountingVoucher v, PaymentAllocation a, Function<String, Payment<?>> payments) {
		if (a.getLines() == null) return;
		for (PaymentAllocationLine l : a.getLines()) {
			Payment<?> p = l.hasPayment() ? payments.apply(l.getPaymentId()) : null;
			boolean sales = l.hasInvoice() ? l.isSalesTransaction() : (p != null ? p.isCustomerPayment() : l.isSalesTransaction());
			// Customer side: unallocated (liability) debited, receivable credited. Vendor side mirrored.
			BigDecimal s = sales ? BigDecimal.ONE : BigDecimal.ONE.negate();
			String prepaymentRole = sales ? AccountingType.LIABILITY_CUSTOMER_PREPAYMENT : AccountingType.ASSET_VENDOR_PREPAYMENT;
			String openRole = sales ? AccountingType.LIQUID_ASSET_AR : AccountingType.LIABILITY_VENDOR;
			BigDecimal amount = VoucherLines.nvl(l.getAmount());

			if (l.hasPayment()) {
				String unallocated = bankAccounts.unallocatedAccount(bankAccounts.find(p), sales);
				VoucherLines.add(v, amount.multiply(s), prepaymentRole, unallocated);
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

}
