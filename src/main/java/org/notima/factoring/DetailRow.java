package org.notima.factoring;

/**
 * Interface used to create a standardized way of representing details on
 * orders and invoices.
 * 
 * @author Daniel Tamm
 *
 */
public interface DetailRow {

	public int getLineNo();
	
	public String getArtNo();
	
	public String getName();
	
	public String getDescription();
	
	public Double getPricePerUnit();
	
	public Double getQty();
	
	public String getUnit();

	/**
	 * Tax rate is stored in percent. 1.0 = 1%
	 */
	public Double getTaxRate();
	
	public Double getDiscountRate();

	public void setLineNo(int lineNo);
	
	public void setArtNo(String artNo);
	
	public void setName(String name);
	
	public void setDescription(String description);
	
	public void setPricePerUnit(Double price);
	
	public void setQty(Double qty);
	
	public void setUnit(String unit);
	
	/**
	 * Tax rate is stored in percent. 1.0 = 1%
	 * @param rate
	 */
	public void setTaxRate(Double rate);
	
	public void setDiscountRate(Double rate);
	
	public String getGroupCode();
	
	public void setGroupCode(String groupCode);
	
	public void setSKU(String sku);
	
	public String getSKU();
	
	public Double getVatAmount();
	
	public Double getLineTotalWithTax();
	
	/**
	 * Number of decimals. -1 means no rounding.
	 * @return
	 */
	public int getRoundingDecimals();
	
	public void setRoundingDecimals(int nr);
	
}
