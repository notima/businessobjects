package org.notima.generic.ifacebusinessobjects;

import org.notima.generic.businessobjects.TaxSubjectIdentifier;
import org.notima.generic.businessobjects.exception.NoSuchTenantException;

/**
 * A service that help mapping one type of entity ID to another
 * 
 * @author Daniel Tamm
 *
 */
public interface MappingServiceInstanceFactory {

	public static final String  ANY_SOURCE_TARGET = "any";
	
	public static final String	TYPEOFOBJECT_CUSTOMER_ID = "customerId";
	
	public String getSourceSystemName();
	public String getTargetSystemName();

	public MappingService getMappingService(TaxSubjectIdentifier tenant) throws NoSuchTenantException;
	
}
