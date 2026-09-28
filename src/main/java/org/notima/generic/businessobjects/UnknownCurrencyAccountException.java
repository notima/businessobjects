package org.notima.generic.businessobjects;

/**
 * Thrown when a general ledger account is requested for a currency that has no
 * account defined.
 */
public class UnknownCurrencyAccountException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String	optionName;
	private final String	currency;

	public UnknownCurrencyAccountException(String optionName, String currency) {
		super((optionName!=null ? optionName : "Account definition") + " has no account for currency " + currency);
		this.optionName = optionName;
		this.currency = currency;
	}

	/**
	 * @return	The name of the account option (e.g. generalLedgerInTransitAccount). Can be null.
	 */
	public String getOptionName() {
		return optionName;
	}

	public String getCurrency() {
		return currency;
	}

}
