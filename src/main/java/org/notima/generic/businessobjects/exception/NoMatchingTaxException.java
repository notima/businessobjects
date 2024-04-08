package org.notima.generic.businessobjects.exception;

public class NoMatchingTaxException extends Exception {

	private static final long serialVersionUID = 3743735224823027005L;


	public NoMatchingTaxException(double taxRate) {
		super("Rate: " + taxRate);
	}
	
	public NoMatchingTaxException(double taxRate, String taxDomicile) {
		super("Rate: " + taxRate + " Domicile: " + taxDomicile);
	}
	
}
