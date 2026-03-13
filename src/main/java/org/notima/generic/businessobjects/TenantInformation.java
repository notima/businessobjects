package org.notima.generic.businessobjects;

/**
 * Persistent information about a specific tenant, keyed by TaxSubjectIdentifier.
 */
public class TenantInformation {

	private TaxSubjectIdentifier tenant;
	private String defaultOutputDirectory;

	public TaxSubjectIdentifier getTenant() {
		return tenant;
	}

	public void setTenant(TaxSubjectIdentifier tenant) {
		this.tenant = tenant;
	}

	public String getDefaultOutputDirectory() {
		return defaultOutputDirectory;
	}

	public void setDefaultOutputDirectory(String defaultOutputDirectory) {
		this.defaultOutputDirectory = defaultOutputDirectory;
	}

}
