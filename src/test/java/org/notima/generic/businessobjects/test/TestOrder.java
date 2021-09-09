package org.notima.generic.businessobjects.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.Order;

public class TestOrder {

	private Order<?> validOrder, validOrderWithoutName;
	
	@Before
	public void setUp() throws Exception {
		
		TestSettings testSettings = new TestSettings();
		validOrder = testSettings.getOrderExample(0);
		validOrderWithoutName = testSettings.getOrderExample(3);
		
	}

	@Test
	public void testIsValid() {

		assertTrue(validOrder.isValidOrder());
		assertTrue(validOrderWithoutName.isValidOrder());
		
	}

}
