package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.notima.generic.businessobjects.AccountingType;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;

/**
 * The accounts that invoices, payments and allocations are posted to: one account per
 * {@link AccountingType role}, plus revenue, purchase and VAT accounts per VAT rate.
 *
 * <p>The voucher builders of this package set the role on every line they create
 * ({@link AccountingVoucherLine#getAcctType()}) and the account from here. A role without an
 * account here leaves the line without one; {@link #missingAccounts(AccountingVoucher)}
 * lists such lines' roles, so the caller can ask for them before the voucher is saved.</p>
 *
 * <p>Defaults per country come from
 * {@link org.notima.generic.businessobjects.accounting.rule.CountryRuleSet#getDefaultPostingAccounts()}.</p>
 */
public class PostingAccounts {

	private Map<String, String>	accounts = new LinkedHashMap<String, String>();
	private Map<String, String>	revenueAccountsByRate = new LinkedHashMap<String, String>();
	private Map<String, String>	purchaseAccountsByRate = new LinkedHashMap<String, String>();
	private Map<String, String>	outputVatAccountsByRate = new LinkedHashMap<String, String>();
	private Map<String, String>	inputVatAccountsByRate = new LinkedHashMap<String, String>();

	/** Sets the account for {@code role}, one of the {@link AccountingType} constants. */
	public PostingAccounts set(String role, String acctNo) {
		accounts.put(role, acctNo);
		return this;
	}

	/** @return	The account for {@code role}, or null if none is set. */
	public String get(String role) {
		return accounts.get(role);
	}

	/** Sets the revenue account for sales at {@code rate} percent VAT. */
	public PostingAccounts setRevenueAccount(double rate, String acctNo) {
		revenueAccountsByRate.put(rateKey(rate), acctNo);
		return this;
	}

	/** Sets the account for purchases at {@code rate} percent VAT. */
	public PostingAccounts setPurchaseAccount(double rate, String acctNo) {
		purchaseAccountsByRate.put(rateKey(rate), acctNo);
		return this;
	}

	/** Sets the account for output VAT (on sales) at {@code rate} percent. */
	public PostingAccounts setOutputVatAccount(double rate, String acctNo) {
		outputVatAccountsByRate.put(rateKey(rate), acctNo);
		return this;
	}

	/** Sets the account for input VAT (on purchases) at {@code rate} percent. */
	public PostingAccounts setInputVatAccount(double rate, String acctNo) {
		inputVatAccountsByRate.put(rateKey(rate), acctNo);
		return this;
	}

	/** @return	The revenue account for {@code rate}, else the one for {@link AccountingType#REVENUE}. */
	public String getRevenueAccount(double rate) {
		return byRate(revenueAccountsByRate, rate, AccountingType.REVENUE);
	}

	/** @return	The purchase account for {@code rate}, else the one for {@link AccountingType#COGS}. */
	public String getPurchaseAccount(double rate) {
		return byRate(purchaseAccountsByRate, rate, AccountingType.COGS);
	}

	/** @return	The output VAT account for {@code rate}, else the one for {@link AccountingType#LIABILITY_VAT}. */
	public String getOutputVatAccount(double rate) {
		return byRate(outputVatAccountsByRate, rate, AccountingType.LIABILITY_VAT);
	}

	/** @return	The input VAT account for {@code rate}, else the one for {@link AccountingType#CLAIM_VAT}. */
	public String getInputVatAccount(double rate) {
		return byRate(inputVatAccountsByRate, rate, AccountingType.CLAIM_VAT);
	}

	private String byRate(Map<String, String> map, double rate, String role) {
		String acct = map.get(rateKey(rate));
		return acct != null ? acct : get(role);
	}

	/** "25", "12.5", "0": the rate without trailing zeros, so 25 and 25.0 are the same. */
	static String rateKey(double rate) {
		BigDecimal bd = BigDecimal.valueOf(rate).stripTrailingZeros();
		return bd.signum() == 0 ? "0" : bd.toPlainString();
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
		this.accounts = accounts;
	}

	public Map<String, String> getRevenueAccountsByRate() {
		return revenueAccountsByRate;
	}

	public void setRevenueAccountsByRate(Map<String, String> revenueAccountsByRate) {
		this.revenueAccountsByRate = revenueAccountsByRate;
	}

	public Map<String, String> getPurchaseAccountsByRate() {
		return purchaseAccountsByRate;
	}

	public void setPurchaseAccountsByRate(Map<String, String> purchaseAccountsByRate) {
		this.purchaseAccountsByRate = purchaseAccountsByRate;
	}

	public Map<String, String> getOutputVatAccountsByRate() {
		return outputVatAccountsByRate;
	}

	public void setOutputVatAccountsByRate(Map<String, String> outputVatAccountsByRate) {
		this.outputVatAccountsByRate = outputVatAccountsByRate;
	}

	public Map<String, String> getInputVatAccountsByRate() {
		return inputVatAccountsByRate;
	}

	public void setInputVatAccountsByRate(Map<String, String> inputVatAccountsByRate) {
		this.inputVatAccountsByRate = inputVatAccountsByRate;
	}

}
