package org.notima.generic.businessobjects;

import java.util.Properties;

public class PaymentBatchChannelOptions {

	private Properties 	destinationProperties;
	private Properties	sourceProperties;
	
	public Properties getDestinationProperties() {
		return destinationProperties;
	}
	public void setDestinationProperties(Properties destinationProperties) {
		this.destinationProperties = destinationProperties;
	}
	public Properties getSourceProperties() {
		return sourceProperties;
	}
	public void setSourceProperties(Properties sourceProperties) {
		this.sourceProperties = sourceProperties;
	}
	
}
