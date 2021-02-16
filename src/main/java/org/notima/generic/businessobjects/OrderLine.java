package org.notima.generic.businessobjects;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.notima.generic.ifacebusinessobjects.OrderInvoiceLine;

@Entity
public class OrderLine implements OrderInvoiceLine {

	private String 	groupKey;
	private String	key;
	private String 	name;
	private double	qtyEntered;
	private double	qtyDelivered;
	private Date	dateLastDelivered;
	private Double	priceActual;
	private int		lineNo;
	private String	taxKey;
	private double  taxPercent;
	private double	taxAmount;
	private double	lineNet;
	private String	productKey;
	private String	description;
	private String 	UOM;
	private Double	priceNormal;
	private boolean pricesIncludeVAT;
	
	@ManyToOne
	private Product<?> product;
	
	public Product<?> getProduct() {
		return product;
	}

	public void setProduct(Product<?> product) {
		this.product = product;
	}

	public String toString() {
		return "[productKey=>" + productKey + "], [qtyEntered=>" + qtyEntered + "], [UOM=>" + UOM +"]";
	}
	
	public String getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getQtyEntered() {
		return qtyEntered;
	}
	public void setQtyEntered(double qtyEntered) {
		this.qtyEntered = qtyEntered;
	}
	
	public double getQtyDelivered() {
		return qtyDelivered;
	}

	public void setQtyDelivered(double qtyDelivered) {
		this.qtyDelivered = qtyDelivered;
	}

	public Date getDateLastDelivered() {
		return dateLastDelivered;
	}

	public void setDateLastDelivered(Date dateLastDelivered) {
		this.dateLastDelivered = dateLastDelivered;
	}

	public Double getPriceActual() {
		if (priceActual==null && priceNormal!=null)
			return priceNormal;
		return priceActual;
	}
	public void setPriceActual(Double priceActual) {
		this.priceActual = priceActual;
		
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public String getTaxKey() {
		return taxKey;
	}
	public void setTaxKey(String taxKey) {
		this.taxKey = taxKey;
	}
	public double getTaxPercent() {
		return taxPercent;
	}
	public void setTaxPercent(double taxPercent) {
		this.taxPercent = taxPercent;
	}
	
	/**
	 * Calculates tax percent based on priceactual and supplied priceIncVat
	 * 
	 * @param priceIncVat
	 * @return
	 */
	public double calculateTaxPercent(double priceIncVat) {
		
		taxAmount = priceIncVat - priceActual;
		
		double taxP = taxAmount / priceActual;
		
		// Round to two decimals
		taxP = (Math.round(taxP * 100.0 ) / 100.0);

		taxPercent = taxP;
		lineNet = qtyEntered * (pricesIncludeVAT ? (priceActual-taxAmount) : (priceActual));
		
		return taxPercent;
	}


	/**
	 * Calculates price actual from total line amount, qtyEntered, pricesIncludeVAT and taxPercent.
	 * 
	 * @param lineTotal				The total to use.
	 * @param roundingDecimals		Rounding precision.
	 * @return		The price actual.
	 */
	public double calculatePriceActualFromLineTotalIncTax(double lineTotal, int roundingDecimals) {

		// Can't recalculate if qty entered = 0.
		if (qtyEntered==0) return priceActual;
		
		double lineNet;
		
		if (pricesIncludeVAT) {
			priceActual = InvoiceLine.round(lineTotal / qtyEntered, roundingDecimals);
		} else {
			lineNet = InvoiceLine.round(lineTotal / ( 1+(taxPercent/100.0)), roundingDecimals);
			priceActual = InvoiceLine.round(lineNet / qtyEntered, roundingDecimals);
		}
		
		return priceActual;
		
	}
	
	public double calculateLineTotalIncTax(int roundingDecimals) {
		double lineTotal;
		if (pricesIncludeVAT) {
			double lineIncTax = InvoiceLine.round(priceActual * qtyEntered, roundingDecimals);
			taxAmount = lineIncTax - (lineIncTax / (1+(taxPercent/100.0)));
			taxAmount = InvoiceLine.round(taxAmount, roundingDecimals);
			lineNet = lineIncTax - taxAmount;
			lineTotal = lineIncTax;
		} else {
			lineNet = InvoiceLine.round(priceActual * qtyEntered, roundingDecimals);
			taxAmount = lineNet * (taxPercent/100.0);
			taxAmount = InvoiceLine.round(taxAmount, roundingDecimals);
			lineTotal = lineNet * (1+(taxPercent/100.0));
			lineTotal = InvoiceLine.round(lineTotal, roundingDecimals);
		}
		
		return lineTotal;
	}
	
	
	public String getProductKey() {
		return productKey;
	}
	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}

	public Double getPriceNormal() {
		if (priceNormal==null && priceActual!=null)
			return priceActual;
		return priceNormal;
	}

	public void setPriceNormal(Double priceNormal) {
		this.priceNormal = priceNormal;
	}

	public boolean isPricesIncludeVAT() {
		return pricesIncludeVAT;
	}

	public void setPricesIncludeVAT(boolean pricesIncludeVAT) {
		this.pricesIncludeVAT = pricesIncludeVAT;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getLineNet() {
		return lineNet;
	}

	public void setLineNet(double lineNet) {
		this.lineNet = lineNet;
	}
	

	

}
