package org.notima.generic.businessobjects.accounting.statement;

/**
 * The unit amounts of an annual report are shown in.
 */
public enum AmountUnit {

	KR(1, "kr"),
	TKR(1000, "tkr"),
	MKR(1000000, "Mkr");

	/** Below this balance sheet total (kr), {@link #suggest(double)} proposes kr. */
	public static final double KR_LIMIT  = 50000000.0;
	/** Below this balance sheet total (kr), {@link #suggest(double)} proposes tkr. */
	public static final double TKR_LIMIT = 5000000000.0;

	private final double factor;
	private final String symbol;

	AmountUnit(double factor, String symbol) {
		this.factor = factor;
		this.symbol = symbol;
	}

	public double getFactor() {
		return factor;
	}

	public String getSymbol() {
		return symbol;
	}

	/**
	 * Proposes a unit from the size of the company.
	 *
	 * @param balanceSheetTotal	The total assets in kr.
	 */
	public static AmountUnit suggest(double balanceSheetTotal) {
		double total = Math.abs(balanceSheetTotal);
		if (total < KR_LIMIT) return KR;
		if (total < TKR_LIMIT) return TKR;
		return MKR;
	}

}
