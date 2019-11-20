package org.notima.generic.businessobjects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "ProductInfoBatch")
public class ProductInfoBatch {

	@OneToMany
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
