package org.notima.generic.businessobjects;

import java.time.LocalDate;

/**
 * Class to specify reader options
 */
public class OrderInvoiceReaderOptions {

	private LocalDate	fromDate;
	private LocalDate	untilDate;
	private boolean		unpostedOnly;
	private boolean		openOnly;
	private int			readLimit;
	
	private TaxSubjectIdentifier	bpartner;
	
	private boolean		salesOnly;
	private boolean		vendorOnly;
	
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public LocalDate getUntilDate() {
		return untilDate;
	}
	public void setUntilDate(LocalDate untilDate) {
		this.untilDate = untilDate;
	}
	public boolean isOpenOnly() {
		return openOnly;
	}
	public void setOpenOnly(boolean openOnly) {
		this.openOnly = openOnly;
	}
	public boolean isUnpostedOnly() {
		return unpostedOnly;
	}
	public void setUnpostedOnly(boolean unpostedOnly) {
		this.unpostedOnly = unpostedOnly;
	}
	public TaxSubjectIdentifier getBpartner() {
		return bpartner;
	}
	public void setBpartner(TaxSubjectIdentifier bpartner) {
		this.bpartner = bpartner;
	}
	public boolean isSalesOnly() {
		return salesOnly;
	}
	public void setSalesOnly(boolean salesOnly) {
		this.salesOnly = salesOnly;
	}
	public boolean isVendorOnly() {
		return vendorOnly;
	}
	public void setVendorOnly(boolean vendorOnly) {
		this.vendorOnly = vendorOnly;
	}
	public int getReadLimit() {
		return readLimit;
	}
	public void setReadLimit(int readLimit) {
		this.readLimit = readLimit;
	}
	
	
}
