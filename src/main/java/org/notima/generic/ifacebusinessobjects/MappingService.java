package org.notima.generic.ifacebusinessobjects;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Location;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;

public interface MappingService {

	public TaxSubjectIdentifier mapApartmentNoToTaxSubject(String aptNo);
	
	public BusinessPartner<?> mapLocationToBusinessPartner(Location location, String aptNo);
	
}
