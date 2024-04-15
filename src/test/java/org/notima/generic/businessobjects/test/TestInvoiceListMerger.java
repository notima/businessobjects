package org.notima.generic.businessobjects.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.InvoiceList;
import org.notima.generic.businessobjects.test.samples.InvoiceListTestFactory;
import org.notima.generic.businessobjects.tools.InvoiceListMerger;

public class TestInvoiceListMerger {
	
	private InvoiceList		invoiceList;
	private InvoiceListTestFactory	factory;
	
	@Before
	public void setUp() throws Exception {
		
		factory = new InvoiceListTestFactory();
		
		invoiceList = factory.buildInvoiceList();
		
	}

	@Test
	public void testMerge() {
		
		InvoiceListMerger merger = new InvoiceListMerger(invoiceList);
		
		merger.mergeListByBusinessPartner();
		
		assertTrue(merger.getList().getInvoiceList().size()==2);
		
	}
	

}
