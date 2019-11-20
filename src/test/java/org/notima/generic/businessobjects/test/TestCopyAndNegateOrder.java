package org.notima.generic.businessobjects.test;

import java.io.File;
import java.io.PrintWriter;

import javax.xml.bind.JAXB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.BasicBusinessObjectConverter;
import org.notima.generic.businessobjects.Order;

public class TestCopyAndNegateOrder {

	private File orderExampleFile;
	
	@Before
	public void setUp() throws Exception {
		orderExampleFile = TestSettings.getOrderExampleFile(0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	@Test
	public void test() {
		
		Order<?> order = JAXB.unmarshal(orderExampleFile, Order.class);

		BasicBusinessObjectConverter bbc = new BasicBusinessObjectConverter();
		
		Order<?> copy = bbc.copyOrder(order);
		
		bbc.negateOrder(copy);
		
		PrintWriter wr = new PrintWriter(System.out);
		
		JAXB.marshal(copy, wr);
		
		wr.close();
		
		// fail("Not yet implemented");
	}

}
