package org.notima.generic.businessobjects;

import java.util.Properties;

public class PaymentBatchChannelOptions {

	public static final String DIRECTORY = "directory";
	public static final String FILE_FILTER = "file-filter";
	
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

	public boolean hasDestinationProperties() {
		return destinationProperties!=null && !destinationProperties.isEmpty();
	}
	
	public boolean hasSourceProperties() {
		return sourceProperties!=null && !sourceProperties.isEmpty();
	}
	
	public String getSourceDirectory() {
		if (!hasSourceProperties()) return null;
		
		return sourceProperties.getProperty(DIRECTORY);
	}
	
	public void setSourceDirectory(String directory) {
		setSourceProperty(DIRECTORY, directory);
	}
	
	public String getSourceFileFilter() {
		if (!hasSourceProperties()) return null;
		
		return sourceProperties.getProperty(FILE_FILTER);
	}
	
	public void setSourceFileFilter(String filter) {
		setSourceProperty(FILE_FILTER, filter);
	}
	
	public void setSourceProperty(String key, String value) {
		if (key==null) return;
		if (sourceProperties==null) {
			sourceProperties = new Properties();
		}
		sourceProperties.setProperty(key, value);
	}
	
}
