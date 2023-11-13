package org.notima.generic.ifacebusinessobjects;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Location;

public interface MappingService {

	public String mapSourceToTarget(String srcValue, String typeOfObject);
	
	public BusinessPartner<?> mapLocationToBusinessPartner(Location location, String aptNo);
	
}
