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

	
	
}
