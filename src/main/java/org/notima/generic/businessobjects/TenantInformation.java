package org.notima.generic.businessobjects;

/**
 * Persistent information about a specific tenant, keyed by TaxSubjectIdentifier.
 */
public class TenantInformation {

	private TaxSubjectIdentifier tenant;
	private String defaultOutputDirectory;
	private String reportDirectory;

	// Payment information, used when the tenant's adapter can't supply it
	private String remitToAccount;
	private String remitToAccountType;
	private String remitToIBAN;
	private String remitToBIC;

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

	/**
	 * @return	The account to pay to, ie a bankgiro number. Can be null.
	 */
	public String getRemitToAccount() {
		return remitToAccount;
	}

	public void setRemitToAccount(String remitToAccount) {
		this.remitToAccount = remitToAccount;
	}

	/**
	 * @return	The type of the remit to account, ie BG (bankgiro) or PG (plusgiro). Can be null.
	 */
	public String getRemitToAccountType() {
		return remitToAccountType;
	}

	public void setRemitToAccountType(String remitToAccountType) {
		this.remitToAccountType = remitToAccountType;
	}

	public String getRemitToIBAN() {
		return remitToIBAN;
	}

	public void setRemitToIBAN(String remitToIBAN) {
		this.remitToIBAN = remitToIBAN;
	}

	public String getRemitToBIC() {
		return remitToBIC;
	}

	public void setRemitToBIC(String remitToBIC) {
		this.remitToBIC = remitToBIC;
	}

	/**
	 * @return	True if a remit to account or IBAN is set.
	 */
	public boolean hasPaymentInformation() {
		return (remitToAccount != null && remitToAccount.trim().length() > 0)
				|| (remitToIBAN != null && remitToIBAN.trim().length() > 0);
	}

	/**
	 * Copies the payment information to a business partner, ie the creditor of an invoice.
	 * 
	 * @param bp	The business partner to set payment information on.
	 */
	public void copyPaymentInformationTo(BusinessPartner<?> bp) {
		if (bp == null) return;
		bp.setRemitToAccount(remitToAccount);
		bp.setRemitToAccountType(remitToAccountType);
		bp.setRemitToIBAN(remitToIBAN);
		bp.setRemitToBIC(remitToBIC);
	}

}
