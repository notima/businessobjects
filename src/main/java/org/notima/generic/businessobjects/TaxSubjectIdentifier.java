package org.notima.generic.businessobjects;

import java.time.LocalDate;
import java.util.Set;

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

	public enum TaxIdType {
		COMPANY_REGISTER,				// A company registrar identification id
		VAT_NO,							// An id for VAT purposes.
		EMPLOYER_ID,					// An ID for registration of employer.
		TAX_ID,							// ID for the tax authorities register.
		CUSTOMS_ID						// ID for import / export at customs authorities.
	}
	
	private String	taxId;
	private String	countryCode;
	private String	state;
	private Set<TaxIdType>	taxIdTypes;
	private	boolean	physicalPerson = false;
	private String	legalName;

	
	/**
	 * Expands a Swedish personal number (personnummer/samordningsnummer) given in
	 * the common 10-digit form {@code YYMMDD-NNNN} to the 12-digit form
	 * {@code YYYYMMDDNNNN} that authorities such as Skatteverket require.
	 * <p>
	 * The two-digit year is ambiguous between two centuries; this picks whichever
	 * keeps the birth date on or before {@code referenceDate} and no more than 100
	 * years before it, i.e. it assumes a person is rarely older than 100 years (and
	 * never assumes a birth date in the future). A value already in 12-digit form
	 * is returned unchanged (digits only, any hyphen/plus stripped).
	 *
	 * @throws IllegalArgumentException if, once non-digit characters are stripped,
	 *         {@code taxId} is neither 10 nor 12 digits
	 */
	public static String toTwelveDigitSwedishPersonalNumber(String taxId, LocalDate referenceDate) {
		if (taxId == null) {
			throw new IllegalArgumentException("taxId is null");
		}
		String digits = taxId.replaceAll("[^0-9]", "");
		if (digits.length() == 12) {
			return digits;
		}
		if (digits.length() != 10) {
			throw new IllegalArgumentException("Not a 10- or 12-digit Swedish personal number: " + taxId);
		}
		int twoDigitYear = Integer.parseInt(digits.substring(0, 2));
		int referenceYear = referenceDate.getYear();
		int currentCentury = (referenceYear / 100) * 100;
		int fullYear = currentCentury + twoDigitYear;
		if (fullYear > referenceYear) {
			fullYear -= 100;
		}
		return String.format("%04d", fullYear) + digits.substring(2);
	}

	/**
	 * Same as {@link #toTwelveDigitSwedishPersonalNumber(String, LocalDate)} using
	 * today's date as the reference date.
	 */
	public static String toTwelveDigitSwedishPersonalNumber(String taxId) {
		return toTwelveDigitSwedishPersonalNumber(taxId, LocalDate.now());
	}

	public static TaxSubjectIdentifier getUndefinedIdentifier() {
		TaxSubjectIdentifier identifier = new TaxSubjectIdentifier();
		return identifier;
	}
	
	public static TaxSubjectIdentifier createBusinessTaxSubject(String taxId, String countryCode, String legalName) {
		TaxSubjectIdentifier identifier = new TaxSubjectIdentifier(taxId, countryCode);
		identifier.setLegalName(legalName);
		return identifier;
	}
	
	public TaxSubjectIdentifier() {};
	
	public TaxSubjectIdentifier(String taxId) {
		this.taxId = taxId;
	}
	
	public TaxSubjectIdentifier(String taxId, String countryCode) {
		this.taxId = taxId;
		this.countryCode = countryCode;
	}
	
	public TaxSubjectIdentifier(BusinessPartner<?> bp) {
		if (bp!=null) {
			if (bp.hasTaxId()) {
				taxId = bp.getTaxId();
			}
			countryCode = bp.getCountryCode();
			legalName = bp.getName();
		}
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
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set<TaxIdType> getTaxIdTypes() {
		return taxIdTypes;
	}

	public void setTaxIdTypes(Set<TaxIdType> taxIdTypes) {
		this.taxIdTypes = taxIdTypes;
	}
	
	public boolean isPhysicalPerson() {
		return physicalPerson;
	}

	public void setPhysicalPerson(boolean physicalPerson) {
		this.physicalPerson = physicalPerson;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public boolean hasName() {
		return legalName!=null && legalName.trim().length()>0;
	}
	
	public boolean isEqual(TaxSubjectIdentifier o) {
		if (o==null) return false;
		return o.compareTo(this) == 0;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (hasTaxId()) {
			buf.append(getTaxId());
		}
		if (hasCountryCode()) {
			if (buf.length()>0) {
				buf.append(" ");
			}
			buf.append(getCountryCode());
		}
		if (hasName()) {
			if (buf.length()>0) {
				buf.append(" ");
			}
			buf.append(getLegalName());
		}
		return buf.toString();
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
