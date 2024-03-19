package org.notima.generic.businessobjects.exception;

public class TaxRatesNotAvailableException extends Exception {

	private static final long serialVersionUID = 5946133521096809254L;

	public TaxRatesNotAvailableException(String countryCode) {
		super(countryCode);
	}
	
}
