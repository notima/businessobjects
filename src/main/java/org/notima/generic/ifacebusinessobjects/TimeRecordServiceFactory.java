package org.notima.generic.ifacebusinessobjects;

import org.notima.generic.businessobjects.TaxSubjectIdentifier;

/**
 * Factory for getting time record services.
 */
public interface TimeRecordServiceFactory {

	public TimeRecordService getTimeRecordServiceFor(String system, TaxSubjectIdentifier tenant);
	
	public String getSystemName();
	
}
