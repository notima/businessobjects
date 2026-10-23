package org.notima.generic.businessobjects.accounting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.AccountCategory;
import org.notima.generic.businessobjects.AccountElement;
import org.notima.generic.businessobjects.AccountElement.AccountElementType;
import org.notima.generic.businessobjects.AccountingPeriodData;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;
import org.notima.generic.businessobjects.accounting.rule.AccountElementTypes;
import org.notima.generic.businessobjects.accounting.rule.CountryRuleSetRegistry;
import org.notima.generic.businessobjects.accounting.rule.se.SeCountryRuleSet;

class TestLedgerCalculator {

	private static AccountElement account(String no, double opening) {
		AccountElement ae = new AccountElement(no);
		ae.setName("Account " + no);
		ae.setOpeningBalance(opening);
		return ae;
	}

	private static AccountingVoucherLine line(String acctNo, double debit, double credit) {
		AccountingVoucherLine l = new AccountingVoucherLine();
		l.setAcctNo(acctNo);
		if (debit != 0) l.setDebitAmount(BigDecimal.valueOf(debit));
		if (credit != 0) l.setCreditAmount(BigDecimal.valueOf(credit));
		return l;
	}

	private static AccountingVoucher voucher(String series, String no, String date, AccountingVoucherLine... lines) {
		AccountingVoucher v = new AccountingVoucher();
		v.setVoucherSeries(series);
		v.setVoucherNo(no);
		v.setAcctDate(date != null ? LocalDate.parse(date) : null);
		v.setLines(new ArrayList<AccountingVoucherLine>(Arrays.asList(lines)));
		return v;
	}

	/** Bank, receivables, equity, sales and a cost account, classified as in Sweden. */
	private static AccountingPeriodData period(AccountingVoucher... vouchers) {
		AccountingPeriodData p = new AccountingPeriodData();
		p.setPeriodStart(LocalDate.parse("2026-01-01"));
		p.setPeriodEnd(LocalDate.parse("2026-12-31"));
		p.setChartOfAccounts(new ArrayList<AccountElement>(Arrays.asList(
				account("1930", 1000.0),
				account("1510", 0.0),
				account("2081", -1000.0),
				account("3010", 0.0),
				account("5010", 0.0))));
		CountryRuleSetRegistry.withDefaults().applyTo("SE", Arrays.asList(p));
		p.setVouchers(new ArrayList<AccountingVoucher>(Arrays.asList(vouchers)));
		return p;
	}

	@Test
	void accountsAreClassifiedBySwedishRules() {
		AccountingPeriodData p = period();
		AccountElement bank = p.getChartOfAccounts().get(0);
		assertEquals(AccountElementType.BalanceAcct, bank.getAccountElementType());
		assertEquals(AccountCategory.ASSET_CASH.name(), bank.getAccountClass());
		assertEquals(AccountCategory.ASSET_SHORT, LedgerCalculator.categoryOf(p.getChartOfAccounts().get(1)));
		assertEquals(AccountCategory.EQUITY, LedgerCalculator.categoryOf(p.getChartOfAccounts().get(2)));
		assertEquals(AccountElementType.PLAcct, p.getChartOfAccounts().get(3).getAccountElementType());
		assertEquals(AccountCategory.REVENUE, LedgerCalculator.categoryOf(p.getChartOfAccounts().get(3)));
	}

	@Test
	void balancesAreOpeningPlusMovements() {
		AccountingPeriodData p = period(
				voucher("A", "1", "2026-01-15", line("1510", 500, 0), line("3010", 0, 500)),
				voucher("A", "2", "2026-02-10", line("1930", 500, 0), line("1510", 0, 500)),
				voucher("A", "3", "2026-03-05", line("5010", 200, 0), line("1930", 0, 200)));

		LedgerSummary all = LedgerCalculator.summarize(p, AccountElementType.BalanceAcct, null, null);
		assertEquals(1000.0, all.getAccount("1930").getOpeningBalance(), 0.001);
		assertEquals(1300.0, all.getAccount("1930").getBalanceAtTo(), 0.001);
		assertEquals(0.0, all.getAccount("1510").getBalanceAtTo(), 0.001);
		assertNull(all.getAccount("3010"), "only balance sheet accounts");
		assertEquals(1300.0, all.getCategoryTotals(AccountCategory.ASSET_CASH).getBalanceAtTo(), 0.001);
		assertEquals(0.0, all.getCategoryTotals(AccountCategory.ASSET_SHORT).getBalanceAtTo(), 0.001);
		assertEquals(-1000.0, all.getCategoryTotals(AccountCategory.EQUITY).getBalanceAtTo(), 0.001);

		LedgerSummary feb = LedgerCalculator.summarize(p, AccountElementType.BalanceAcct,
				LocalDate.parse("2026-02-01"), LocalDate.parse("2026-02-28"));
		AccountTotals receivables = feb.getAccount("1510");
		assertEquals(500.0, receivables.getBalanceAtFrom(), 0.001, "January before the range");
		assertEquals(0.0, receivables.getBalanceAtTo(), 0.001);
		assertEquals(1500.0, feb.getAccount("1930").getBalanceAtTo(), 0.001, "March after the range");

		assertEquals(Double.valueOf(1300.0), LedgerCalculator.endingBalances(p).get("1930"));
	}

	@Test
	void profitAndLoss() {
		AccountingPeriodData p = period(
				voucher("A", "1", "2026-01-15", line("1510", 500, 0), line("3010", 0, 500)),
				voucher("A", "2", "2026-03-05", line("5010", 200, 0), line("1930", 0, 200)),
				voucher("A", "3", null, line("5010", 50, 0), line("1930", 0, 50)));

		LedgerSummary pl = LedgerCalculator.summarize(p, AccountElementType.PLAcct, null, null);
		assertEquals(500.0, pl.getCategoryTotals(AccountCategory.REVENUE).getResult(), 0.001);
		assertEquals(250.0, pl.getAccount("5010").getDebit(), 0.001, "a voucher without date is in any range");
		assertEquals(250.0, pl.total().getResult(), 0.001);
		assertEquals(500.0, pl.sum(AccountCategory.REVENUE, AccountCategory.COGS).getResult(), 0.001);

		LedgerSummary q1 = LedgerCalculator.summarize(p, AccountElementType.PLAcct,
				LocalDate.parse("2026-02-01"), LocalDate.parse("2026-03-31"));
		assertTrue(q1.getAccount("3010").hasNoMovements());
		assertEquals(250.0, q1.getAccount("5010").getNet(), 0.001);
	}

	@Test
	void struckLinesAndUnknownAccountsDontCount() {
		AccountingVoucherLine struck = line("1930", 999, 0);
		struck.setDeleted(Boolean.TRUE);
		AccountingPeriodData p = period(
				voucher("A", "1", "2026-01-15", struck, line("9999", 0, 999), line("1930", 100, 0)));
		LedgerSummary s = LedgerCalculator.summarize(p, null, null, null);
		assertEquals(1100.0, s.getAccount("1930").getBalanceAtTo(), 0.001);
		assertNull(s.getAccount("9999"));
	}

	@Test
	void balanceOnlyPeriodUsesEndingBalances() {
		AccountingPeriodData p = period();
		p.getChartOfAccounts().get(0).setEndingBalance(1750.0);
		p.getChartOfAccounts().get(3).setEndingBalance(400.0);
		LedgerSummary bs = LedgerCalculator.summarize(p, AccountElementType.BalanceAcct, null, null);
		assertEquals(1750.0, bs.getAccount("1930").getBalanceAtTo(), 0.001);
		LedgerSummary pl = LedgerCalculator.summarize(p, AccountElementType.PLAcct, null, null);
		assertEquals(400.0, pl.getAccount("3010").getCredit(), 0.001, "P&L ending balance taken as credit");
		assertEquals(Double.valueOf(1750.0), LedgerCalculator.endingBalances(p).get("1930"));
	}

	@Test
	void monthlyResults() {
		AccountingPeriodData p = period(
				voucher("A", "1", "2026-01-15", line("1510", 500, 0), line("3010", 0, 500)),
				voucher("A", "2", "2026-02-10", line("1510", 300, 0), line("3010", 0, 300)),
				voucher("A", "3", "2026-04-01", line("1510", 100, 0), line("3010", 0, 100)));
		YearMonth[] months = { YearMonth.of(2026, 1), YearMonth.of(2026, 2), YearMonth.of(2026, 3) };
		MonthlyLedgerSummary m = LedgerCalculator.monthly(p, AccountElementType.PLAcct, months);
		assertEquals(1, m.getAccounts().size(), "only P&L accounts with lines");
		double[] revenue = m.getCategoryTotals(AccountCategory.REVENUE);
		assertEquals(500.0, revenue[0], 0.001);
		assertEquals(300.0, revenue[1], 0.001);
		assertEquals(0.0, revenue[2], 0.001, "April is outside the months");
		assertNull(m.getCategoryTotals(AccountCategory.COGS));
	}

	@Test
	void accountLedgerHasRunningBalanceAndIsSorted() {
		List<AccountingVoucher> vouchers = Arrays.asList(
				voucher("A", "10", "2026-02-01", line("1930", 0, 50)),
				voucher("A", "9", "2026-02-01", line("1930", 100, 0)),
				voucher("A", "1", "2026-01-01", line("1930", 25, 0)),
				voucher("A", "11", "2026-03-01", line("1930", 5, 0)));
		AccountLedger ledger = LedgerCalculator.accountLedger(vouchers, "1930", "Bank", 1000.0,
				LocalDate.parse("2026-02-01"), LocalDate.parse("2026-02-28"));
		assertEquals(1025.0, ledger.getOpeningBalance(), 0.001, "January included in the opening balance");
		List<AccountLedger.Entry> entries = ledger.getEntries();
		assertEquals(2, entries.size());
		assertEquals("A 9", entries.get(0).getVoucherRef(), "numeric voucher order");
		assertEquals(1125.0, entries.get(0).getBalance(), 0.001);
		assertFalse(entries.get(0).isEndOfDay());
		assertTrue(entries.get(1).isEndOfDay());
		assertEquals(1075.0, ledger.getClosingBalance(), 0.001);
	}

	@Test
	void generalLedgerPerAccount() {
		AccountingPeriodData p = period(
				voucher("A", "1", "2026-01-15", line("1510", 500, 0), line("3010", 0, 500)),
				voucher("A", "2", "2026-02-10", line("1930", 500, 0), line("1510", 0, 500)));
		List<AccountLedger> gl = LedgerCalculator.generalLedger(p);
		assertEquals(Arrays.asList("1510", "1930", "3010"),
				Arrays.asList(gl.get(0).getAccountNo(), gl.get(1).getAccountNo(), gl.get(2).getAccountNo()));
		assertEquals(1500.0, gl.get(1).getClosingBalance(), 0.001);
		Map<String, Double> endings = LedgerCalculator.endingBalances(p);
		assertEquals(Double.valueOf(0.0), endings.get("1510"));
	}

	@Test
	void elementTypesFollowCategoryElseCountryRules() {
		AccountElement withCategory = new AccountElement("9999");
		withCategory.setAccountClass(AccountCategory.ASSET_CASH.name());
		assertEquals(AccountElementType.BalanceAcct, AccountElementTypes.of(withCategory, null));
		AccountElement withoutCategory = new AccountElement("3010");
		assertNull(AccountElementTypes.of(withoutCategory, null), "number not interpreted without country rules");
		assertEquals(AccountElementType.PLAcct, AccountElementTypes.of(withoutCategory, new SeCountryRuleSet()));
		assertNull(new SeCountryRuleSet().classifyCategory("9100"));
	}

	@Test
	void categoriesAreParsedFromNamesAndLegacyConstants() {
		assertEquals(AccountCategory.VAT_CLAIM, AccountCategory.parse("VAT_CLAIM"));
		assertEquals(AccountCategory.ASSET_LONG, AccountCategory.parse("assets-long"));
		assertEquals(AccountCategory.ASSET_CASH, AccountCategory.parse("assets-cash"));
		assertNull(AccountCategory.parse("nonsense"));
		assertNull(AccountCategory.parse(null));
	}

}
