package org.notima.generic.businessobjects.accounting;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.notima.generic.businessobjects.AccountCategory;
import org.notima.generic.businessobjects.AccountElement;
import org.notima.generic.businessobjects.AccountElement.AccountElementType;
import org.notima.generic.businessobjects.AccountingPeriodData;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;

/**
 * Calculates balances, movements and ledgers from an {@link AccountingPeriodData}: its chart
 * of accounts with opening (and ending) balances, and its vouchers. Stateless.
 *
 * <p>Rules that apply to all calculations:</p>
 * <ul>
 *   <li>Struck voucher lines ({@link AccountingVoucherLine#isDeleted()}, SIE {@code #BTRANS})
 *       don't count.</li>
 *   <li>Only accounts in the chart of accounts count; lines on other accounts are ignored.
 *       Accounts without a {@link AccountCategory} are {@link AccountCategory#OTHER}.</li>
 *   <li>Vouchers without a date are within any date range.</li>
 *   <li>A period without vouchers (ie a balance-only import) uses the ending balances of
 *       its chart of accounts as the movements of the period.</li>
 * </ul>
 */
public final class LedgerCalculator {

	private LedgerCalculator() {}

	/**
	 * Sums the accounts of {@code period} over a date range.
	 *
	 * @param period	The period.
	 * @param type		Only accounts of this type ({@link AccountElementType#BalanceAcct} or
	 * 					{@link AccountElementType#PLAcct}); null for all accounts.
	 * @param from		Start of the range, inclusive; null for the start of the period.
	 * 					Movements before it are summed in {@link AccountTotals#getMovementBeforeFrom()}.
	 * @param to		End of the range, inclusive; null for the end of the period.
	 * @return	The totals per account and category.
	 */
	public static LedgerSummary summarize(AccountingPeriodData period, AccountElementType type, LocalDate from, LocalDate to) {

		LedgerSummary summary = new LedgerSummary(from, to);
		if (period == null || period.getChartOfAccounts() == null) return summary;

		for (AccountElement ae : period.getChartOfAccounts()) {
			if (ae.getAccountNo() == null) continue;
			if (type != null && type != ae.getAccountElementType()) continue;
			AccountTotals t = summary.addAccount(ae.getAccountNo(), ae.getName() != null ? ae.getName() : "", categoryOf(ae));
			if (ae.getAccountElementType() == AccountElementType.BalanceAcct) {
				t.addOpeningBalance(ae.getOpeningBalance());
			}
		}

		if (hasVouchers(period)) {
			for (AccountingVoucher v : period.getVouchers()) {
				if (v.getLines() == null) continue;
				LocalDate date = v.getAcctDate();
				boolean beforeFrom = from != null && date != null && date.isBefore(from);
				boolean afterTo    = to   != null && date != null && date.isAfter(to);
				if (afterTo) continue;
				for (AccountingVoucherLine line : v.getLines()) {
					if (isStruck(line)) continue;
					AccountTotals t = summary.getAccount(line.getAcctNo());
					if (t == null) continue;
					if (beforeFrom) {
						t.addBeforeFrom(debitOf(line) - creditOf(line));
					} else {
						t.addDebit(debitOf(line));
						t.addCredit(creditOf(line));
					}
				}
			}
		} else {
			// No vouchers: the movement of the period is the difference between ending and opening balance
			for (AccountElement ae : period.getChartOfAccounts()) {
				AccountTotals t = ae.getAccountNo() != null ? summary.getAccount(ae.getAccountNo()) : null;
				if (t == null) continue;
				double delta = ae.getEndingBalance() - ae.getOpeningBalance();
				if (delta == 0.0) continue;
				if (ae.getAccountElementType() == AccountElementType.PLAcct) {
					// P&L ending balances are taken as results: positive is credit
					if (delta > 0) t.addCredit(delta); else t.addDebit(-delta);
				} else {
					if (delta > 0) t.addDebit(delta); else t.addCredit(-delta);
				}
			}
		}

		summary.sumCategories();
		return summary;

	}

	/**
	 * Sums the result (credit - debit) per month of the accounts of {@code period}. Only
	 * vouchers with a date in one of the months count.
	 *
	 * @param period	The period.
	 * @param type		Only accounts of this type; null for all accounts.
	 * @param months	The months to sum.
	 */
	public static MonthlyLedgerSummary monthly(AccountingPeriodData period, AccountElementType type, YearMonth[] months) {

		MonthlyLedgerSummary summary = new MonthlyLedgerSummary(months);
		if (period == null || period.getChartOfAccounts() == null || period.getVouchers() == null) return summary;

		Map<String, AccountElement> chart = chartOf(period, type);
		Map<YearMonth, Integer> slots = new HashMap<YearMonth, Integer>();
		for (int i = 0; i < months.length; i++) slots.put(months[i], i);

		for (AccountingVoucher v : period.getVouchers()) {
			if (v.getAcctDate() == null || v.getLines() == null) continue;
			Integer slot = slots.get(YearMonth.from(v.getAcctDate()));
			if (slot == null) continue;
			for (AccountingVoucherLine line : v.getLines()) {
				if (isStruck(line)) continue;
				AccountElement ae = chart.get(line.getAcctNo());
				if (ae == null) continue;
				summary.add(ae.getAccountNo(), ae.getName() != null ? ae.getName() : "", categoryOf(ae),
						slot, creditOf(line) - debitOf(line));
			}
		}
		return summary;

	}

	/**
	 * The ending balance of every balance sheet account of {@code period}: the opening balance
	 * plus the movements of the period, or the stored ending balance if the period has no vouchers.
	 */
	public static Map<String, Double> endingBalances(AccountingPeriodData period) {
		Map<String, Double> endings = new HashMap<String, Double>();
		if (period == null || period.getChartOfAccounts() == null) return endings;
		LedgerSummary summary = summarize(period, AccountElementType.BalanceAcct, null, null);
		for (AccountTotals t : summary.getAccounts()) {
			endings.put(t.getAccountNo(), t.getBalanceAtTo());
		}
		return endings;
	}

	/**
	 * The net movement (debit - credit) per account of {@code vouchers}, for all accounts,
	 * whether in a chart of accounts or not.
	 */
	public static Map<String, Double> netMovements(Collection<AccountingVoucher> vouchers) {
		Map<String, Double> movements = new HashMap<String, Double>();
		if (vouchers == null) return movements;
		for (AccountingVoucher v : vouchers) {
			if (v.getLines() == null) continue;
			for (AccountingVoucherLine line : v.getLines()) {
				if (isStruck(line)) continue;
				if (line.getAcctNo() == null) continue;
				movements.merge(line.getAcctNo(), debitOf(line) - creditOf(line), Double::sum);
			}
		}
		return movements;
	}

	/**
	 * The transactions of one account over a date range.
	 *
	 * @param vouchers			The vouchers of the period.
	 * @param accountNo			The account.
	 * @param accountName		The name of the account, for the result.
	 * @param periodOpening		The opening balance of the account for the period.
	 * @param from				Start of the range, inclusive; null for the start of the period.
	 * 							Lines before it are included in the opening balance.
	 * @param to				End of the range, inclusive; null for the end of the period.
	 */
	public static AccountLedger accountLedger(Collection<AccountingVoucher> vouchers, String accountNo, String accountName,
			double periodOpening, LocalDate from, LocalDate to) {

		double opening = periodOpening;
		List<AccountLedger.Entry> entries = new ArrayList<AccountLedger.Entry>();
		if (vouchers != null) {
			for (AccountingVoucher v : vouchers) {
				if (v.getLines() == null) continue;
				LocalDate date = v.getAcctDate();
				boolean beforeFrom = from != null && date != null && date.isBefore(from);
				boolean afterTo    = to   != null && date != null && date.isAfter(to);
				if (afterTo) continue;
				for (AccountingVoucherLine line : v.getLines()) {
					if (isStruck(line)) continue;
					if (accountNo == null || !accountNo.equals(line.getAcctNo())) continue;
					if (beforeFrom) {
						opening += debitOf(line) - creditOf(line);
					} else {
						entries.add(new AccountLedger.Entry(v, line));
					}
				}
			}
		}
		Collections.sort(entries, ENTRY_ORDER);
		return new AccountLedger(accountNo, accountName, opening, entries);

	}

	/**
	 * The transactions of every account of {@code period} that has voucher lines, sorted by
	 * account number. The opening balances are those of the chart of accounts.
	 */
	public static List<AccountLedger> generalLedger(AccountingPeriodData period) {

		List<AccountLedger> result = new ArrayList<AccountLedger>();
		if (period == null || period.getVouchers() == null) return result;

		Map<String, AccountElement> chart = chartOf(period, null);
		Map<String, List<AccountingVoucher>> vouchersPerAccount = new TreeMap<String, List<AccountingVoucher>>();
		for (AccountingVoucher v : period.getVouchers()) {
			if (v.getLines() == null) continue;
			for (AccountingVoucherLine line : v.getLines()) {
				if (isStruck(line)) continue;
				String acct = line.getAcctNo();
				if (acct == null || acct.isEmpty()) continue;
				List<AccountingVoucher> list = vouchersPerAccount.computeIfAbsent(acct, k -> new ArrayList<AccountingVoucher>());
				if (list.isEmpty() || list.get(list.size() - 1) != v) list.add(v);
			}
		}
		for (Map.Entry<String, List<AccountingVoucher>> e : vouchersPerAccount.entrySet()) {
			AccountElement ae = chart.get(e.getKey());
			result.add(accountLedger(e.getValue(), e.getKey(),
					ae != null && ae.getName() != null ? ae.getName() : "",
					ae != null ? ae.getOpeningBalance() : 0.0, null, null));
		}
		return result;

	}

	/** The category of {@code ae}; {@link AccountCategory#OTHER} if it has none or an unknown one. */
	public static AccountCategory categoryOf(AccountElement ae) {
		AccountCategory c = AccountCategory.parse(ae.getAccountClass());
		return c != null ? c : AccountCategory.OTHER;
	}

	/** True when {@code line} has been struck from its voucher: it stays for the audit trail, but doesn't count. */
	public static boolean isStruck(AccountingVoucherLine line) {
		return line != null && Boolean.TRUE.equals(line.isDeleted());
	}

	static double debitOf(AccountingVoucherLine line) {
		return line.getDebitAmount() != null ? line.getDebitAmount().doubleValue() : 0.0;
	}

	static double creditOf(AccountingVoucherLine line) {
		return line.getCreditAmount() != null ? line.getCreditAmount().doubleValue() : 0.0;
	}

	private static boolean hasVouchers(AccountingPeriodData period) {
		return period.getVouchers() != null && !period.getVouchers().isEmpty();
	}

	private static Map<String, AccountElement> chartOf(AccountingPeriodData period, AccountElementType type) {
		Map<String, AccountElement> chart = new LinkedHashMap<String, AccountElement>();
		if (period.getChartOfAccounts() == null) return chart;
		for (AccountElement ae : period.getChartOfAccounts()) {
			if (ae.getAccountNo() == null) continue;
			if (type != null && type != ae.getAccountElementType()) continue;
			chart.put(ae.getAccountNo(), ae);
		}
		return chart;
	}

	/** Date (nulls last), then voucher series, then voucher number (numeric before text). */
	private static final Comparator<AccountLedger.Entry> ENTRY_ORDER = Comparator
			.<AccountLedger.Entry>comparingLong(e -> e.getDate() != null ? e.getDate().toEpochDay() : Long.MAX_VALUE)
			.thenComparing(e -> e.getVoucher().getVoucherSeries() != null ? e.getVoucher().getVoucherSeries() : "")
			.thenComparing((a, b) -> compareVoucherNo(a.getVoucher().getVoucherNo(), b.getVoucher().getVoucherNo()));

	/**
	 * Compares voucher numbers numerically when both are numeric; numeric numbers sort before
	 * non-numeric ones, which are compared as text. Nulls sort last.
	 */
	static int compareVoucherNo(String a, String b) {
		Long na = parseVoucherNo(a);
		Long nb = parseVoucherNo(b);
		if (na != null && nb != null) return Long.compare(na, nb);
		if (na != null) return -1;
		if (nb != null) return 1;
		if (a == null) return b == null ? 0 : 1;
		if (b == null) return -1;
		return a.compareTo(b);
	}

	private static Long parseVoucherNo(String no) {
		if (no == null) return null;
		try {
			return Long.parseLong(no.trim());
		} catch (NumberFormatException ignored) {
			return null;
		}
	}

}
