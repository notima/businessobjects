package org.notima.generic.businessobjects;

public class Currency {

	public static final int		DEFAULT_PRECISION = 2;
	public static final int		DEFAULT_EXCHANGE_PRECISION = 4;
	
	private String currencyISO;
	private String currencyName;

	private int    precision = DEFAULT_PRECISION;
	private int	   exchangePrecision = DEFAULT_EXCHANGE_PRECISION;	
	
	public Currency() {};
	
	public Currency(String ISOcode) {
		currencyISO = ISOcode;
	}
	
	public String getCurrencyISO() {
		return currencyISO;
	}
	public void setCurrencyISO(String currencyISO) {
		this.currencyISO = currencyISO;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	/**
	 * Precision is to how many decimals the currency is normally rounded.
	 * 
	 * @return		The precision for this currency
	 */
	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * The exchange rate precision is to how many decimals the exchange rate for this currency is normally displayed in.
	 * 
	 * @return		The exchange rate precision for this currency
	 */
	public int getExchangePrecision() {
		return exchangePrecision;
	}

	public void setExchangePrecision(int exchangePrecision) {
		this.exchangePrecision = exchangePrecision;
	}
	
	
}
