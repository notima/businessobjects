package org.notima.generic.businessobjects.accounting.statement;

/**
 * An account's part of a statement line, shown with the line's sign.
 */
public final class AccountAmount {

	private final String accountNo;
	private final String name;
	private final double amount;

	/**
	 * @param accountNo	The account, or null for the result of the year that hasn't been booked.
	 * @param name		The account's name.
	 * @param amount	The amount, with the sign of the line.
	 */
	public AccountAmount(String accountNo, String name, double amount) {
		this.accountNo = accountNo;
		this.name = name;
		this.amount = amount;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public String getName() {
		return name;
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return accountNo + " " + name + " " + amount;
	}

}
