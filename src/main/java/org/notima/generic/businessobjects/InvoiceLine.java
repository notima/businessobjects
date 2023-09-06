	package org.notima.generic.businessobjects;


import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.notima.generic.ifacebusinessobjects.OrderInvoiceLine;

@Entity
public class InvoiceLine implements OrderInvoiceLine {

	private String 	groupKey;
	private String	key;
	private String 	name;
	private double	qtyEntered;
	private double	qtyDelivered;
	private Double	priceActual;
	private double  lineNet;
	private int		lineNo;
	private String	taxKey;
	private double  taxPercent;
	private double	theirTotal;
	private double	taxAmount;
	private String	productKey;
	private String	description;
	private String	poDocumentNo;
	private String	poReference;
	private String	vendorProductNo;
	private String	UOM;
	private boolean	taxIncludedInPrice;
	private Double	priceNormal;
	private String	accountNo;
	
	@ManyToOne
	private Product<?>	product;
	
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
	/**
	 * Tax is specified in percent. That means 25% is 25.0
	 */
	public double getTaxPercent() {
		return taxPercent;
	}
	public void setTaxPercent(double taxPercent) {
		this.taxPercent = taxPercent;
	}
	public String getPoDocumentNo() {
		return poDocumentNo;
	}
	public void setPoDocumentNo(String poDocumentNo) {
		this.poDocumentNo = poDocumentNo;
	}
	public String getPoReference() {
		return poReference;
	}
	public void setPoReference(String poDocumentReference) {
		this.poReference = poDocumentReference;
	}
	public String getVendorProductNo() {
		return vendorProductNo;
	}
	public void setVendorProductNo(String vendorProductNo) {
		this.vendorProductNo = vendorProductNo;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	
	/**
	 * Calculates line totals using actual price, qty and tax percent.
	 * 
	 * If tax is included in the price the calculation assumes the price is
	 * including tax.
	 * 
	 * If tax is not included, the tax is added to the actual price / total.
	 *  
	 * @param roundingDecimals
	 * @see #setTaxIncludedInPrice(boolean)
	 * 
	 * @return		The line total including tax.
	 */
	public double calculateLineTotalIncTax(int roundingDecimals) {
		double lineTotal;
		if (taxIncludedInPrice) {
			double lineIncTax = round(priceActual * qtyEntered, roundingDecimals);
			taxAmount = lineIncTax - (lineIncTax / (1+(taxPercent/100.0)));
			taxAmount = round(taxAmount, roundingDecimals);
			lineNet = lineIncTax - taxAmount;
			lineTotal = lineIncTax;
		} else {
			lineNet = round(priceActual * qtyEntered, roundingDecimals);
			taxAmount = lineNet * (taxPercent/100.0);
			taxAmount = round(taxAmount, roundingDecimals);
			lineTotal = lineNet * (1+(taxPercent/100.0));
			lineTotal = round(lineTotal, roundingDecimals);
		}
		
		return lineTotal;
	}

	/**
	 * Rounding function.
	 * 
	 * @param value
	 * @param roundingDecimals
	 * @return
	 */
	/*public static double round(double value, int roundingDecimals) {
		// Round to rounding decimals
		double power = Math.pow(10.0, (double)roundingDecimals);
		double rounded = (double)(Math.round(value * power)) / power;
		return rounded;
	}*/
	
	public static double round(double value, int roundingDecimals) {
		DecimalFormat df = new DecimalFormat("#." + new String(new char[roundingDecimals]).replace("\0", "#"));
		df.setRoundingMode(RoundingMode.HALF_DOWN);
		return Double.parseDouble(df.format(value).replace(',', '.'));
	}
	
	public boolean isTaxIncludedInPrice() {
		return taxIncludedInPrice;
	}
	
	public void setTaxIncludedInPrice(boolean taxIncludedInPrice) {
		this.taxIncludedInPrice = taxIncludedInPrice;
	}
	public Product<?> getProduct() {
		return product;
	}
	public void setProduct(Product<?> product) {
		this.product = product;
	}
	public double getTheirTotal() {
		return theirTotal;
	}
	public void setTheirTotal(double theirTotal) {
		this.theirTotal = theirTotal;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	
	/**
	 * Line total excluding VAT
	 * 
	 * @return
	 */
	public double getLineNet() {
		return lineNet;
	}
	public void setLineNet(double lineNet) {
		this.lineNet = lineNet;
	}
	
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	public Double getPriceNormal() {
		if (priceNormal==null && priceActual!=null) {
			return priceActual;
		}
		return priceNormal;
	}
	public void setPriceNormal(Double priceNormal) {
		this.priceNormal = priceNormal;
	}
	@Override
	public boolean isPricesIncludeVAT() {
		return isTaxIncludedInPrice();
	}

	
	
}
