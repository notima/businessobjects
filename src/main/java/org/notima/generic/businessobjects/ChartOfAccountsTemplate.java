package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * A named, reusable chart of accounts (e.g. a national standard such as the
 * Swedish BAS plan) that can be applied to seed a new company or accounting
 * period with a starting set of {@link AccountElement}s.
 * <p>
 * The template itself carries no balances &mdash; {@link AccountElement#getOpeningBalance()}
 * and {@link AccountElement#getEndingBalance()} are expected to be zero on every
 * account until the template has been applied to a specific period.
 */
public class ChartOfAccountsTemplate {

	private String	id;
	private String	name;
	private String	countryCode;

	private List<AccountElement> accounts = new ArrayList<>();

	public ChartOfAccountsTemplate() {}

	public ChartOfAccountsTemplate(String id, String name, String countryCode) {
		this.id = id;
		this.name = name;
		this.countryCode = countryCode;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public List<AccountElement> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountElement> accounts) {
		this.accounts = accounts != null ? accounts : new ArrayList<>();
	}

	@Override
	public String toString() {
		return name != null ? name : id;
	}

}
