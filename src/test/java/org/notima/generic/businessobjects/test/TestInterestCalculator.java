package org.notima.generic.businessobjects.test;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;
import org.notima.generic.businessobjects.util.InterestCalculator;

public class TestInterestCalculator {

	@Test
	public void testCalculateFixedRate() {
		
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.YEAR, 2018);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH,1);
		java.util.Date from = cal.getTime();
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		java.util.Date until = cal.getTime();
		
		assertEquals(1000D, InterestCalculator.calculateFixedRate(from, until, 10000, 0.10, 0), 5.0);
		
	}

	@Test
	public void testRoundToDecimals() {

		assertEquals(10000.10, InterestCalculator.roundToDecimals(10000.103, 2), 0.0);
		
	}

}
