package org.notima.generic.businessobjects.accounting.statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.AccountElement;
import org.notima.generic.businessobjects.AccountingPeriodData;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;
import org.notima.generic.businessobjects.accounting.rule.CountryRuleSetRegistry;
import org.notima.generic.businessobjects.accounting.rule.se.SeK2Statements;

class TestAnnualStatementCalculator {

	private static AccountElement account(String no, double opening) {
		AccountElement ae = new AccountElement(no);
		ae.setName("Account " + no);
		ae.setOpeningBalance(opening);
		return ae;
	}

	/** An account of a period without vouchers: its balance is the ending balance. */
	private static AccountElement balance(String no, double balance) {
		AccountElement ae = account(no, balance);
		ae.setEndingBalance(balance);
		return ae;
	}

	private static AccountingVoucherLine line(String acctNo, double amount) {
		AccountingVoucherLine l = new AccountingVoucherLine();
		l.setAcctNo(acctNo);
		if (amount > 0) l.setDebitAmount(BigDecimal.valueOf(amount));
		else l.setCreditAmount(BigDecimal.valueOf(-amount));
		return l;
	}

	private static AccountingVoucher voucher(String no, AccountingVoucherLine... lines) {
		AccountingVoucher v = new AccountingVoucher();
		v.setVoucherSeries("A");
		v.setVoucherNo(no);
		v.setAcctDate(LocalDate.parse("2025-03-01"));
		v.setLines(new ArrayList<AccountingVoucherLine>(Arrays.asList(lines)));
		return v;
	}

	private static AccountingPeriodData period(List<AccountElement> coa, AccountingVoucher... vouchers) {
		AccountingPeriodData p = new AccountingPeriodData();
		p.setPeriodStart(LocalDate.parse("2025-01-01"));
		p.setPeriodEnd(LocalDate.parse("2025-12-31"));
		p.setChartOfAccounts(new ArrayList<AccountElement>(coa));
		CountryRuleSetRegistry.withDefaults().applyTo("SE", Arrays.asList(p));
		p.setVouchers(new ArrayList<AccountingVoucher>(Arrays.asList(vouchers)));
		return p;
	}

	/** Bank 1000, share capital 1000, then a sale and a cost. The result isn't booked. */
	private static AccountingPeriodData unclosedYear() {
		return period(Arrays.asList(
						account("1930", 1000.0), account("1510", 0.0), account("2081", -1000.0),
						account("2099", 0.0), account("2440", 0.0), account("3010", 0.0),
						account("5010", 0.0), account("8999", 0.0)),
				voucher("1", line("1510", 5000.40), line("3010", -5000.40)),
				voucher("2", line("5010", 1200.30), line("2440", -1200.30)));
	}

	private static AnnualStatement calculate(AccountingPeriodData current, AccountingPeriodData previous) {
		return AnnualStatementCalculator.calculate(SeK2Statements.templates(), SeK2Statements.basMapping(),
				current, previous, AmountUnit.KR);
	}

	private static long rounded(AnnualStatement s, String id) {
		return s.getRow(id).getRounded(0);
	}

	@Test
	void unbookedResultGoesToTheResultOfTheYear() {
		AnnualStatement s = calculate(unclosedYear(), null);

		assertEquals(5000L, rounded(s, SeK2Statements.NETTOOMSATTNING));
		assertEquals(-1200L, rounded(s, SeK2Statements.OVRIGA_EXTERNA_KOSTNADER));
		assertEquals(3800L, rounded(s, SeK2Statements.ARETS_RESULTAT));
		assertEquals(3800.10, s.getRow(SeK2Statements.ARETS_RESULTAT).getExact(0), 0.001);
		assertEquals(3800L, rounded(s, SeK2Statements.ARETS_RESULTAT_EK));
		assertEquals(6000L, rounded(s, SeK2Statements.TILLGANGAR));
		assertEquals(6000L, rounded(s, SeK2Statements.EGET_KAPITAL_OCH_SKULDER));
		assertFalse(s.hasErrors(), s.getWarnings().toString());
		assertFalse(s.hasPrevious());
	}

	@Test
	void bookedResultIsTakenFromTheBooks() {
		AccountingPeriodData p = unclosedYear();
		p.getVouchers().add(voucher("3", line("8999", 3800.10), line("2099", -3800.10)));
		AnnualStatement s = calculate(p, null);

		assertEquals(3800L, rounded(s, SeK2Statements.ARETS_RESULTAT));
		assertEquals(3800L, rounded(s, SeK2Statements.ARETS_RESULTAT_EK));
		List<AccountAmount> accounts = s.getRow(SeK2Statements.ARETS_RESULTAT_EK).getAccounts(0);
		assertEquals(1, accounts.size());
		assertEquals("2099", accounts.get(0).getAccountNo());
		assertEquals(3800.10, accounts.get(0).getAmount(), 0.001, "shown with the sign of the line");
		assertFalse(s.hasErrors(), s.getWarnings().toString());
	}

	@Test
	void roundingDifferenceOfTheResultGoesOnNetSales() {
		// 5000.40 + 0.45 - 1200.30 = 3800.55: the lines round to 5000 + 0 - 1200 = 3800, the result to 3801
		AccountingPeriodData p = period(Arrays.asList(
						account("1930", 0.0), account("2099", 0.0), account("3010", 0.0),
						account("3900", 0.0), account("5010", 0.0)),
				voucher("1", line("1930", 5000.40), line("3010", -5000.40)),
				voucher("2", line("1930", 0.45), line("3900", -0.45)),
				voucher("3", line("5010", 1200.30), line("1930", -1200.30)));
		AnnualStatement s = calculate(p, null);

		assertEquals(3801L, rounded(s, SeK2Statements.ARETS_RESULTAT));
		assertEquals(5001L, rounded(s, SeK2Statements.NETTOOMSATTNING));
		assertFalse(s.getRow(SeK2Statements.OVRIGA_RORELSEINTAKTER).isVisible(), "0.45 rounds to nothing");
		StatementWarning w = s.getWarnings().get(0);
		assertEquals(StatementWarning.Code.ROUNDING_ADJUSTED, w.getCode());
		assertEquals(SeK2Statements.NETTOOMSATTNING, w.getLineId());
		assertEquals(1.0, w.getAmount(), 0.001);
	}

	@Test
	void roundingAdjustsTheLastLiabilityOfTheBalanceSheet() {
		// Assets 600.40 + 600.40 = 1200.80 shown as 600 + 600 = 1200; liabilities 1200.80 shown as 1201
		AccountingPeriodData p = period(Arrays.asList(
				balance("1510", 600.40), balance("1930", 600.40), balance("2081", -1000.0),
				balance("2440", -100.0), balance("2990", -100.80)));
		AnnualStatement s = calculate(p, null);

		assertEquals(1200L, rounded(s, SeK2Statements.TILLGANGAR));
		assertEquals(1200L, rounded(s, SeK2Statements.EGET_KAPITAL_OCH_SKULDER));
		assertEquals(100L, rounded(s, SeK2Statements.UPPLUPNA_KOSTNADER));
		assertEquals(100L, rounded(s, SeK2Statements.LEVERANTORSSKULDER));
	}

	@Test
	void taxReceivableIsNettedAgainstTheTaxLiability() {
		AccountingPeriodData receivable = period(Arrays.asList(
				balance("1630", 371.0), balance("1640", 42317.0), balance("2510", -9260.0), balance("2081", -33428.0)));
		AnnualStatement s = calculate(receivable, null);
		assertEquals(33428L, rounded(s, SeK2Statements.OVRIGA_FORDRINGAR));
		assertFalse(s.getRow(SeK2Statements.SKATTESKULDER).isVisible());

		AccountingPeriodData liability = period(Arrays.asList(
				balance("1630", 35245.0), balance("2510", 139.0), balance("2514", -29149.0), balance("2081", -6235.0)));
		s = calculate(liability, null);
		assertEquals(35245L, rounded(s, SeK2Statements.OVRIGA_FORDRINGAR), "the tax account isn't netted");
		assertEquals(29010L, rounded(s, SeK2Statements.SKATTESKULDER));
		assertEquals(2, s.getRow(SeK2Statements.SKATTESKULDER).getAccounts(0).size());
	}

	@Test
	void overdraftIsALiability() {
		AccountingPeriodData p = period(Arrays.asList(
				balance("1930", -500.0), balance("1940", 800.0), balance("2081", -300.0)));
		AnnualStatement s = calculate(p, null);
		assertEquals(800L, rounded(s, SeK2Statements.KASSA_OCH_BANK));
		assertEquals(500L, rounded(s, SeK2Statements.CHECKRAKNINGSKREDIT_KORT));
	}

	@Test
	void unmappedAccountsAndMismatchesAreReported() {
		AccountingPeriodData p = period(Arrays.asList(account("1930", 100.0), account("2081", -100.0), account("9100", 0.0)),
				voucher("1", line("9100", 50.0), line("1930", -50.0)));
		AnnualStatement s = calculate(p, null);
		assertTrue(s.hasErrors());
		boolean unmapped = false, balance = false;
		for (StatementWarning w : s.getWarnings()) {
			if (w.getCode() == StatementWarning.Code.UNMAPPED_ACCOUNT && "9100".equals(w.getAccountNo())) unmapped = true;
			if (w.getCode() == StatementWarning.Code.BALANCE_MISMATCH) balance = true;
		}
		assertTrue(unmapped, s.getWarnings().toString());
		assertTrue(balance, s.getWarnings().toString());
		for (StatementWarning w : s.getWarnings()) {
			assertTrue(w.getCode() != StatementWarning.Code.ROUNDING_ADJUSTED || w.getLineId().equals(SeK2Statements.NETTOOMSATTNING),
					"a real difference isn't hidden as rounding: " + w);
		}
		assertEquals(-50L, rounded(s, SeK2Statements.TILLGANGAR) - rounded(s, SeK2Statements.EGET_KAPITAL_OCH_SKULDER), "50 kr went to the unmapped account");
	}

	@Test
	void overridesAndPreviousYear() {
		AccountingPeriodData previous = period(Arrays.asList(
				account("1332", 0.0), account("1930", 0.0), account("2081", 0.0)));
		previous.setPeriodStart(LocalDate.parse("2024-01-01"));
		previous.setPeriodEnd(LocalDate.parse("2024-12-31"));
		previous.getChartOfAccounts().get(0).setEndingBalance(300.0);
		previous.getChartOfAccounts().get(1).setEndingBalance(700.0);
		previous.getChartOfAccounts().get(2).setEndingBalance(-1000.0);
		AccountingPeriodData current = period(Arrays.asList(
				balance("1332", 300.0), balance("1930", 600.0), balance("2081", -900.0)));

		StatementMapping mapping = SeK2Statements.basMapping()
				.withOverrides(Collections.singletonMap("1332", SeK2Statements.FORDRINGAR_INTRESSEFORETAG_LANG));
		AnnualStatement s = AnnualStatementCalculator.calculate(SeK2Statements.templates(), mapping, current, previous, null);

		assertEquals(AmountUnit.KR, s.getUnit());
		assertTrue(s.hasPrevious());
		assertEquals(LocalDate.parse("2024-12-31"), s.getPeriodEnd(1));
		StatementRow fordringar = s.getRow(SeK2Statements.FORDRINGAR_INTRESSEFORETAG_LANG);
		assertEquals(300L, fordringar.getRounded(0));
		assertEquals(300L, fordringar.getRounded(1));
		assertFalse(s.getRow(SeK2Statements.ANDELAR_INTRESSEFORETAG).isVisible());
		assertEquals(700L, s.getRow(SeK2Statements.KASSA_OCH_BANK).getRounded(1));

		java.util.Map<String, Double> opening = new java.util.HashMap<String, Double>();
		for (StatementWarning w : s.getWarnings()) {
			if (w.getCode() == StatementWarning.Code.OPENING_MISMATCH) opening.put(w.getAccountNo(), w.getAmount());
		}
		assertEquals(2, opening.size(), s.getWarnings().toString());
		assertEquals(-100.0, opening.get("1930"), 0.001, "current opening 600 differs from previous ending 700");
		assertEquals(100.0, opening.get("2081"), 0.001);
		assertFalse(s.hasErrors(), s.getWarnings().toString());
	}

	@Test
	void sectionsWithoutAmountsAreHidden() {
		AnnualStatement s = calculate(unclosedYear(), null);
		assertFalse(s.getRow(SeK2Statements.IMMATERIELLA).isVisible());
		assertTrue(s.getRow(SeK2Statements.KORTFRISTIGA_FORDRINGAR).isVisible());
		assertTrue(s.getRow(SeK2Statements.TILLGANGAR).isVisible());
		assertFalse(s.getRow(SeK2Statements.BOKSLUTSDISPOSITIONER).isVisible(), "no appropriations in either year");
		assertTrue(s.getRow(SeK2Statements.RORELSEINTAKTER).isVisible());
		assertFalse(s.getRow(SeK2Statements.RESULTAT_FORE_SKATT).isVisible(), "the same as the result after financial items");
		assertTrue(s.getRow(SeK2Statements.RORELSERESULTAT).isVisible());
		assertFalse(s.getRow(SeK2Statements.RESULTAT_EFTER_FINANSIELLA).isVisible(), "no financial items either");
		assertTrue(s.getRow(SeK2Statements.ARETS_RESULTAT).isVisible(), "the last result is always shown");
		for (StatementRow r : s.getVisibleRows(SeK2Statements.BR)) {
			assertNotNull(r.getLabel());
		}
		assertNull(SeK2Statements.basMapping().place("ABCD"));
		assertTrue(SeK2Statements.basMapping().place("8999").isClosing());
	}

	@Test
	void unitIsSuggestedFromTotalAssets() {
		assertEquals(AmountUnit.KR, AmountUnit.suggest(2282307));
		assertEquals(AmountUnit.TKR, AmountUnit.suggest(120000000));
		assertEquals(AmountUnit.MKR, AmountUnit.suggest(8000000000.0));
	}

	@Test
	void companyFormFollowsTheOrganisationNumber() {
		assertEquals(SeK2Statements.CompanyForm.AKTIEBOLAG, SeK2Statements.CompanyForm.of("556745-6941"));
		assertEquals(SeK2Statements.CompanyForm.EKONOMISK_FORENING, SeK2Statements.CompanyForm.of("769612-3456"));
		assertEquals(SeK2Statements.CompanyForm.EKONOMISK_FORENING, SeK2Statements.CompanyForm.of("16769612-3456"));
		assertEquals(SeK2Statements.CompanyForm.AKTIEBOLAG, SeK2Statements.CompanyForm.of(null));
	}

	/** A bostadsrättsförening: insatser and fond för yttre underhåll (4.16), no Fri överkursfond. */
	@Test
	void ekonomiskForeningHasItsOwnEquity() {
		AccountingPeriodData p = period(Arrays.asList(
				balance("1930", 1500.0), balance("2083", -1000.0), balance("2088", -300.0), balance("2091", -200.0)));
		SeK2Statements.CompanyForm form = SeK2Statements.CompanyForm.EKONOMISK_FORENING;
		AnnualStatement s = AnnualStatementCalculator.calculate(SeK2Statements.templates(form),
				SeK2Statements.basMapping(form), p, null, AmountUnit.KR);
		assertEquals(1000L, rounded(s, SeK2Statements.MEDLEMSINSATSER));
		assertEquals(300L, rounded(s, SeK2Statements.FOND_YTTRE_UNDERHALL));
		assertEquals(1300L, rounded(s, SeK2Statements.BUNDET_EGET_KAPITAL));
		assertNull(s.getRow(SeK2Statements.FRI_OVERKURSFOND));
		assertNull(s.getRow(SeK2Statements.AKTIEKAPITAL));
		assertFalse(s.hasErrors(), s.getWarnings().toString());

		// The same accounts in an aktiebolag aren't allowed
		AnnualStatement ab = calculate(p, null);
		assertTrue(ab.hasErrors());
		assertNull(ab.getRow(SeK2Statements.LAMNAD_GOTTGORELSE));
		assertNotNull(AnnualStatementCalculator.calculate(SeK2Statements.templates(form),
				SeK2Statements.basMapping(form), p, null, AmountUnit.KR).getRow(SeK2Statements.LAMNAD_GOTTGORELSE));
	}

	/** K2 doesn't allow capitalised development (1010-1019), so it is reported. */
	@Test
	void capitalisedDevelopmentIsNotAllowed() {
		AccountingPeriodData p = period(Arrays.asList(balance("1012", 500.0), balance("2081", -500.0)));
		AnnualStatement s = calculate(p, null);
		assertEquals(StatementWarning.Code.UNMAPPED_ACCOUNT, s.getWarnings().get(0).getCode());
		assertEquals("1012", s.getWarnings().get(0).getAccountNo());
	}

}
