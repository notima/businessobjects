package org.notima.generic.businessobjects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.notima.util.InvalidTaxIdFormatException;
import org.notima.util.NotimaUtil;
import org.notima.util.TaxIdFormatter;
import org.notima.util.TaxIdStructure;
import org.notima.util.UnknownTaxIdFormatException;

@Entity
@XmlRootElement(name = "BusinessPartner")
public class BusinessPartner<B> {

	@Id
	@GeneratedValue
	private int		bPartnerId;
	private boolean isCompany;
	private String name;
	private String taxId;
	private String	vatNo;
	private String identityNo;
	private String language;
	private boolean emailInvoice;
	@ManyToOne
	private Location	addressOfficial;
	@ManyToOne
	private Location	addressShipping;
	private List<Person>	contacts;
	private List<KeyValue>	attributes;
	private Boolean		isCustomer;
	private Boolean		isVendor;
	private String	countryCode;
	
	private transient B nativeBusinessPartner;
	
	public int getbPartnerId() {
		return bPartnerId;
	}

	public void setbPartnerId(int bPartnerId) {
		this.bPartnerId = bPartnerId;
	}

	public String getLanguage() {
		return language;
	}

	public boolean isCompany() {
		return isCompany;
	}

	public void setCompany(boolean isCompany) {
		this.isCompany = isCompany;
	}

	/**
	 * If name is null, the address record is tried.
	 * 
	 * @return
	 */
	public String getName() {
		if (name!=null)
			return name;
		else {
			if (addressOfficial!=null) {
				String altName = addressOfficial.getName();
				if (altName==null) {
					altName = addressOfficial.getCustomerReference();
				}
				if (altName==null) {
					altName = addressOfficial.getEmail();
				}
				if (altName!=null)
					return altName;
			}
		}
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getTaxId(String format) throws UnknownTaxIdFormatException, InvalidTaxIdFormatException {

		if (format==null) return taxId;
		
		String countryCode = addressOfficial!=null ? addressOfficial.getCountryCode() : null;
		
    	taxId = NotimaUtil.removeBlanks(taxId);
		
		// Check tax id
		String origFormat = TaxIdFormatter.determineFormat(countryCode, taxId);
		
		if (origFormat==null || TaxIdStructure.FMT_UNKNOWN.equals(origFormat)) {
			return taxId;
		}
		
		String result = TaxIdFormatter.printTaxId(origFormat, taxId, format);
		
		return result;
	}
	
	/**
	 * Returns VAT no. If there's a tax id and no VAT-no the tax id is attempted to be converted.
	 * @return
	 */
	public String getVatNo() {
		if (vatNo!=null && vatNo.trim().length()>0)
			return vatNo;
		
		if (taxId==null || taxId.trim().length()==0)
			return null;
		
		String countryCode = addressOfficial!=null ? addressOfficial.getCountryCode() : null;
		if (countryCode==null)
			return null;
		
		// Check tax id
		String format = TaxIdFormatter.determineFormat(countryCode, taxId);
		
		if (format==null || TaxIdStructure.FMT_UNKNOWN.equals(format)) {
			return null;
		}
		
		try {
			vatNo = TaxIdFormatter.printTaxId(format, taxId, TaxIdStructure.FMT_SE14);
		} catch (UnknownTaxIdFormatException | InvalidTaxIdFormatException e) {
			return null;
		}
		
		return vatNo;
	}

	public void setVatNo(String vatNo) {
		this.vatNo = vatNo;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isEmailInvoice() {
		return emailInvoice;
	}

	public void setEmailInvoice(boolean emailInvoice) {
		this.emailInvoice = emailInvoice;
	}

	/**
	 * Use as identifier (for instance bp no in a customer/vendor register)
	 * 
	 * @return
	 */
	public String getIdentityNo() {
		return identityNo;
	}

	public void setIdentityNo(String identityNo) {
		this.identityNo = identityNo;
	}

	public Location getAddressOfficial() {
		return addressOfficial;
	}

	public void setAddressOfficial(Location officialAddress) {
		this.addressOfficial = officialAddress;
	}

	public List<KeyValue> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<KeyValue> attributes) {
		this.attributes = attributes;
	}

	public Location getAddressShipping() {
		return addressShipping;
	}

	public void setAddressShipping(Location addressShipping) {
		this.addressShipping = addressShipping;
	}

	public List<Person> getContacts() {
		return contacts;
	}

	public void setContacts(List<Person> contacts) {
		this.contacts = contacts;
	}

	public B getNativeBusinessPartner() {
		return nativeBusinessPartner;
	}

	public void setNativeBusinessPartner(B nativeBusinessPartner) {
		this.nativeBusinessPartner = nativeBusinessPartner;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		if (bPartnerId>0) {
			result.append("bpId: " + bPartnerId);
		}
		if (taxId!=null) {
			if (result.length()>0) 
				result.append(", ");
			result.append("taxId: " + taxId);
		}
		if (vatNo!=null) {
			if (result.length()>0) 
				result.append(", ");
			result.append("vatNo: " + vatNo);
		}
		if (name!=null) {
			if (result.length()>0) {
				result.append(", ");
			}
			result.append(name);
		}
		result.insert(0, "{");
		result.append("}");
		return result.toString();
	}

	public Boolean getIsCustomer() {
		return isCustomer;
	}

	public void setIsCustomer(Boolean isCustomer) {
		this.isCustomer = isCustomer;
	}

	public Boolean getIsVendor() {
		return isVendor;
	}

	public void setIsVendor(Boolean isVendor) {
		this.isVendor = isVendor;
	}

	/**
	 * The country code where this business partner is registered.
	 * @return	The country code where this business partner is registered.
	 */
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public boolean hasName() {
		return (name!=null && name.trim().length()>0);
	}
	
	public boolean hasIdentityNo() {
		return (identityNo!=null && identityNo.trim().length()>0);
	}
	
	public boolean hasTaxId() {
		return (taxId!=null && taxId.trim().length()>0);
	}
	
	public boolean hasContacts() {
		if (contacts==null) return false;
		for (Person person : contacts) {
			if (person.hasName())
				return true;
		}
		return false;
	}
	
	public boolean hasLocations() {
		return (getAddressOfficial()!=null || getAddressShipping()!=null);
	}
	
	@XmlTransient
	public boolean isAnonymous() {
		return !hasName() && !hasContacts();
	}
	
}
