package org.notima.generic.businessobjects.accounting;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.notima.generic.businessobjects.AccountCategory;

/**
 * The result (credit - debit) per month of the accounts of a period, and their sums per
 * {@link AccountCategory}. Calculated by {@link LedgerCalculator#monthly}.
 */
public class MonthlyLedgerSummary {

	/** The monthly results of one account. */
	public static class MonthlyTotals {
		private final String			accountNo;
		private final String			name;
		private final AccountCategory	category;
		private final double[]			values;

		MonthlyTotals(String accountNo, String name, AccountCategory category, int months) {
			this.accountNo = accountNo;
			this.name = name;
			this.category = category;
			this.values = new double[months];
		}

		public String getAccountNo() {
			return accountNo;
		}
		public String getName() {
			return name;
		}
		public AccountCategory getCategory() {
			return category;
		}
		/** @return	The result (credit - debit) per month, in the order of {@link MonthlyLedgerSummary#getMonths()}. */
		public double[] getValues() {
			return values.clone();
		}
		/** @return	True if every month is zero. */
		public boolean isZero() {
			for (double v : values) if (v != 0.0) return false;
			return true;
		}
	}

	private final YearMonth[] months;
	private final Map<String, MonthlyTotals> accounts = new LinkedHashMap<String, MonthlyTotals>();
	private final Map<AccountCategory, double[]> categories = new EnumMap<AccountCategory, double[]>(AccountCategory.class);

	MonthlyLedgerSummary(YearMonth[] months) {
		this.months = months.clone();
	}

	public YearMonth[] getMonths() {
		return months.clone();
	}

	/** @return	The accounts with voucher lines in the months, sorted by account number. */
	public List<MonthlyTotals> getAccounts() {
		List<MonthlyTotals> result = new ArrayList<MonthlyTotals>(accounts.values());
		Collections.sort(result, (a, b) -> a.getAccountNo().compareTo(b.getAccountNo()));
		return result;
	}

	/** @return	The accounts of {@code category} with voucher lines in the months, sorted by account number. */
	public List<MonthlyTotals> getAccounts(AccountCategory category) {
		List<MonthlyTotals> result = new ArrayList<MonthlyTotals>();
		for (MonthlyTotals t : getAccounts()) {
			if (t.getCategory() == category) result.add(t);
		}
		return result;
	}

	/** @return	The sum per month of the accounts of {@code category}; null if none has voucher lines. */
	public double[] getCategoryTotals(AccountCategory category) {
		double[] v = categories.get(category);
		return v != null ? v.clone() : null;
	}

	/** @return	The sum per month of the given categories. */
	public double[] sum(AccountCategory... categoriesToSum) {
		double[] result = new double[months.length];
		for (AccountCategory c : categoriesToSum) {
			double[] v = categories.get(c);
			if (v == null) continue;
			for (int i = 0; i < result.length; i++) result[i] += v[i];
		}
		return result;
	}

	void add(String accountNo, String name, AccountCategory category, int month, double amount) {
		MonthlyTotals t = accounts.get(accountNo);
		if (t == null) {
			t = new MonthlyTotals(accountNo, name, category, months.length);
			accounts.put(accountNo, t);
		}
		t.values[month] += amount;
		categories.computeIfAbsent(category, k -> new double[months.length])[month] += amount;
	}

}
