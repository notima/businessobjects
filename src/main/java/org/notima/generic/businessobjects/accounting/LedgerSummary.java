package org.notima.generic.businessobjects.accounting;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.notima.generic.businessobjects.AccountCategory;

/**
 * The {@link AccountTotals} of all accounts of a period over a date range, and their sums per
 * {@link AccountCategory}. Calculated by {@link LedgerCalculator#summarize}.
 */
public class LedgerSummary {

	private final LocalDate from;
	private final LocalDate to;
	private final Map<String, AccountTotals> accounts = new LinkedHashMap<String, AccountTotals>();
	private final Map<AccountCategory, AccountTotals> categories = new EnumMap<AccountCategory, AccountTotals>(AccountCategory.class);

	LedgerSummary(LocalDate from, LocalDate to) {
		this.from = from;
		this.to = to;
	}

	/** @return	The start of the range; null for the start of the period. */
	public LocalDate getFrom() {
		return from;
	}

	/** @return	The end of the range; null for the end of the period. */
	public LocalDate getTo() {
		return to;
	}

	/** @return	All accounts, sorted by account number. */
	public List<AccountTotals> getAccounts() {
		List<AccountTotals> result = new ArrayList<AccountTotals>(accounts.values());
		Collections.sort(result, (a, b) -> a.getAccountNo().compareTo(b.getAccountNo()));
		return result;
	}

	/** @return	The accounts of {@code category}, sorted by account number. */
	public List<AccountTotals> getAccounts(AccountCategory category) {
		List<AccountTotals> result = new ArrayList<AccountTotals>();
		for (AccountTotals t : getAccounts()) {
			if (t.getCategory() == category) result.add(t);
		}
		return result;
	}

	/** @return	The totals of one account; null if it isn't in the summary. */
	public AccountTotals getAccount(String accountNo) {
		return accounts.get(accountNo);
	}

	/** @return	The sum of the accounts of {@code category}; all zero if it has none. */
	public AccountTotals getCategoryTotals(AccountCategory category) {
		AccountTotals t = categories.get(category);
		return t != null ? t : new AccountTotals(null, null, category);
	}

	/** @return	True if {@code category} has at least one account in the summary. */
	public boolean hasCategory(AccountCategory category) {
		return categories.containsKey(category);
	}

	/** @return	The sum of the given categories. */
	public AccountTotals sum(AccountCategory... categoriesToSum) {
		AccountTotals result = new AccountTotals(null, null, null);
		for (AccountCategory c : categoriesToSum) {
			AccountTotals t = categories.get(c);
			if (t != null) result.add(t);
		}
		return result;
	}

	/** @return	The sum of all accounts. */
	public AccountTotals total() {
		AccountTotals result = new AccountTotals(null, null, null);
		for (AccountTotals t : categories.values()) {
			result.add(t);
		}
		return result;
	}

	AccountTotals addAccount(String accountNo, String name, AccountCategory category) {
		AccountTotals t = accounts.get(accountNo);
		if (t == null) {
			t = new AccountTotals(accountNo, name, category);
			accounts.put(accountNo, t);
			if (!categories.containsKey(category)) {
				categories.put(category, new AccountTotals(null, null, category));
			}
		}
		return t;
	}

	void sumCategories() {
		for (AccountCategory c : categories.keySet()) {
			categories.put(c, new AccountTotals(null, null, c));
		}
		for (AccountTotals t : accounts.values()) {
			categories.get(t.getCategory()).add(t);
		}
	}

}
