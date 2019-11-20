package org.notima.generic.businessobjects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PriceListLine {

	@ManyToOne
	private Product<?> product;
	private String pricelistReference;
	private double listPrice;
	private double stdPrice;
	private double limitPrice;
	private double qtyAvail;
	private String currency;
	private boolean	updateCost;
	private boolean	defaultSupplier;
	
	public Product<?> getProduct() {
		return product;
	}
	public void setProduct(Product<?> product) {
		this.product = product;
	}
	
	public boolean isUpdateCost() {
		return updateCost;
	}
	public void setUpdateCost(boolean updateCost) {
		this.updateCost = updateCost;
	}
	public boolean isDefaultSupplier() {
		return defaultSupplier;
	}
	public void setDefaultSupplier(boolean defaultSupplier) {
		this.defaultSupplier = defaultSupplier;
	}
	/**
	 * Reference to a specific price list that this price list line belongs to
	 * @return
	 */
	public String getPricelistReference() {
		return pricelistReference;
	}
	public void setPricelistReference(String pricelistReference) {
		this.pricelistReference = pricelistReference;
	}
	
	public double getListPrice() {
		return listPrice;
	}
	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}
	public double getStdPrice() {
		return stdPrice;
	}
	public void setStdPrice(double stdPrice) {
		this.stdPrice = stdPrice;
	}
	public double getLimitPrice() {
		return limitPrice;
	}
	public void setLimitPrice(double limitPrice) {
		this.limitPrice = limitPrice;
	}
	public double getQtyAvail() {
		return qtyAvail;
	}
	public void setQtyAvail(double qtyAvail) {
		this.qtyAvail = qtyAvail;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	
}
