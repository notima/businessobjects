package org.notima.generic.businessobjects;

import java.time.LocalDate;

public class OrderInvoiceWriterOptions {

	private LocalDate	invoiceDate;
	private LocalDate	orderDate;
	private LocalDate	dueDate;
	private int			createLimit;
	private boolean		mergeOnBusinessPartner;
	private boolean		mapOnTaxId;
	private boolean		mapOnAddressFirst;
	private boolean		updateExisting = true;
	private boolean		createBusinessPartner;
	
	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public int getCreateLimit() {
		return createLimit;
	}
	public void setCreateLimit(int createLimit) {
		this.createLimit = createLimit;
	}
	public boolean isMergeOnBusinessPartner() {
		return mergeOnBusinessPartner;
	}
	public void setMergeOnBusinessPartner(boolean mergeOnBusinessPartner) {
		this.mergeOnBusinessPartner = mergeOnBusinessPartner;
	}
	public boolean isMapOnTaxId() {
		return mapOnTaxId;
	}
	public void setMapOnTaxId(boolean mapOnTaxId) {
		this.mapOnTaxId = mapOnTaxId;
	}
	public boolean isMapOnAddressFirst() {
		return mapOnAddressFirst;
	}
	public void setMapOnAddressFirst(boolean mapOnAddressFirst) {
		this.mapOnAddressFirst = mapOnAddressFirst;
	}
	public boolean isUpdateExisting() {
		return updateExisting;
	}
	public void setUpdateExisting(boolean updateExisting) {
		this.updateExisting = updateExisting;
	}
	public boolean isCreateBusinessPartner() {
		return createBusinessPartner;
	}
	public void setCreateBusinessPartner(boolean createBusinessPartner) {
		this.createBusinessPartner = createBusinessPartner;
	}
	
}
