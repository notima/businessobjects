package org.notima.generic.businessobjects;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BusinessPartnerList")
public class BusinessPartnerList<B> {

	private List<BusinessPartner<B>> BusinessPartner;

	@XmlElement(name="BusinessPartner")
	public List<BusinessPartner<B>> getBusinessPartner() {
		return BusinessPartner;
	}

	public void setBusinessPartner(List<BusinessPartner<B>> businessPartners) {
		BusinessPartner = businessPartners;
	}
	
	
}
