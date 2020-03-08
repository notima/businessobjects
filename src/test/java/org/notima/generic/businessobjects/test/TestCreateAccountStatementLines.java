package org.notima.generic.businessobjects.test;

import java.time.LocalDate;

import javax.xml.bind.JAXB;

import org.junit.Test;
import org.notima.generic.businessobjects.AccountStatementLines;

public class TestCreateAccountStatementLines {

	@Test
	public void testCreateAccountStatementLines() {

		LocalDate from = java.time.LocalDate.of(2018, 1, 1);
		LocalDate until = LocalDate.of(2018, 12, 31);
		
		AccountStatementLines lines = new AccountStatementLines();
		lines.setStartDate(from);
		lines.setEndDate(until);
		
		lines.setStartBalance(100);
		lines.addTransaction(from, 200);
		lines.addTransaction(from, 500);

		JAXB.marshal(lines, System.out);
		
	}		

}
