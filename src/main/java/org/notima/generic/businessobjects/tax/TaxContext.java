package org.notima.generic.businessobjects.tax;

import java.time.LocalDate;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.InvoiceLine;
import org.notima.util.LocalDateUtils;

/**
 * The circumstances of a trade that the {@link TaxEngine} picks a {@link TaxRule} by.
 * Unknown circumstances are null: a rule with a condition on them then still applies, but
 * the engine reports if that leaves it unable to choose.
 */
public class TaxContext {

	private LocalDate	date;
	private boolean		sales = true;
	private String		companyCountry;
	private String		counterpartyCountry;
	private Boolean		counterpartyVatRegistered;
	private String		counterpartyVatType;
	/** True for a service, false for goods, null if unknown. */
	private Boolean		service;
	private String		categoryKey;
	/** The key of a rule chosen explicitly, which then applies if it can. */
	private String		taxKey;
	/**
	 * The VAT rate charged, if known. Used only when the tax category isn't: the rules with
	 * this rate are then taken as the line's, so an invoice without categories is posted as
	 * it was charged.
	 */
	private Double		rate;

	public TaxContext() {}

	/**
	 * The context of one line of an invoice, for a company in {@code companyCountry}. The
	 * line's tax category and goods/services override its product's; its tax key is taken as
	 * an explicit choice of rule.
	 */
	public static TaxContext of(Invoice<?> inv, InvoiceLine line, String companyCountry) {
		TaxContext c = new TaxContext();
		c.date = LocalDateUtils.asLocalDate(inv.getInvoiceDate());
		c.sales = inv.isSalesTransaction();
		c.companyCountry = companyCountry;
		BusinessPartner<?> bp = inv.getBusinessPartner();
		if (bp != null) {
			c.counterpartyCountry = bp.getCountryCode();
			c.counterpartyVatType = blankToNull(bp.getVatType());
			String vatNo = bp.getVatNo();
			c.counterpartyVatRegistered = vatNo != null && !vatNo.trim().isEmpty();
		}
		if (line != null) {
			c.categoryKey = line.getEffectiveTaxCategory();
			c.service = line.getEffectiveService();
			c.taxKey = blankToNull(line.getTaxKey());
			c.rate = line.getTaxPercent();
		}
		return c;
	}

	/** @return	Where the counterparty is, seen from the company. */
	public TradeArea getTradeArea() {
		return TradeArea.of(companyCountry, counterpartyCountry);
	}

	private static String blankToNull(String s) {
		return s == null || s.trim().isEmpty() ? null : s.trim();
	}

	// --- Fluent setters ---

	public TaxContext date(LocalDate d) { this.date = d; return this; }
	public TaxContext sales(boolean s) { this.sales = s; return this; }
	public TaxContext companyCountry(String c) { this.companyCountry = c; return this; }
	public TaxContext counterpartyCountry(String c) { this.counterpartyCountry = c; return this; }
	public TaxContext vatRegistered(Boolean r) { this.counterpartyVatRegistered = r; return this; }
	public TaxContext vatType(String t) { this.counterpartyVatType = t; return this; }
	public TaxContext service(Boolean s) { this.service = s; return this; }
	public TaxContext category(String k) { this.categoryKey = k; return this; }
	public TaxContext taxKey(String k) { this.taxKey = k; return this; }
	public TaxContext rate(Double r) { this.rate = r; return this; }

	// --- Accessors ---

	public LocalDate getDate() { return date; }
	public boolean isSales() { return sales; }
	public String getCompanyCountry() { return companyCountry; }
	public String getCounterpartyCountry() { return counterpartyCountry; }
	public Boolean getCounterpartyVatRegistered() { return counterpartyVatRegistered; }
	public String getCounterpartyVatType() { return counterpartyVatType; }
	public Boolean getService() { return service; }
	public String getCategoryKey() { return categoryKey; }
	public String getTaxKey() { return taxKey; }
	public Double getRate() { return rate; }

	public void setCategoryKey(String categoryKey) { this.categoryKey = categoryKey; }

}
