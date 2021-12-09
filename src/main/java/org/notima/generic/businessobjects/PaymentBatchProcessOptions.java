package org.notima.generic.businessobjects;

public class PaymentBatchProcessOptions {

	// Property to say that only this account / payment type should be processed
	private String onlyAccountType;
	// Property to say that only this account reference should be processed
	private String onlyAccountReference;
	// Property to say that only this transaction should be processed
	private boolean onlyTrxNumber;
	// Only account fees should be accounted (nothing else)
	private boolean accountFeesOnly;
	// Only payout should be accounted (nothing else)
	private boolean	accountPayoutOnly;
	// The file should be kept after processing (not moved)
	private boolean keepFile;
	private String	outputDirectory;
	private String	outputFilePrefix;
	// Property for debugging org no (output is to this organisation)
	private TaxSubjectIdentifier	debugOrgNo;
	private boolean dryRun;
	
	// Don't complete payments (only draft) if the system allows that
	private boolean	draftPaymentsIfPossible;
	
	public boolean isDryRun() {
		return dryRun;
	}
	public void setDryRun(boolean dryRun) {
		this.dryRun = dryRun;
	}
	public boolean isDraftPaymentsIfPossible() {
		return draftPaymentsIfPossible;
	}
	public void setDraftPaymentsIfPossible(boolean draftPaymentsIfPossible) {
		this.draftPaymentsIfPossible = draftPaymentsIfPossible;
	}
	public String getOnlyAccountType() {
		return onlyAccountType;
	}
	public void setOnlyAccountType(String onlyAccountType) {
		this.onlyAccountType = onlyAccountType;
	}
	public String getOnlyAccountReference() {
		return onlyAccountReference;
	}
	public void setOnlyAccountReference(String onlyAccountReference) {
		this.onlyAccountReference = onlyAccountReference;
	}
	public boolean isOnlyTrxNumber() {
		return onlyTrxNumber;
	}
	public void setOnlyTrxNumber(boolean onlyTrxNumber) {
		this.onlyTrxNumber = onlyTrxNumber;
	}
	public boolean isAccountFeesOnly() {
		return accountFeesOnly;
	}
	public void setAccountFeesOnly(boolean accountFeesOnly) {
		this.accountFeesOnly = accountFeesOnly;
	}
	public boolean isAccountPayoutOnly() {
		return accountPayoutOnly;
	}
	public void setAccountPayoutOnly(boolean accountPayoutOnly) {
		this.accountPayoutOnly = accountPayoutOnly;
	}
	public boolean isKeepFile() {
		return keepFile;
	}
	public void setKeepFile(boolean keepFile) {
		this.keepFile = keepFile;
	}
	public String getOutputDirectory() {
		return outputDirectory;
	}
	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	public String getOutputFilePrefix() {
		return outputFilePrefix;
	}
	public void setOutputFilePrefix(String outputFilePrefix) {
		this.outputFilePrefix = outputFilePrefix;
	}
	public TaxSubjectIdentifier getDebugOrgNo() {
		return debugOrgNo;
	}
	public void setDebugOrgNo(TaxSubjectIdentifier debugOrgNo) {
		this.debugOrgNo = debugOrgNo;
	}
	
	
	
}
