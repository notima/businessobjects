package org.notima.generic.businessobjects.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.InvoiceLine;
import org.notima.generic.businessobjects.InvoiceList;

/**
 * Class that merges a list of invoices.
 */
public class InvoiceListMerger {

	private InvoiceList 	list;
	private Map<BusinessPartner<?>, List<Invoice<?>>> bpInvoiceMap;
	
	public InvoiceListMerger(InvoiceList list) {
		this.list = list;
	}

	public InvoiceList getList() {
		return list;
	}

	public void setList(InvoiceList list) {
		this.list = list;
	}

	public void mergeListByBusinessPartner() {
		
		createBpInvoiceMap();
		
		Invoice<?> mergedInvoice;
		List<Invoice<?>> newList = new ArrayList<Invoice<?>>();
		
		for (BusinessPartner<?> bp : bpInvoiceMap.keySet()) {
			mergedInvoice = mergeIntoOneBasedOnBp(bp);
			newList.add(mergedInvoice);
		}
		
		bpInvoiceMap.clear();
		
     	// Update the invoice list with the new merged invoices.
		list.setInvoiceList(newList);
		
		// Update the invoice map from the new list.
		createBpInvoiceMap();
		
	}
	
	private Invoice<?> mergeIntoOneBasedOnBp(BusinessPartner<?> bp) {

		Invoice<?> result = null;
		List<Invoice<?>> bpList = bpInvoiceMap.get(bp);
		if (bpList==null || bpList.size()==0) {
			result = new Invoice<Object>();
			result.setBusinessPartner(bp);
		} else {

			result = bpList.get(0);
			if (bpList.size()>1) {
				for (int i=1 ; i<bpList.size(); i++) {
					addInvoiceLinesFromTo(bpList.get(i), result);
				}
			}
			
		}
		
		return result;
		
	}
	
	private void addInvoiceLinesFromTo(Invoice<?> from, Invoice<?> to) {
		if (from==null) return;
		if (to==null) return;
		
		if (from.getLines()==null || from.getLines().size()==0) return;
		
		for (InvoiceLine il : from.getLines()) {
			to.addInvoiceLine(il);
		}
		to.calculateGrandTotal();
	}
	
	private void createBpInvoiceMap() {
		
		bpInvoiceMap = new TreeMap<BusinessPartner<?>,List<Invoice<?>>>();
		if (list==null) return;
		
		List<Invoice<?>> invoiceByBp;
		List<Invoice<?>> noBpList;
		
		BusinessPartner<?> businessPartner;
		noBpList = new ArrayList<Invoice<?>>();
		
		for (Invoice<?> invoice : list.getInvoiceList()) {

			businessPartner = invoice.getBusinessPartner();

			if (businessPartner==null) {
				noBpList.add(invoice);
				continue;
			}
			
			invoiceByBp = bpInvoiceMap.get(businessPartner);
			if (invoiceByBp==null) {
				invoiceByBp = new ArrayList<Invoice<?>>();
				bpInvoiceMap.put(businessPartner, invoiceByBp);
			}
			invoiceByBp.add(invoice);
			
		}
		
		
	}
	
	
}
