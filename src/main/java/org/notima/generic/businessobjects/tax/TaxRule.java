package org.notima.generic.businessobjects.tax;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * One way of taxing a supply, as {@code C_Tax} with its {@code C_Tax_Acct} in iDempiere: under
 * which conditions it applies, its rate, and the accounts it's posted to. The accounts are
 * kept here, not in the chart of accounts, so the same rate can be posted differently
 * depending on the trade (domestic, EU, export, reverse charge).
 *
 * <p><b>Conditions</b>, all of which must hold for the rule to apply (an unset condition
 * holds always): {@link #getScope() scope} (sales/purchases), {@link #getValidFrom() validity},
 * {@link #getCategoryKey() tax category}, {@link #getTradeArea() trade area},
 * {@link #getCounterpartyCountry() country} of the counterparty, {@link #isAppliesToGoods() goods}
 * or {@link #isAppliesToServices() services}, whether the counterparty is
 * {@link #getVatRegisteredCounterparty() VAT registered}, and its
 * {@link #getCounterpartyVatTypes() VAT treatment}. Among the rules that apply, the
 * {@link TaxEngine} picks the most specific.</p>
 *
 * <p><b>Reverse charge</b> (omvänd skattskyldighet): the invoice has no VAT ({@link #getRate()}
 * 0), and on a purchase the buyer accounts for it at {@link #getReverseChargeRate()}, both as
 * output VAT ({@link #getReverseChargeOutputAccount()}) and as input VAT
 * ({@link #getInputVatAccount()}).</p>
 *
 * <p><b>Accounts</b>: VAT accounts, and optionally the revenue or expense account of the
 * supply itself (ie EU sales of goods to their own revenue account). Without one, the line's
 * own account or the default revenue/purchase account is used.</p>
 */
public class TaxRule {

	private String		key;
	private String		name;
	private TaxScope	scope = TaxScope.BOTH;
	private LocalDate	validFrom;
	private LocalDate	validTo;

	// --- Conditions ---
	/** The {@link TaxCategory} it applies to; null for all. */
	private String		categoryKey;
	private TradeArea	tradeArea = TradeArea.ANY;
	/** Only for counterparties in this country (ISO 3166); null for all. */
	private String		counterpartyCountry;
	private boolean		appliesToGoods = true;
	private boolean		appliesToServices = true;
	/** True: only VAT registered counterparties; false: only those that aren't; null: all. */
	private Boolean		vatRegisteredCounterparty;
	/**
	 * Only for counterparties with one of these VAT treatments
	 * ({@link org.notima.generic.businessobjects.BusinessPartner#getVatType()}, ie
	 * {@code SEREVERSEDVAT}); empty for all.
	 */
	private List<String> counterpartyVatTypes = new ArrayList<String>();

	// --- Rate ---
	/** The VAT rate in percent charged on the invoice. */
	private double		rate;
	private boolean		reverseCharge;
	/** The rate the buyer accounts for under reverse charge. */
	private double		reverseChargeRate;

	// --- Accounts ---
	/** Output VAT (sales), T_Due in iDempiere. */
	private String		outputVatAccount;
	/** Input VAT (purchases, and reverse charge), T_Credit in iDempiere. */
	private String		inputVatAccount;
	/** Output VAT the buyer accounts for under reverse charge. */
	private String		reverseChargeOutputAccount;
	/** Revenue account of a sale under this rule; null for the line's or the default one. */
	private String		revenueAccount;
	/** Expense account of a purchase under this rule; null for the line's or the default one. */
	private String		expenseAccount;

	/** Where the trade is reported, ie the boxes of the Swedish VAT return ("05", "10"). */
	private List<String> reportingCodes = new ArrayList<String>();

	public TaxRule() {}

	public TaxRule(String key, String name, TaxScope scope, String categoryKey, double rate) {
		this.key = key;
		this.name = name;
		this.scope = scope;
		this.categoryKey = categoryKey;
		this.rate = rate;
	}

	/** @return	True if the rule is valid on {@code date} (always, if date is null). */
	public boolean isValidOn(LocalDate date) {
		if (date == null) return true;
		return (validFrom == null || !date.isBefore(validFrom)) && (validTo == null || !date.isAfter(validTo));
	}

	// --- Fluent setters for defining rules in code ---

	public TaxRule tradeArea(TradeArea area) { this.tradeArea = area; return this; }
	public TaxRule goodsOnly() { this.appliesToGoods = true; this.appliesToServices = false; return this; }
	public TaxRule servicesOnly() { this.appliesToGoods = false; this.appliesToServices = true; return this; }
	public TaxRule vatRegistered(Boolean registered) { this.vatRegisteredCounterparty = registered; return this; }
	public TaxRule forVatTypes(String... vatTypes) { for (String t : vatTypes) counterpartyVatTypes.add(t); return this; }
	public TaxRule reverseCharge(double rate) { this.reverseCharge = true; this.reverseChargeRate = rate; return this; }
	public TaxRule outputVat(String acct) { this.outputVatAccount = acct; return this; }
	public TaxRule inputVat(String acct) { this.inputVatAccount = acct; return this; }
	public TaxRule reverseChargeOutput(String acct) { this.reverseChargeOutputAccount = acct; return this; }
	public TaxRule revenue(String acct) { this.revenueAccount = acct; return this; }
	public TaxRule expense(String acct) { this.expenseAccount = acct; return this; }
	public TaxRule reporting(String... codes) { for (String c : codes) reportingCodes.add(c); return this; }

	@Override
	public String toString() {
		return key + (name != null ? " " + name : "");
	}

	// --- Bean accessors ---

	public String getKey() { return key; }
	public void setKey(String key) { this.key = key; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public TaxScope getScope() { return scope; }
	public void setScope(TaxScope scope) { this.scope = scope != null ? scope : TaxScope.BOTH; }

	public LocalDate getValidFrom() { return validFrom; }
	public void setValidFrom(LocalDate validFrom) { this.validFrom = validFrom; }

	public LocalDate getValidTo() { return validTo; }
	public void setValidTo(LocalDate validTo) { this.validTo = validTo; }

	public String getCategoryKey() { return categoryKey; }
	public void setCategoryKey(String categoryKey) { this.categoryKey = categoryKey; }

	public TradeArea getTradeArea() { return tradeArea; }
	public void setTradeArea(TradeArea tradeArea) { this.tradeArea = tradeArea != null ? tradeArea : TradeArea.ANY; }

	public String getCounterpartyCountry() { return counterpartyCountry; }
	public void setCounterpartyCountry(String counterpartyCountry) { this.counterpartyCountry = counterpartyCountry; }

	public boolean isAppliesToGoods() { return appliesToGoods; }
	public void setAppliesToGoods(boolean appliesToGoods) { this.appliesToGoods = appliesToGoods; }

	public boolean isAppliesToServices() { return appliesToServices; }
	public void setAppliesToServices(boolean appliesToServices) { this.appliesToServices = appliesToServices; }

	public Boolean getVatRegisteredCounterparty() { return vatRegisteredCounterparty; }
	public void setVatRegisteredCounterparty(Boolean vatRegisteredCounterparty) { this.vatRegisteredCounterparty = vatRegisteredCounterparty; }

	public List<String> getCounterpartyVatTypes() { return counterpartyVatTypes; }
	public void setCounterpartyVatTypes(List<String> v) { this.counterpartyVatTypes = v != null ? v : new ArrayList<String>(); }

	public double getRate() { return rate; }
	public void setRate(double rate) { this.rate = rate; }

	public boolean isReverseCharge() { return reverseCharge; }
	public void setReverseCharge(boolean reverseCharge) { this.reverseCharge = reverseCharge; }

	public double getReverseChargeRate() { return reverseChargeRate; }
	public void setReverseChargeRate(double reverseChargeRate) { this.reverseChargeRate = reverseChargeRate; }

	public String getOutputVatAccount() { return outputVatAccount; }
	public void setOutputVatAccount(String outputVatAccount) { this.outputVatAccount = outputVatAccount; }

	public String getInputVatAccount() { return inputVatAccount; }
	public void setInputVatAccount(String inputVatAccount) { this.inputVatAccount = inputVatAccount; }

	public String getReverseChargeOutputAccount() { return reverseChargeOutputAccount; }
	public void setReverseChargeOutputAccount(String v) { this.reverseChargeOutputAccount = v; }

	public String getRevenueAccount() { return revenueAccount; }
	public void setRevenueAccount(String revenueAccount) { this.revenueAccount = revenueAccount; }

	public String getExpenseAccount() { return expenseAccount; }
	public void setExpenseAccount(String expenseAccount) { this.expenseAccount = expenseAccount; }

	public List<String> getReportingCodes() { return reportingCodes; }
	public void setReportingCodes(List<String> v) { this.reportingCodes = v != null ? v : new ArrayList<String>(); }

}
