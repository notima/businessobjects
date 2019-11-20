package org.notima.generic.businessobjects;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The product info class keeps information on both a product, vendor and price.
 * It's a composite class for sending product information.
 * 
 * @author Daniel Tamm
 *
 */
@Entity
@XmlType(name="ProductInfo")
@XmlRootElement(name = "ProductInfo")
public class ProductInfo {

	@ManyToOne
	private	Product<?>				product;
	@ManyToOne
	private BusinessPartner<?>		vendor;
	private double				price;
	private String				currency;
	private Date				validDate;
	private List<WarehouseInfo>		warehouseInfo;

	@XmlElement(name="Product")
	public Product<?> getProduct() {
		return product;
	}
	public void setProduct(Product<?> product) {
		this.product = product;
	}
	public BusinessPartner<?> getVendor() {
		return vendor;
	}
	public void setVendor(BusinessPartner<?> vendor) {
		this.vendor = vendor;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	
	public List<WarehouseInfo> getWarehouseInfo() {
		return warehouseInfo;
	}
	public void setWarehouseInfo(List<WarehouseInfo> warehouseInfo) {
		this.warehouseInfo = warehouseInfo;
	}
	
	
	
	
}
