package org.notima.generic.ifacebusinessobjects;

/**
 * A service that help mapping one type of entity ID to another
 * 
 * @author Daniel Tamm
 *
 */
public interface MappingServiceInstanceFactory {

	public static final String	TYPEOFOBJECT_CUSTOMER_ID = "customerId";
	
	public String getSourceSystemName();
	public String getTargetSystemName();

	public MappingService getMappingService();
	
}
