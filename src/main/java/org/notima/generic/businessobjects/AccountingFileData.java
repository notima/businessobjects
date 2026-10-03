package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Normalised result of reading an accounting file using an
 * {@link org.notima.generic.ifacebusinessobjects.AccountingFileImporter}.
 */
public class AccountingFileData {

	/** Format name of the source, e.g. {@code "SIE4"}. */
	private String						sourceFormat;
	/** ISO 3166-1 alpha-2 country code of the accounting convention, or null if unknown. */
	private String						countryCode;
	/** The company the accounting data belongs to. */
	private BusinessPartner<?>			company;
	private List<AccountingPeriodData>	periods = new ArrayList<AccountingPeriodData>();

	public String getSourceFormat() {
		return sourceFormat;
	}

	public void setSourceFormat(String sourceFormat) {
		this.sourceFormat = sourceFormat;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public BusinessPartner<?> getCompany() {
		return company;
	}

	public void setCompany(BusinessPartner<?> company) {
		this.company = company;
	}

	public List<AccountingPeriodData> getPeriods() {
		return periods;
	}

	public void setPeriods(List<AccountingPeriodData> periods) {
		this.periods = periods != null ? periods : new ArrayList<AccountingPeriodData>();
	}

}
