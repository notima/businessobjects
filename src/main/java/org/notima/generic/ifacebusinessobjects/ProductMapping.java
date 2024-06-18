package org.notima.generic.ifacebusinessobjects;

/**
 * Maps one product to another (between different systems)
 */
public interface ProductMapping {

	public String	getSourceProductId();
	public String	getSourceName();
	public String	getDestinationProductId();
	public String	getDestinationName();
	
	public String	getSourceSystem();
	public String	getDestinationSystem();
	
}
