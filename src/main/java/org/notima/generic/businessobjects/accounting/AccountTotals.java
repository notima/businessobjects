package org.notima.generic.businessobjects.accounting;

import org.notima.generic.businessobjects.AccountCategory;

/**
 * The balances and movements of one account, or the sum for an {@link AccountCategory}, over
 * a date range of an accounting period. Calculated by {@link LedgerCalculator#summarize}.
 *
 * <p>Amounts are signed debit positive, as in the vouchers: assets and costs are positive,
 * liabilities, equity and revenue negative.</p>
 */
public class AccountTotals {

	private final String			accountNo;
	private final String			name;
	private final AccountCategory	category;

	private double	openingBalance;
	private double	beforeFrom;
	private double	debit;
	private double	credit;

	/**
	 * @param accountNo	The account, or null for the sum of a category.
	 * @param name		The account name; null for a category.
	 * @param category	The category of the account.
	 */
	public AccountTotals(String accountNo, String name, AccountCategory category) {
		this.accountNo = accountNo;
		this.name = name;
		this.category = category;
	}

	/** @return	The account number; null for the sum of a category. */
	public String getAccountNo() {
		return accountNo;
	}

	public String getName() {
		return name;
	}

	public AccountCategory getCategory() {
		return category;
	}

	/** @return	The opening balance of the period (balance sheet accounts). */
	public double getOpeningBalance() {
		return openingBalance;
	}

	/** @return	The net movement (debit - credit) from the start of the period until the start of the range. */
	public double getMovementBeforeFrom() {
		return beforeFrom;
	}

	/** @return	The balance at the start of the range: the opening balance plus the movements before it. */
	public double getBalanceAtFrom() {
		return openingBalance + beforeFrom;
	}

	/** @return	The debit amounts within the range. */
	public double getDebit() {
		return debit;
	}

	/** @return	The credit amounts within the range. */
	public double getCredit() {
		return credit;
	}

	/** @return	The net movement within the range, debit - credit. */
	public double getNet() {
		return debit - credit;
	}

	/** @return	The result within the range, credit - debit; ie positive for revenue. */
	public double getResult() {
		return credit - debit;
	}

	/** @return	The balance at the end of the range. */
	public double getBalanceAtTo() {
		return getBalanceAtFrom() + getNet();
	}

	/** @return	True if the account has no balance and no movements. */
	public boolean isZero() {
		return openingBalance == 0.0 && getBalanceAtFrom() == 0.0 && getBalanceAtTo() == 0.0
				&& debit == 0.0 && credit == 0.0;
	}

	/** @return	True if there are no debit or credit amounts within the range. */
	public boolean hasNoMovements() {
		return debit == 0.0 && credit == 0.0;
	}

	void addOpeningBalance(double amount) {
		openingBalance += amount;
	}

	void addBeforeFrom(double net) {
		beforeFrom += net;
	}

	void addDebit(double amount) {
		debit += amount;
	}

	void addCredit(double amount) {
		credit += amount;
	}

	void add(AccountTotals other) {
		openingBalance += other.openingBalance;
		beforeFrom += other.beforeFrom;
		debit += other.debit;
		credit += other.credit;
	}

}
