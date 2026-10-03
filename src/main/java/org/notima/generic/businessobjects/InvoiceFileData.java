package org.notima.generic.businessobjects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Normalised result of reading invoices from a file using an
 * {@link org.notima.generic.ifacebusinessobjects.InvoiceFileImporter}.
 * <p>
 * Each invoice carries both its original amount ({@link Invoice#getGrandTotal()})
 * and its remaining balance ({@link Invoice#getOpenAmt()}) as of {@link #getAsOfDate()},
 * and whether it's a customer or vendor invoice ({@link Invoice#isSalesTransaction()}).
 */
public class InvoiceFileData {

	/** Format name of the source, e.g. {@code "Fortnox AP report"}. */
	private String				sourceFormat;
	/** The date the open amounts are valid for, or null if unknown. */
	private LocalDate			asOfDate;
	/** True if the source only lists invoices that are unpaid as of {@link #asOfDate}. */
	private boolean				openItemsOnly;
	/** The company the invoices belong to, or null if unknown. */
	private BusinessPartner<?>	company;
	private List<Invoice<?>>	invoices = new ArrayList<Invoice<?>>();

	public String getSourceFormat() {
		return sourceFormat;
	}

	public void setSourceFormat(String sourceFormat) {
		this.sourceFormat = sourceFormat;
	}

	public LocalDate getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(LocalDate asOfDate) {
		this.asOfDate = asOfDate;
	}

	public boolean isOpenItemsOnly() {
		return openItemsOnly;
	}

	public void setOpenItemsOnly(boolean openItemsOnly) {
		this.openItemsOnly = openItemsOnly;
	}

	public BusinessPartner<?> getCompany() {
		return company;
	}

	public void setCompany(BusinessPartner<?> company) {
		this.company = company;
	}

	public List<Invoice<?>> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice<?>> invoices) {
		this.invoices = invoices != null ? invoices : new ArrayList<Invoice<?>>();
	}

}
