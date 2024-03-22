package org.notima.generic.businessobjects;

import java.time.LocalDate;

public class Tax {

	public static final String TAX_KEY_UNKNOWN = "?";
	
	private String key;
	private double rate;
	
	private AccountElement	defaultRevenueAccount;
	private AccountElement	taxDebtAccount;
	private AccountElement	taxReceivableAccount;
	
	private LocalDate	validFrom;
	private LocalDate	vaildUntil;
	
	private String		countryCode;
	private String		otherCountry;
	private String		otherCountryGroup;

	private boolean		reverseCharge;
	
	public Tax() {}
	
	public Tax(String key, String countryCode, double rate, String revenueVatCode, String debtVatCode, String receivableVatCode) {

		this.key = key;
		this.countryCode = countryCode;
		this.rate = rate;
		defaultRevenueAccount = new AccountElement().setVatCode(revenueVatCode);
		taxDebtAccount = new AccountElement().setVatCode(debtVatCode);
		taxReceivableAccount = new AccountElement().setVatCode(receivableVatCode);
		
	}
	
	public String getKey() {
		return key;
	}
	
	public double getRate() {
		return rate;
	}

	public LocalDate getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public LocalDate getVaildUntil() {
		return vaildUntil;
	}

	public void setVaildUntil(LocalDate vaildUntil) {
		this.vaildUntil = vaildUntil;
	}

	/**
	 * The country where this tax applies (jurisdiction)
	 * @return		country code
	 */
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * The country that this tax is applied to when trading with.
	 * If applicable.
	 * 
	 * @return	The trading country if applicable.
	 */
	public String getOtherCountry() {
		return otherCountry;
	}

	public void setOtherCountry(String otherCountry) {
		this.otherCountry = otherCountry;
	}

	/**
	 * The country group that this tax is applied to when trading with (EU)
	 * If applicable.
	 * 
	 * @return	The trading country group if applicable.
	 */
	public String getOtherCountryGroup() {
		return otherCountryGroup;
	}

	public void setOtherCountryGroup(String otherCountryGroup) {
		this.otherCountryGroup = otherCountryGroup;
	}
	
	public boolean isReverseCharge() {
		return reverseCharge;
	}

	public void setReverseCharge(boolean reverseCharge) {
		this.reverseCharge = reverseCharge;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public AccountElement getDefaultRevenueAccount() {
		return defaultRevenueAccount;
	}

	public void setDefaultRevenueAccount(AccountElement defaultRevenueAccount) {
		this.defaultRevenueAccount = defaultRevenueAccount;
	}

	public AccountElement getTaxDebtAccount() {
		return taxDebtAccount;
	}

	public void setTaxDebtAccount(AccountElement taxDebtAccount) {
		this.taxDebtAccount = taxDebtAccount;
	}

	public AccountElement getTaxReceivableAccount() {
		return taxReceivableAccount;
	}

	public void setTaxReceivableAccount(AccountElement taxReceivableAccount) {
		this.taxReceivableAccount = taxReceivableAccount;
	}
	
	
	
}
