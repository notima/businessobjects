package org.notima.generic.businessobjects.accounting.statement;

/**
 * Where an account goes in the statements.
 *
 * <p>Accounts with the same {@code group} are summed before they are placed: a debit net goes
 * on {@code debitLineId}, a credit net on {@code creditLineId}. That way a tax receivable can
 * be netted against the tax liability and shown on whichever side the net belongs.
 * Most accounts have the same line for both and their own line as group.
 */
public final class AccountPlacement {

	/** The account closes the result of the year (8999 in Sweden) and isn't shown. */
	public static final AccountPlacement CLOSING = new AccountPlacement(null, null, null);

	private final String debitLineId;
	private final String creditLineId;
	private final String group;

	private AccountPlacement(String debitLineId, String creditLineId, String group) {
		this.debitLineId = debitLineId;
		this.creditLineId = creditLineId;
		this.group = group;
	}

	/** The account goes on {@code lineId} whatever its sign. */
	public static AccountPlacement on(String lineId) {
		return new AccountPlacement(lineId, lineId, lineId);
	}

	/** The net of the accounts in {@code group} goes on the debit or the credit line. */
	public static AccountPlacement bySign(String group, String debitLineId, String creditLineId) {
		return new AccountPlacement(debitLineId, creditLineId, group);
	}

	public boolean isClosing() {
		return this == CLOSING;
	}

	public String getDebitLineId() {
		return debitLineId;
	}

	public String getCreditLineId() {
		return creditLineId;
	}

	public String getGroup() {
		return group;
	}

	/** @return	The line for a net amount (debit positive). */
	public String lineFor(double net) {
		return net >= 0 ? debitLineId : creditLineId;
	}

	@Override
	public String toString() {
		if (isClosing()) return "CLOSING";
		return debitLineId.equals(creditLineId) ? debitLineId : group + "(" + debitLineId + "/" + creditLineId + ")";
	}

}
