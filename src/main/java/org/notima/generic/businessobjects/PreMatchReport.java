package org.notima.generic.businessobjects;


/**
 * A struct used to get a preview on how many invoices must be matched against and the estimated time for that.
 * 
 */
public class PreMatchReport {

	private String 	systemName;
	private TaxSubjectIdentifier	tenant;	
	private int		openInvoiceCount;
	private int		milliSecondsPerInvoice;
	
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public TaxSubjectIdentifier getTenant() {
		return tenant;
	}
	public void setTenant(TaxSubjectIdentifier tenant) {
		this.tenant = tenant;
	}
	public int getOpenInvoiceCount() {
		return openInvoiceCount;
	}
	public void setOpenInvoiceCount(int openInvoiceCount) {
		this.openInvoiceCount = openInvoiceCount;
	}
	public int getMilliSecondsPerInvoice() {
		return milliSecondsPerInvoice;
	}
	public void setMilliSecondsPerInvoice(int milliSecondsPerInvoice) {
		this.milliSecondsPerInvoice = milliSecondsPerInvoice;
	}	
	
	public long getEstimatedTimeInSeconds() {
		return Math.round(openInvoiceCount * milliSecondsPerInvoice / 1000);
	}
	
}
