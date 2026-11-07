package org.notima.generic.businessobjects.settlement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.notima.generic.businessobjects.AccountingType;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;

/**
 * The accounts that invoices, payments and allocations are posted to, one per
 * {@link AccountingType role}: receivables, payables, bank, prepayments, rounding, fees,
 * write-offs, discounts, and the default revenue and purchase accounts.
 *
 * <p>VAT accounts, and the revenue or expense account of a particular kind of trade (ie EU
 * sales), aren't kept here but in the tax rules of the
 * {@link org.notima.generic.businessobjects.tax.TaxEngine tax engine}.</p>
 *
 * <p>The voucher builders of this package set the role on every line they create
 * ({@link AccountingVoucherLine#getAcctType()}). A line whose account couldn't be found is
 * left without one; {@link #missingAccounts(AccountingVoucher)} lists such lines' roles, so
 * the caller can ask for them before the voucher is saved.</p>
 *
 * <p>Defaults per country come from
 * {@link org.notima.generic.businessobjects.accounting.rule.CountryRuleSet#getDefaultPostingAccounts()}.</p>
 */
public class PostingAccounts {

	private Map<String, String>	accounts = new LinkedHashMap<String, String>();

	/** Sets the account for {@code role}, one of the {@link AccountingType} constants. */
	public PostingAccounts set(String role, String acctNo) {
		accounts.put(role, acctNo);
		return this;
	}

	/** @return	The account for {@code role}, or null if none is set. */
	public String get(String role) {
		return accounts.get(role);
	}

	/**
	 * @return	The roles of the lines of {@code voucher} that have no account, each once, in
	 * 			the order they first occur. Lines without a role are listed as null.
	 */
	public static List<String> missingAccounts(AccountingVoucher voucher) {
		List<String> missing = new ArrayList<String>();
		if (voucher.getLines() == null) return missing;
		for (AccountingVoucherLine l : voucher.getLines()) {
			if ((l.getAcctNo() == null || l.getAcctNo().trim().isEmpty()) && !missing.contains(l.getAcctType())) {
				missing.add(l.getAcctType());
			}
		}
		return missing;
	}

	public Map<String, String> getAccounts() {
		return accounts;
	}

	public void setAccounts(Map<String, String> accounts) {
		this.accounts = accounts != null ? accounts : new LinkedHashMap<String, String>();
	}

}
