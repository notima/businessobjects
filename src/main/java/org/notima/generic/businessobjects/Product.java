package org.notima.generic.businessobjects;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import org.notima.generic.ifacebusinessobjects.IProduct;

@Entity
@XmlRootElement(name = "Product")
public class Product<P> implements Serializable, IProduct {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1831457768717483184L;
	protected String key;
	protected String keyReference;
	protected String name;
	protected String taxKey;
	protected String brand;
	protected String countryOfOrigin;
	protected String upc;
	protected String vendorProductNo;
	protected String productCategory;
	protected String productCategoryReference;
	protected String unit;
	protected String packageInfo;
	protected boolean isNew;
	protected boolean active;
	protected double	weight;
	protected double	length;
	protected double	width;
	protected double	height;
	protected int	 unitsPerPack;
	protected int	 unitsPerPallet;
	protected List<Translation> translations;
	protected List<KeyValue>	attributes;
	protected transient P nativeProduct;
	
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
	public String getTaxKey() {
		return taxKey;
	}
	public void setTaxKey(String taxKey) {
		this.taxKey = taxKey;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}
	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public String getVendorProductNo() {
		return vendorProductNo;
	}
	public void setVendorProductNo(String vendorProductNo) {
		this.vendorProductNo = vendorProductNo;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPackageInfo() {
		return packageInfo;
	}
	public void setPackageInfo(String packageInfo) {
		this.packageInfo = packageInfo;
	}
	
	/**
	 * Flag to indicate if the product is new or not.
	 * 
	 * @return
	 */
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (key!=null)
			buf.append(key);
		
		if (name!=null) {
			if (buf.length()>0) buf.append(" ");
			buf.append(name);
		}
		
		if (brand!=null) {
			if (buf.length()>0) buf.append(" ");
			buf.append(brand);
		}
		
		if (packageInfo!=null) {
			if (buf.length()>0) buf.append(" ");
			buf.append(packageInfo);
		}
		
		if (unit!=null) {
			if (buf.length()>0) buf.append(" ");
			buf.append(unit);
		}
		return buf.toString();
		
	}
	
	/**
	 * When synching between different systems this reference can be 
	 * used to map the category from one system to another
	 * 
	 * @return
	 */
	public String getKeyReference() {
		return keyReference;
	}
	public void setKeyReference(String keyReference) {
		this.keyReference = keyReference;
	}
	
	/**
	 * When synching between different systems this reference can be 
	 * used to map the category from one system to another
	 * 
	 * @return
	 */
	public String getProductCategoryReference() {
		return productCategoryReference;
	}
	public void setProductCategoryReference(String productCategoryReference) {
		this.productCategoryReference = productCategoryReference;
	}
	public List<Translation> getTranslations() {
		return translations;
	}
	public void setTranslations(List<Translation> translations) {
		this.translations = translations;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<KeyValue> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<KeyValue> attributes) {
		this.attributes = attributes;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public int getUnitsPerPack() {
		return unitsPerPack;
	}
	public void setUnitsPerPack(int unitsPerPack) {
		this.unitsPerPack = unitsPerPack;
	}
	public int getUnitsPerPallet() {
		return unitsPerPallet;
	}
	public void setUnitsPerPallet(int unitsPerPallet) {
		this.unitsPerPallet = unitsPerPallet;
	}
	public P getNativeProduct() {
		return nativeProduct;
	}
	public void setNativeProduct(P nativeProduct) {
		this.nativeProduct = nativeProduct;
	}
	
	
	
}
