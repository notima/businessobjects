package org.notima.generic.businessobjects.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.util.NumberUtils;


public class TestNumberUtils {

	@Before
	public void setUp() throws Exception {
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test() {
		
		double roundMe = 10000.100291;
		double rounded = NumberUtils.roundToPrecision(roundMe, 2);
		assertEquals(10000.1, rounded, 0);
		
	}

}
