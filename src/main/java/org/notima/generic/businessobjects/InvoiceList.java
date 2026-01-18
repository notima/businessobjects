package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invoiceList")
public class InvoiceList {

	private List<Invoice<?>> invoiceList = new ArrayList<Invoice<?>>();

	/**
	 * Creditor is the sender/owner of this invoice list. 
	 */
	private BusinessPartner<?> creditor;
	
	@XmlElement(name = "invoice")
	public List<Invoice<?>> getInvoiceList() {
		return invoiceList;
	}

	
	public void setInvoiceList(List<Invoice<?>> invoiceList) {
		this.invoiceList = invoiceList;
	}

	public BusinessPartner<?> getCreditor() {
		return creditor;
	}


	public void setCreditor(BusinessPartner<?> creditor) {
		this.creditor = creditor;
	}
	
	public void addInvoice(Invoice<?> invoice) {
		invoiceList.add(invoice);
	}
	
}
