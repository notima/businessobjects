package org.notima.generic.ifacebusinessobjects;

public interface OrderInvoiceLine {

	public int getLineNo();
	
	public String getGroupKey();
	
	public String getKey();
	
	public String getName();
	
	public double getQtyEntered();
	
	public String getAccountNo();
	
	/**
	 * Actual price per unit.
	 * @return
	 */
	public Double getPriceActual();
	
	public void setPriceActual(Double price);
	
	public String getTaxKey();
	
	public void setTaxKey(String taxKey);
	
	public String getProductKey();
	
	public String getDescription();

	public String getProject();
	
	public String getCostCenter();
	
	/**
	 * Value 0 - 100
	 * @return
	 */
	public double getTaxPercent();
	
	public void setTaxPercent(double taxRate);
	
	public String getUOM();
	
	public boolean isPricesIncludeVAT();
	
	public void setPricesIncludeVAT(boolean include);
	
	/**
	 * Price without discounts (per unit)
	 */
	public Double getPriceNormal();
	
	public double calculateLineTotalIncTax(int roundingDecimals);	
	
}
