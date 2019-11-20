package org.notima.generic.ifacebusinessobjects;

public interface OrderInvoiceLine {

	public int getLineNo();
	
	public String getGroupKey();
	
	public String getKey();
	
	public String getName();
	
	public double getQtyEntered();
	
	/**
	 * Actual price per unit.
	 * @return
	 */
	public Double getPriceActual();
	
	public String getTaxKey();
	
	public String getProductKey();
	
	public String getDescription();

	/**
	 * Value 0 - 100
	 * @return
	 */
	public double getTaxPercent();
	
	public String getUOM();
	
	public boolean isPricesIncludeVAT();
	
	/**
	 * Price without discounts (per unit)
	 */
	public Double getPriceNormal();
	
}
