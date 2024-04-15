package org.notima.generic.businessobjects.test.samples;

import org.notima.generic.businessobjects.BusinessPartner;

public class BPartnerTestFactory {

	public static BusinessPartner<?> buildTestBusinessPartnerNameOnly() {
		
		BusinessPartner<?> bp = new BusinessPartner<Object>(); 

		bp.setName("Test Testsson");
		bp.setTaxId("999999-9999");
		
		return bp;
		
	}
	
	public static BusinessPartner<?> buildTestBusinessPartnerNameAndIdentityNo() {
		
		BusinessPartner<?> bp = new BusinessPartner<Object>(); 

		bp.setName("Test Testsson 10");
		bp.setIdentityNo("10");
		
		return bp;
		
	}
	
	public static BusinessPartner<?> buildTestBusinessPartnerSweden() {
		
		BusinessPartner<?> bp = new BusinessPartner<Object>(); 

		bp.setName("Sven Svensson");
		bp.setIdentityNo("101");
		bp.setCountryCode("SE");
		
		return bp;
		
	}
	
	
	
}
