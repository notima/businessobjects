package org.notima.generic.businessobjects.test;

import java.io.PrintWriter;

import javax.xml.bind.JAXB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.BasicBusinessObjectConverter;
import org.notima.generic.businessobjects.Order;

public class TestCreditOrderAmount {

	private Order<?> order;
	
	@Before
	public void setUp() throws Exception {
		TestSettings testSettings = new TestSettings();
		order = testSettings.getOrderExample(1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	@Test
	public void test() throws Exception {

		BasicBusinessObjectConverter bbc = new BasicBusinessObjectConverter();
		
		Order creditOrder = bbc.createCreditOrderFromAmount(order, 122.20);
		
		PrintWriter wr = new PrintWriter(System.out);
		
		JAXB.marshal(creditOrder, wr);
		
		wr.close();
		
		// fail("Not yet implemented");
	}

}
