package org.notima.generic.businessobjects;

/**
 * Use this class to unambigously identify a tax subject. 
 * Normally a country code and that's countries tax identifier is what is needed.
 * 
 * Use ISO-country codes.
 * 
 * @author Daniel Tamm
 *
 */
public class TaxSubjectIdentifier {

	private String	taxId;
	private String	countryCode;
	
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
}
