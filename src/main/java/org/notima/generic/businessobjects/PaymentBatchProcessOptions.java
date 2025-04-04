package org.notima.generic.businessobjects;

import java.util.Map;
import java.util.TreeMap;

public class PaymentBatchProcessOptions {

	// Property to say that only this account / payment type should be processed
	private String onlyAccountType;
	// Property to say that only this account reference should be processed
	private String onlyAccountReference;
	// Property to say that only this transaction should be processed
	private boolean onlyTrxNumber;
	// Only account fees should be accounted (nothing else)
	private boolean accountFeesOnly;
	// Account fees on each payment if possible. If false (default), fees are lumped together
	private boolean feesPerPayment = false;
	// Account non matched payments on pre payment account
	private boolean nonMatchedAsPrepayments = false;
	// Only payout should be accounted (nothing else)
	private boolean	accountPayoutOnly;
	// The file should be kept after processing (not moved)
	private boolean keepFile;
	private String	outputDirectory;
	private String	outputFilePrefix;
	// Property for debugging org no (output is to this organisation)
	private TaxSubjectIdentifier	debugOrgNo;
	// Manual reference map that should override any other mappings.
	private Map<String,String>		manualReferenceMap;
	
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
	
	public boolean isFeesPerPayment() {
		return feesPerPayment;
	}
	public void setFeesPerPayment(boolean feesPerPayment) {
		this.feesPerPayment = feesPerPayment;
	}
	
	public boolean isNonMatchedAsPrepayments() {
		return nonMatchedAsPrepayments;
	}
	public void setNonMatchedAsPrepayments(boolean nonMatchedAsPrepayments) {
		this.nonMatchedAsPrepayments = nonMatchedAsPrepayments;
	}
	
	public boolean hasManualReferenceMap() {
		return manualReferenceMap!=null && !manualReferenceMap.isEmpty();
	}

	/**
	 * Returns a reference if there's one.
	 * 
	 * @param sourceRef
	 * @return	Destination reference (if existing). Otherwise null.
	 */
	public String getManualReferenceFor(String sourceRef) {
		if (sourceRef==null) return null;
		if (!hasManualReferenceMap()) return null;
		return manualReferenceMap.get(sourceRef);
	}
	
	/**
	 * The reference map is a comma separated list of tuples (where = is the mapper)
	 * 
	 * For instance 1234=445,1232=432 etc
	 * 
	 * @param list
	 */
	public void addManualReferenceMapFromCommaList(String csvlist) {
		
		String[] items = csvlist.split(",");
		
		for (String item : items) {
			addManualReferenceFromTupleString(item);
		}
		
	}
	
	private void addManualReferenceFromTupleString(String tuple) {
		
		if (tuple==null) return;
		int pos = tuple.indexOf("=");
		
		String left = tuple.substring(0, pos);
		String right = tuple.substring(pos+1);
		
		if (left.trim().length()>0) {
			left = left.trim();
		} else 
			return; // Cant add without key
		
		if (right.trim().length()>0) {
			addManualReference(left, right);
		}
		
	}
	
	/**
	 * Adds a manual reference to the options.
	 * 
	 * @param key
	 * @param value
	 */
	public void addManualReference(String key, String value) {

		if (key!=null && key.trim().length()>0 && value!=null && value.trim().length()>0) {
			if (manualReferenceMap==null) {
				manualReferenceMap = new TreeMap<String,String>();
			}
			manualReferenceMap.put(key, value);
			
		}
		
	}
	
	
	
}
