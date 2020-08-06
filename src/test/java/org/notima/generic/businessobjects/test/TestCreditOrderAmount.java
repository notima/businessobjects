package org.notima.generic.businessobjects.test;

import java.io.File;
import java.io.PrintWriter;

import javax.xml.bind.JAXB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.BasicBusinessObjectConverter;
import org.notima.generic.businessobjects.Order;

public class TestCreditOrderAmount {

	private File orderExampleFile;
	
	@Before
	public void setUp() throws Exception {
		orderExampleFile = TestSettings.getOrderExampleFile(1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	@Test
	public void test() throws Exception {
		
		Order order = JAXB.unmarshal(orderExampleFile, Order.class);

		BasicBusinessObjectConverter bbc = new BasicBusinessObjectConverter();
		
		Order creditOrder = bbc.createCreditOrderFromAmount(order, 122.20);
		
		PrintWriter wr = new PrintWriter(System.out);
		
		JAXB.marshal(creditOrder, wr);
		
		wr.close();
		
		// fail("Not yet implemented");
	}

}
