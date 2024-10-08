package org.notima.generic.businessobjects.test;

import java.io.PrintWriter;

import javax.xml.bind.JAXB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.BasicBusinessObjectConverter;
import org.notima.generic.businessobjects.Order;

public class TestCopyAndNegateOrder {

	private Order<?> orderExample;
	
	@Before
	public void setUp() throws Exception {
		TestSettings testSettings = new TestSettings();
		orderExample = testSettings.getOrderExample(0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	@Test
	public void test() {
		
		BasicBusinessObjectConverter bbc = new BasicBusinessObjectConverter();
		
		Order<?> copy = bbc.copyOrder(orderExample);
		
		bbc.negateOrder(copy);
		
		PrintWriter wr = new PrintWriter(System.out);
		
		JAXB.marshal(copy, wr);
		
		wr.close();
		
		// fail("Not yet implemented");
	}

}
