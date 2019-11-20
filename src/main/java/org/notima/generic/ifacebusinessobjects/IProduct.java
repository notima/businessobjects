package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.KeyValue;
import org.notima.generic.businessobjects.Translation;

public interface IProduct {

	public String getKey();
	public void setKey(String key);

	public String getName();
	public void setName(String name);
	
	public String getTaxKey();
	public void setTaxKey(String key);
	
	public String getBrand();
	public void setBrand(String brand);
	
	public String getCountryOfOrigin();
	public void setCountryOfOrigin(String cos);
	
	public String getUpc();
	public void setUpc(String upc);
	
	public String getVendorProductNo();
	public void setVendorProductNo(String no);
	
	public String getProductCategory();
	public void setProductCategory(String pc);
	
	public String getProductCategoryReference();
	public void setProductCategoryReference(String pcr);
	
	public String getUnit();
	public void setUnit(String unit);
	
	public String getPackageInfo();
	public void setPackageInfo(String info);
	
	/**
	 * Flag to indicate if the product is new or not.
	 * 
	 * @return
	 */
	public boolean isNew();
	public void setNew(boolean flag);
	
	public List<Translation> getTranslations();
	
	public void setTranslations(List<Translation> translations);
	public boolean isActive();
	public void setActive(boolean active);
	
	public List<KeyValue> getAttributes();
	public void setAttributes(List<KeyValue> attributes);
	
	public double getWeight();
	public void setWeight(double weight);
	public double getLength();
	public void setLength(double length);
	public double getWidth();
	public void setWidth(double width);
	public double getHeight();
	public void setHeight(double height);
	public int getUnitsPerPack();
	public void setUnitsPerPack(int unitsPerPack);
	public int getUnitsPerPallet();
	public void setUnitsPerPallet(int unitsPerPallet);
	
	
}
