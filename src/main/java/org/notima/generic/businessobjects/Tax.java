package org.notima.generic.businessobjects;

import java.time.LocalDate;

public class Tax {

	public static final String TAX_KEY_UNKNOWN = "?";
	
	private String key;
	private double rate;
	
	private AccountElement	defaultRevenueAccount;
	private AccountElement	taxDebtAccount;
	private AccountElement	taxReceivableAccount;
	
	private boolean		appliesToServices = true;
	private boolean		appliesToGoods = true;
	
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
		defaultRevenueAccount = new AccountElement().setTaxKey(revenueVatCode);
		taxDebtAccount = new AccountElement().setTaxKey(debtVatCode);
		taxReceivableAccount = new AccountElement().setTaxKey(receivableVatCode);
		
	}
	
	public String getKey() {
		return key;
	}
	
	/**
	 * This is the rate expressed in percent. That means that the value of 100.0 is 100 percent.
	 * @return
	 */
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

	/**
	 * Sets the rate. 100.0 means 100 %.
	 * 
	 * @param rate
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * This is the account element where revenue where this tax applies is normally
	 * accounted.
	 * 
	 * @return
	 */
	public AccountElement getDefaultRevenueAccount() {
		return defaultRevenueAccount;
	}

	public void setDefaultRevenueAccount(AccountElement defaultRevenueAccount) {
		this.defaultRevenueAccount = defaultRevenueAccount;
	}

	/**
	 * This is the account element where tax payables for this tax normally is accounted.
	 * 
	 * @return
	 */
	public AccountElement getTaxDebtAccount() {
		return taxDebtAccount;
	}

	public void setTaxDebtAccount(AccountElement taxDebtAccount) {
		this.taxDebtAccount = taxDebtAccount;
	}

	/**
	 * This is the account element where tax to be refunded for this tax is normally accounted.
	 * 
	 * @return
	 */
	public AccountElement getTaxReceivableAccount() {
		return taxReceivableAccount;
	}

	public void setTaxReceivableAccount(AccountElement taxReceivableAccount) {
		this.taxReceivableAccount = taxReceivableAccount;
	}

	/**
	 * True if this tax is applicable to services rendered.
	 * 
	 * @return
	 */
	public boolean isAppliesToServices() {
		return appliesToServices;
	}

	public void setAppliesToServices(boolean appliesToServices) {
		this.appliesToServices = appliesToServices;
	}

	/**
	 * This is this tax is applicable to goods sold / bought
	 * 
	 * @return
	 */
	public boolean isAppliesToGoods() {
		return appliesToGoods;
	}

	public void setAppliesToGoods(boolean appliesToGoods) {
		this.appliesToGoods = appliesToGoods;
	}
	
}
