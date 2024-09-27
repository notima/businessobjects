package org.notima.generic.businessobjects;

import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ProductInfoBatch")
public class ProductInfoBatch {

	private List<ProductInfo>	productInfo;
	// Business partner for this whole batch
	private BusinessPartner<?>		businessPartner;

	public List<ProductInfo> getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(List<ProductInfo> productInfo) {
		this.productInfo = productInfo;
	}

	public BusinessPartner<?> getBusinessPartner() {
		return businessPartner;
	}

	public void setBusinessPartner(BusinessPartner<?> businessPartner) {
		this.businessPartner = businessPartner;
	}
	
	
	
}
