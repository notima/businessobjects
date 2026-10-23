package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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

	/** When reminders were last sent (print-dunning-run with --internal-comment). */
	private Date lastDunningDate;

	/** The tenant's suspense accounts per type, ie UNIDENTIFIED: 1799, 2999. */
	private Map<SuspenseAccountType, List<String>> suspenseAccounts;

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
	 * @return	When reminders were last sent, or null if never recorded.
	 */
	public Date getLastDunningDate() {
		return lastDunningDate;
	}

	public void setLastDunningDate(Date lastDunningDate) {
		this.lastDunningDate = lastDunningDate;
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

	/**
	 * @return	The tenant's suspense accounts per type: accounts where amounts are booked
	 * 			temporarily and that should normally be zero. Can be null.
	 */
	public Map<SuspenseAccountType, List<String>> getSuspenseAccounts() {
		return suspenseAccounts;
	}

	public void setSuspenseAccounts(Map<SuspenseAccountType, List<String>> suspenseAccounts) {
		this.suspenseAccounts = suspenseAccounts;
	}

	/** @return	The suspense accounts of {@code type}; empty if none. */
	public List<String> getSuspenseAccounts(SuspenseAccountType type) {
		List<String> accounts = suspenseAccounts != null ? suspenseAccounts.get(type) : null;
		return accounts != null ? accounts : new ArrayList<String>();
	}

	/**
	 * Sets the suspense accounts of {@code type}.
	 *
	 * @param accounts	The account numbers; null or empty to remove the type.
	 */
	public void setSuspenseAccounts(SuspenseAccountType type, List<String> accounts) {
		if (accounts == null || accounts.isEmpty()) {
			if (suspenseAccounts != null) {
				suspenseAccounts.remove(type);
				if (suspenseAccounts.isEmpty()) suspenseAccounts = null;
			}
			return;
		}
		if (suspenseAccounts == null) suspenseAccounts = new EnumMap<SuspenseAccountType, List<String>>(SuspenseAccountType.class);
		suspenseAccounts.put(type, new ArrayList<String>(accounts));
	}

	/** @return	True if the tenant has any suspense accounts. */
	public boolean hasSuspenseAccounts() {
		if (suspenseAccounts == null) return false;
		for (List<String> accounts : suspenseAccounts.values()) {
			if (accounts != null && !accounts.isEmpty()) return true;
		}
		return false;
	}

}
