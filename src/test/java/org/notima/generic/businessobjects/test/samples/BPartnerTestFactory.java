package org.notima.generic.businessobjects.test.samples;

import org.notima.generic.businessobjects.BusinessPartner;

public class BPartnerTestFactory {

	public static BusinessPartner<?> buildTestBusinessPartnerNameOnly() {
		
		BusinessPartner<?> bp = new BusinessPartner<Object>(); 

		bp.setName("Test Testsson");
		bp.setTaxId("999999-9999");
		
		return bp;
		
	}
	
}
