package org.notima.generic.businessobjects;

/**
 * Persistent information about a specific tenant, keyed by TaxSubjectIdentifier.
 */
public class TenantInformation {

	private TaxSubjectIdentifier tenant;
	private String defaultOutputDirectory;
	private String reportDirectory;

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

	/**
	 * @return	The directory for reports, ie payment channel match reports. Can be null.
	 * @see #getEffectiveReportDirectory()
	 */
	public String getReportDirectory() {
		return reportDirectory;
	}

	public void setReportDirectory(String reportDirectory) {
		this.reportDirectory = reportDirectory;
	}

	/**
	 * @return	The report directory if set, otherwise the default output directory. Null if neither is set.
	 */
	public String getEffectiveReportDirectory() {
		if (reportDirectory != null && reportDirectory.trim().length() > 0) {
			return reportDirectory.trim();
		}
		if (defaultOutputDirectory != null && defaultOutputDirectory.trim().length() > 0) {
			return defaultOutputDirectory.trim();
		}
		return null;
	}

}
