package org.notima.generic.businessobjects.test.samples;

import java.util.ArrayList;
import java.util.List;

import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.InvoiceLine;
import org.notima.generic.businessobjects.InvoiceList;

public class InvoiceListTestFactory {
	
	private InvoiceList	invoiceList;
	
	public InvoiceList buildInvoiceList() {
		
		invoiceList = new InvoiceList();
		List<Invoice<?>> invoices = new ArrayList<Invoice<?>>();
		invoiceList.setInvoiceList(invoices);

		invoices.add(createInvoiceOne());
		invoices.add(createInvoiceTwo());
		invoices.add(createInvoiceThree());
		
		return invoiceList;
		
	}
	
	private Invoice<?> createInvoiceOne() {
		
		Invoice<?> invoice = new Invoice<Object>();
		invoice.setBusinessPartner(BPartnerTestFactory.buildTestBusinessPartnerNameAndIdentityNo());
		invoice.addInvoiceLine(createLine());
		return invoice;
		
	}
	
	private Invoice<?> createInvoiceTwo() {
		
		Invoice<?> invoice = new Invoice<Object>();
		invoice.setBusinessPartner(BPartnerTestFactory.buildTestBusinessPartnerSweden());
		invoice.addInvoiceLine(createLine1());
		return invoice;
		
	}
	
	private Invoice<?> createInvoiceThree() {
		
		Invoice<?> invoice = new Invoice<Object>();
		invoice.setBusinessPartner(BPartnerTestFactory.buildTestBusinessPartnerSweden());
		invoice.addInvoiceLine(createLine());
		return invoice;
		
	}
	
	
	private InvoiceLine createLine() {
		InvoiceLine il = new InvoiceLine();
		il.setKey("115");
		il.setName("Some description");
		il.setQtyEntered(2);
		il.setPriceActual(100.0);
		il.setTaxPercent(25);
		return il;
		
	}
	
	private InvoiceLine createLine1() {
		InvoiceLine il = new InvoiceLine();
		il.setKey("215");
		il.setName("Another description");
		il.setQtyEntered(2.5);
		il.setPriceActual(110.0);
		il.setTaxPercent(12);
		return il;
		
	}
	
	
	
}
