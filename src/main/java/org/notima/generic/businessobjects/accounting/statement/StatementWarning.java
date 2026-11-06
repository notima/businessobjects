package org.notima.generic.businessobjects.accounting.statement;

/**
 * Something the user should check before the statements are used: an unmapped account,
 * statements that don't agree, or a rounding adjustment.
 */
public final class StatementWarning {

	public enum Severity { INFO, WARNING, ERROR }

	public enum Code {
		/** An account with an amount isn't placed on any line. */
		UNMAPPED_ACCOUNT(Severity.ERROR),
		/** The mapping places an account on a line that no template has. */
		UNKNOWN_LINE(Severity.ERROR),
		/** Total assets differ from total equity and liabilities. */
		BALANCE_MISMATCH(Severity.ERROR),
		/** The result of the income statement differs from the result of the year in the balance sheet. */
		RESULT_MISMATCH(Severity.ERROR),
		/**
		 * An account's balance from the opening balance and the vouchers differs from the
		 * ending balance (#UB) of the file. The statements use the vouchers.
		 */
		ENDING_MISMATCH(Severity.WARNING),
		/** An account's opening balance differs from its ending balance of the previous year. */
		OPENING_MISMATCH(Severity.WARNING),
		/** A line was adjusted so that the rounded statements add up. */
		ROUNDING_ADJUSTED(Severity.INFO);

		private final Severity severity;

		Code(Severity severity) {
			this.severity = severity;
		}

		public Severity getSeverity() {
			return severity;
		}
	}

	private final Code code;
	private final int column;
	private final String accountNo;
	private final String lineId;
	private final double amount;

	/**
	 * @param code		What is wrong.
	 * @param column	0 for the current year, 1 for the previous year.
	 * @param accountNo	The account concerned, or null.
	 * @param lineId	The line concerned, or null.
	 * @param amount	The difference or amount concerned.
	 */
	public StatementWarning(Code code, int column, String accountNo, String lineId, double amount) {
		this.code = code;
		this.column = column;
		this.accountNo = accountNo;
		this.lineId = lineId;
		this.amount = amount;
	}

	public Code getCode() {
		return code;
	}

	public Severity getSeverity() {
		return code.getSeverity();
	}

	public int getColumn() {
		return column;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public String getLineId() {
		return lineId;
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(code).append(column == 0 ? " (current year)" : " (previous year)");
		if (accountNo != null) s.append(" account ").append(accountNo);
		if (lineId != null) s.append(" line ").append(lineId);
		s.append(": ").append(String.format(java.util.Locale.ROOT, "%.2f", amount));
		return s.toString();
	}

}
