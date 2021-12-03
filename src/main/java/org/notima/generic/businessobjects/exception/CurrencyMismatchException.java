package org.notima.generic.businessobjects.exception;

public class CurrencyMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6444470877761500924L;
	private String	expected;
	private String	actual;
	
	public CurrencyMismatchException(String expected, String actual) {
		
		this.expected = expected;
		this.actual = actual;
		
	}
	
	public String toString() {
		return "Expected currency: " + formatCurrency(expected) + ". Actual: " + formatCurrency(actual);
	}
	
	private String formatCurrency(String cur) {
		if (cur==null || cur.trim().length()==0)
			return "<none>";
		else 
			return cur;
	}
	
}
