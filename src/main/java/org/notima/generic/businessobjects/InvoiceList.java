package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invoiceList")
public class InvoiceList {

	private List<Invoice<?>> invoiceList = new ArrayList<Invoice<?>>();

	@XmlElement(name = "invoice")
	public List<Invoice<?>> getInvoiceList() {
		return invoiceList;
	}

	
	public void setInvoiceList(List<Invoice<?>> invoiceList) {
		this.invoiceList = invoiceList;
	}
	
}
