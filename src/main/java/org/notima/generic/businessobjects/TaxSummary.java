package org.notima.generic.businessobjects;

/**
 * Record used to summarize tax
 * 
 * @author Daniel Tamm
 *
 */
public class TaxSummary {

	private String  key;
	private double	rate;
	private double	taxBase;
	private double	taxAmount;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getTaxBase() {
		return taxBase;
	}
	public void setTaxBase(double taxBase) {
		this.taxBase = taxBase;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public TaxSummary add(double taxBase, double taxAmount) {
		this.taxBase += taxBase;
		this.taxAmount += taxAmount;
		return this;
	}
	
	public boolean isEmpty() {
		return taxBase==0 && taxAmount==0;
	}

	/**
	 * 
	 * @return	The total inc tax.
	 */
	public double calculateTotal() {
		return taxBase + taxAmount;
	}
	
	/**
	 * Calculates tax amount and tax base from the total amount and tax rate.
	 * 
	 * @param incTax		The total amount (taxbase + taxAmount).
	 * @param taxRate		The rate added on tax base.
	 */
	public void calculateValuesFrom(double incTax, double taxRate) {
		rate = taxRate;
		double baseMultiplier = 1 / (1+taxRate);
		taxBase = incTax * baseMultiplier;
		taxAmount = taxBase * taxRate;
	}
	
	
	/**
	 * Clone a tax summary object
	 */
	public TaxSummary clone() {
		TaxSummary ts = new TaxSummary();
		ts.setKey(key);
		ts.setRate(rate);
		ts.setTaxAmount(taxAmount);
		ts.setTaxBase(taxBase);
		return ts;
	}
	
}
