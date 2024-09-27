package org.notima.factoring;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Implements a basic detail row using local variables.
 * 
 * @author Daniel Tamm
 *
 */
@XmlRootElement
public class BasicDetailRow implements DetailRow {
	
	private int		m_lineNo;
	private String 	m_artNo;
	private String	m_name;
	private String	m_description;
	private Double	m_ppu;
	private Double	m_qty;
	private String	m_unit;
	private Double	m_taxRate;
	private Double	m_discountRate;
	private String	m_groupCode;
	private String	m_sku;
	private int		roundingDecimals = 2; // Defaults to 2
	
	public BasicDetailRow() {}
	
	public BasicDetailRow(String artNo, String name, Double priceEach, Double qty) {
		m_lineNo = 0;
		m_artNo = artNo;
		m_name = name;
		m_description = "";
		m_ppu = priceEach;
		m_qty = qty;
		m_unit = null;
		m_taxRate = 0.0;
		m_discountRate = 0.0;
	}
	
	public BasicDetailRow(int lineNo, String artNo, String name, String desc,
						  Double priceEach, Double qty, String unit,
						  Double taxRate, Double discountRate) {
		
		m_lineNo = lineNo;
		m_artNo = artNo;
		m_name = name;
		m_description = desc;
		m_ppu = priceEach;
		m_qty = qty;
		m_unit = unit;
		m_taxRate = taxRate;
		m_discountRate = discountRate;
	}
	
	@Override
	public int getLineNo() {
		return m_lineNo;
	}

	@Override
	public String getArtNo() {
		return m_artNo;
	}

	@Override
	public String getName() {
		return m_name;
	}

	@Override
	public String getDescription() {
		return m_description;
	}

	@Override
	public Double getPricePerUnit() {
		if (m_ppu==null)
			return 0.0;
		return m_ppu;
	}

	@Override
	public Double getQty() {
		if (m_qty==null)
			return 0.0;
		return m_qty;
	}

	@Override
	public String getUnit() {
		return m_unit;
	}

	@Override
	public Double getTaxRate() {
		if (m_taxRate==null)
			return 0.0;
		return m_taxRate;
	}

	@Override
	public Double getDiscountRate() {
		return m_discountRate;
	}

	@Override
	public void setLineNo(int lineNo) {
		m_lineNo = lineNo;
	}

	@Override
	public void setArtNo(String artNo) {
		m_artNo = artNo;
	}

	@Override
	public void setName(String name) {
		m_name = name;
	}

	@Override
	public void setDescription(String description) {
		m_description = description;
	}

	@Override
	public void setPricePerUnit(Double price) {
		m_ppu = price;
	}

	@Override
	public void setQty(Double qty) {
		m_qty = qty;
	}

	@Override
	public void setUnit(String unit) {
		m_unit = unit;
	}

	@Override
	public void setTaxRate(Double rate) {
		m_taxRate = rate;
	}

	@Override
	public void setDiscountRate(Double rate) {
		m_discountRate = rate;
	}

	@Override
	public String getGroupCode() {
		return m_groupCode;
	}

	@Override
	public void setGroupCode(String groupCode) {
		m_groupCode = groupCode;
	}

	public void setSKU(String sku) {
		m_sku = sku;
	}
	
	public String getSKU() {
		return(m_sku);
	}
	
	public Double getVatAmount() {
		if (m_taxRate==null || m_qty==null || m_ppu==null)
			return 0.0;
		return(m_taxRate*m_qty*m_ppu);
	}
	
	@XmlElement(name="LineTotalWithTax")
	public Double getLineTotalWithTax() {
		if (m_ppu==null || m_qty==null)
			return 0.0;
		double lineExTax = m_ppu * m_qty;
		if (m_discountRate!=null) {
			lineExTax = (1-(m_discountRate/100.0))*lineExTax;
		}
		if (m_taxRate!=null) {
			lineExTax = lineExTax * (1+m_taxRate/100.0);
		}
		// Round
		if (roundingDecimals>-1) {
			long integervalue = Math.round(lineExTax * Math.pow(10, roundingDecimals));
			lineExTax = (double)integervalue/Math.pow(10, roundingDecimals);
		}
		return lineExTax;
	}

	@Override
	public int getRoundingDecimals() {
		return roundingDecimals;
	}

	@Override
	public void setRoundingDecimals(int nr) {
		roundingDecimals = nr;
	}
	
	public String toString() {
		
		return 
				(m_artNo!=null ? (m_artNo + " ") : "") +
				(m_name!=null ? (m_name + " ") : "") + 
				(m_description!=null ? (m_description + " ") : "") +
				(m_qty!=null ? (m_qty + " ") : "") + 
				(m_unit!=null ? (m_unit + " ") : "") + 
				(m_ppu!=null ? (m_ppu + " ") : "") + 
				getLineTotalWithTax();
		
	}
	
}
