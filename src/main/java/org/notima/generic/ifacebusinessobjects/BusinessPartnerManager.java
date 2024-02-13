package org.notima.generic.ifacebusinessobjects;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;

/**
 * Interface for managing business partners for a system.
 * 
 * @param <B>
 */
public interface BusinessPartnerManager<B> {

	public BusinessPartner<B> lookupBusinessPartner(TaxSubjectIdentifier tsi);
	
	public BusinessPartner<B> addBusinessPartner(TaxSubjectIdentifier tsi, BusinessPartner<B> bp);
	
	public boolean removeBusinessPartner(TaxSubjectIdentifier tsi);
	
	public String getSystemName();
	
}
