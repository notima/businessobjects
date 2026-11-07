package org.notima.generic.businessobjects.settlement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.notima.generic.businessobjects.AccountingType;
import org.notima.generic.businessobjects.BankAccountDetail;
import org.notima.generic.businessobjects.Payment;

/**
 * The company's own bank accounts, and the ledger accounts payments on them are posted to.
 *
 * <p>A payment is received on, or paid from, a bank account ({@link Payment#getBankAccountNo()},
 * matched against a {@link BankAccountDetail}'s account number or IBAN, ignoring spaces,
 * dashes and case). The bank account is mapped to ledger accounts: the account the money is
 * on ({@link BankAccountDetail#getGeneralLedgerAccountToUseForPayments()}), the fee account,
 * and the accounts for payments that aren't allocated to an invoice yet. The ledger account
 * isn't the bank account: several bank accounts may share one, and a bank account may post
 * through a reconciliation account.</p>
 *
 * <p>Where a bank account has no ledger account of a kind, or a payment has no bank account,
 * the account of the corresponding {@link AccountingType role} in {@link PostingAccounts} is
 * used.</p>
 */
public class BankAccounts {

	private final List<BankAccountDetail> accounts;
	private final PostingAccounts fallback;

	/**
	 * @param accounts	The company's bank accounts; may be null.
	 * @param fallback	Accounts per role, for what the bank accounts don't say; may be null.
	 */
	public BankAccounts(Collection<BankAccountDetail> accounts, PostingAccounts fallback) {
		this.accounts = accounts != null ? new ArrayList<BankAccountDetail>(accounts) : new ArrayList<BankAccountDetail>();
		this.fallback = fallback != null ? fallback : new PostingAccounts();
	}

	public List<BankAccountDetail> getAccounts() {
		return Collections.unmodifiableList(accounts);
	}

	/** @return	The bank account the payment was received on or paid from, or null if unknown. */
	public BankAccountDetail find(Payment<?> p) {
		return p != null ? find(p.getBankAccountNo()) : null;
	}

	/** @return	The bank account with this account number or IBAN, or null. */
	public BankAccountDetail find(String accountNoOrIban) {
		String key = normalize(accountNoOrIban);
		if (key == null) return null;
		for (BankAccountDetail ba : accounts) {
			if (key.equals(normalize(ba.getAccountNo())) || key.equals(normalize(ba.getIBAN()))) return ba;
		}
		return null;
	}

	/** The ledger account the money of the payment is on (ie 1930). */
	public String cashAccount(Payment<?> p) {
		BankAccountDetail ba = find(p);
		String acct = ba != null ? blankToNull(ba.getGeneralLedgerAccountToUseForPayments()) : null;
		return acct != null ? acct : fallback.get(AccountingType.LIQUID_ASSET_CASH);
	}

	/** The ledger account for fees of the payment (ie 6570). */
	public String feeAccount(Payment<?> p) {
		BankAccountDetail ba = find(p);
		String acct = ba != null ? blankToNull(ba.getGeneralLedgerFeeAccount()) : null;
		return acct != null ? acct : fallback.get(AccountingType.OTHER_EXPENSES_SALES);
	}

	/**
	 * The ledger account for the payment while it isn't allocated to an invoice: the bank
	 * account's for receipts or payments, else prepayments from customers or to vendors.
	 */
	public String unallocatedAccount(Payment<?> p) {
		return unallocatedAccount(find(p), p == null || p.isCustomerPayment());
	}

	/** The ledger account for unallocated payments on {@code ba} (may be null) of the given kind. */
	public String unallocatedAccount(BankAccountDetail ba, boolean customer) {
		String acct = ba != null ? ba.getGeneralLedgerUnallocatedAccount(customer) : null;
		if (acct != null) return acct;
		return fallback.get(customer ? AccountingType.LIABILITY_CUSTOMER_PREPAYMENT : AccountingType.ASSET_VENDOR_PREPAYMENT);
	}

	static String normalize(String s) {
		if (s == null) return null;
		String n = s.replaceAll("[\\s\\-.]", "").toUpperCase();
		return n.isEmpty() ? null : n;
	}

	private static String blankToNull(String s) {
		return s == null || s.trim().isEmpty() ? null : s.trim();
	}

}
