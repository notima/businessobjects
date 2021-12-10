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
public class TaxSubjectIdentifier implements Comparable<TaxSubjectIdentifier> {

	public static TaxSubjectIdentifier getUndefinedIdentifier() {
		TaxSubjectIdentifier identifier = new TaxSubjectIdentifier();
		return identifier;
	}
	
	private String	taxId;
	private String	countryCode;
	
	public TaxSubjectIdentifier() {};
	
	public TaxSubjectIdentifier(String taxId, String countryCode) {
		this.taxId = taxId;
		this.countryCode = countryCode;
	}
	
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
	
	public boolean hasTaxId() {
		return taxId!=null && taxId.trim().length()>0;
	}
	
	public boolean hasCountryCode() {
		return countryCode!=null && countryCode.trim().length()>0;
	}
	
	public boolean isUndefined() {
		return (!hasTaxId());
	}

	
	public boolean isEqual(TaxSubjectIdentifier o) {
		if (o==null) return false;
		return o.compareTo(this) == 0;
	}
	
	@Override
	public int compareTo(TaxSubjectIdentifier o) {
		boolean sameCountry = 
				(!o.hasCountryCode() && !this.hasCountryCode()) ||
				(o.hasCountryCode() && o.getCountryCode().equalsIgnoreCase(this.getCountryCode()));

		if (sameCountry && o.hasTaxId() && o.getTaxId().equalsIgnoreCase(this.getTaxId())) {
			return 0;
		}

		if (sameCountry && !this.hasTaxId() && !o.hasTaxId()) {
			return 0;
		}
		
		if (this.hasTaxId() && !o.hasTaxId()) {
			return -1;
		}
		if (o.hasTaxId() && !this.hasTaxId()) {
			return 1;
		}
		if (sameCountry) {
			return this.getTaxId().compareTo(o.getTaxId());
		} else {
			if (this.hasCountryCode() && !o.hasCountryCode()) {
				return -1;
			}
			if (o.hasCountryCode() && !this.hasCountryCode()) {
				return 1;
			}
			return this.getCountryCode().compareTo(o.getCountryCode());
		}

	}
	
}
