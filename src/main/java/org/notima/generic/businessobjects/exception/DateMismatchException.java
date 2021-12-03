package org.notima.generic.businessobjects.exception;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.notima.util.LocalDateUtils;

public class DateMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5988771816725527578L;

	private DateFormat	sDate = new SimpleDateFormat("yyyy-MM-dd");
	
	private LocalDate 	expected;
	private LocalDate	actual;
	
	public DateMismatchException(LocalDate expected, LocalDate actual) {
		this.expected = expected;
		this.actual = actual;
	}
	
	public DateMismatchException(Date expectedDate, Date actualDate) {
		this.expected = LocalDateUtils.asLocalDate(expectedDate);
		this.actual = LocalDateUtils.asLocalDate(actualDate);
	}
	
	public String toString() {
		return "Expected date: " + formatDate(expected) + " Actual: " + formatDate(actual);
	}

	private String formatDate(LocalDate d) {
		if (d==null) return "<none>";
		return sDate.format(LocalDateUtils.asDate(d));
	}
	
}
