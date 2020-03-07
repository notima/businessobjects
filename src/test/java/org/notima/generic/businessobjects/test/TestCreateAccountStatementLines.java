package org.notima.generic.businessobjects.test;

import java.util.Calendar;

import javax.xml.bind.JAXB;

import org.junit.Test;
import org.notima.generic.businessobjects.AccountStatementLines;

public class TestCreateAccountStatementLines {

	@Test
	public void testCreateAccountStatementLines() {
		
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.YEAR, 2018);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH,1);
		java.util.Date from = cal.getTime();
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		java.util.Date until = cal.getTime();
		
		AccountStatementLines lines = new AccountStatementLines();
		lines.setStartDate(from);
		lines.setEndDate(until);
		
		lines.setStartBalance(100);
		lines.addTransaction(from, 200);
		lines.addTransaction(from, 500);

		JAXB.marshal(lines, System.out);
		
	}		

}
