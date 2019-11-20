package org.notima.generic.businessobjects;


/**
 * Warehouse locator information
 * 
 * Contains information about a product.
 * 
 * @author Daniel Tamm
 *
 */
public class WarehouseLocator {

	private String 	productReference;
	private String	locator;
	private String	locatorReference;
	private Double	qty;
	private Double	qtyOnHand;
	private Double	qtyReserved;
	private Double	qtyAvailable;
	
	public String getProductReference() {
		return productReference;
	}
	public void setProductReference(String productReference) {
		this.productReference = productReference;
	}
	public String getLocator() {
		return locator;
	}
	public void setLocator(String locator) {
		this.locator = locator;
	}
	public String getLocatorReference() {
		return locatorReference;
	}
	public void setLocatorReference(String locatorReference) {
		this.locatorReference = locatorReference;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Double getQtyOnHand() {
		return qtyOnHand;
	}
	public void setQtyOnHand(Double qtyOnHand) {
		this.qtyOnHand = qtyOnHand;
	}
	public Double getQtyReserved() {
		return qtyReserved;
	}
	public void setQtyReserved(Double qtyReserved) {
		this.qtyReserved = qtyReserved;
	}
	public Double getQtyAvailable() {
		return qtyAvailable;
	}
	public void setQtyAvailable(Double qtyAvailable) {
		this.qtyAvailable = qtyAvailable;
	}	
	
}
