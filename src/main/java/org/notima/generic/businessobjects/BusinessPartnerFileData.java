package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Normalised result of reading business partners (customers and/or suppliers) from a
 * file using a {@link org.notima.generic.ifacebusinessobjects.BusinessPartnerFileImporter}.
 * <p>
 * Whether a partner is a customer or a supplier is given by
 * {@link BusinessPartner#getIsCustomer()} / {@link BusinessPartner#getIsVendor()}.
 */
public class BusinessPartnerFileData {

	/** Format name of the source, e.g. {@code "Fortnox customer register"}. */
	private String						sourceFormat;
	/** The company the register belongs to, or null if unknown. */
	private BusinessPartner<?>			company;
	private List<BusinessPartner<?>>	businessPartners = new ArrayList<BusinessPartner<?>>();

	public String getSourceFormat() {
		return sourceFormat;
	}

	public void setSourceFormat(String sourceFormat) {
		this.sourceFormat = sourceFormat;
	}

	public BusinessPartner<?> getCompany() {
		return company;
	}

	public void setCompany(BusinessPartner<?> company) {
		this.company = company;
	}

	public List<BusinessPartner<?>> getBusinessPartners() {
		return businessPartners;
	}

	public void setBusinessPartners(List<BusinessPartner<?>> businessPartners) {
		this.businessPartners = businessPartners != null ? businessPartners : new ArrayList<BusinessPartner<?>>();
	}

}
