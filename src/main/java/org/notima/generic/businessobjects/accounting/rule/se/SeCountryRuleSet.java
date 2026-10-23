package org.notima.generic.businessobjects.accounting.rule.se;

import java.util.List;

import org.notima.generic.businessobjects.AccountCategory;
import org.notima.generic.businessobjects.AccountElement;
import org.notima.generic.businessobjects.AccountElement.AccountElementType;
import org.notima.generic.businessobjects.accounting.rule.CountryRuleSet;

/**
 * Account classification rules for the Swedish BAS chart of accounts.
 *
 * <pre>
 *   1xxx
 *     10xx–13xx  → BalanceAcct / ASSET_LONG   (fixed assets)
 *     14xx–18xx  → BalanceAcct / ASSET_SHORT   (current assets)
 *     19xx       → BalanceAcct / ASSET_CASH    (cash and bank)
 *   2xxx
 *     20xx       → BalanceAcct / EQUITY
 *     21xx       → BalanceAcct / UNTAXED_RESERVE
 *     22xx–27xx  → BalanceAcct / LIABILITY_LONG
 *     28xx–29xx  → BalanceAcct / LIABILITY_SHORT
 *   3xxx
 *     30xx–38xx  → PLAcct / REVENUE
 *     39xx       → PLAcct / REVENUE_OTHER
 *   4xxx         → PLAcct / COGS
 *   5xxx–6xxx    → PLAcct / OPERATING_EXPENSE
 *   7xxx
 *     70xx–76xx  → PLAcct / SALARY
 *     77xx       → PLAcct / IMPAIRMENTS
 *     78xx       → PLAcct / DEPRECIATIONS
 *     79xx       → PLAcct / OTHER_OPEX
 *   8xxx
 *     80xx–83xx  → PLAcct / FINANCIAL_INCOME
 *     84xx–86xx  → PLAcct / FINANCIAL_EXPENSE
 *     87xx–89xx  → PLAcct / TAX_EXPENSE
 * </pre>
 *
 * Accounts outside these ranges (e.g. 9xxx internal cost accounts) are not
 * classified and their fields are left unchanged.
 */
public class SeCountryRuleSet implements CountryRuleSet {

	@Override
	public String getCountryCode() {
		return "SE";
	}

	@Override
	public boolean isSummaryFirst() {
		return false;
	}

	@Override
	public void classifyAccounts(List<AccountElement> chartOfAccounts) {
		if (chartOfAccounts == null) return;
		for (AccountElement ae : chartOfAccounts) {
			AccountElementType type = classifyElementType(ae.getAccountNo());
			if (type != null) ae.setAccountElementType(type);

			AccountCategory ac = classifyCategory(ae.getAccountNo());
			if (ac != null) ae.setAccountClass(ac.name());
		}
	}

	// -------------------------------------------------------------------------
	// Element-type classification (BalanceAcct / PLAcct)
	// -------------------------------------------------------------------------

	/** BalanceAcct for BAS account classes 1–2, PLAcct for 3–8, else {@code null}. */
	@Override
	public AccountElementType classifyElementType(String accountNo) {
		if (accountNo == null || accountNo.isEmpty()) return null;
		char first = accountNo.charAt(0);
		if (first == '1' || first == '2') return AccountElementType.BalanceAcct;
		if (first >= '3' && first <= '8') return AccountElementType.PLAcct;
		return null;
	}

	// -------------------------------------------------------------------------
	// Category classification
	// -------------------------------------------------------------------------

	@Override
	public AccountCategory classifyCategory(String accountNo) {
		if (accountNo == null || accountNo.isEmpty()) return null;
		switch (accountNo.charAt(0)) {
			case '1': return classifyAssets(accountNo);
			case '2': return classifyEquityAndLiabilities(accountNo);
			case '3': return classifyRevenue(accountNo);
			case '4': return AccountCategory.COGS;
			case '5': case '6': return AccountCategory.OPERATING_EXPENSE;
			case '7': return classify7xxx(accountNo);
			case '8': return classifyFinancial(accountNo);
			default:  return null;
		}
	}

	/** 30–38 → REVENUE,  39 → REVENUE_OTHER */
	private static AccountCategory classifyRevenue(String accountNo) {
		int p = twoDigitPrefix(accountNo);
		if (p < 0) return null;
		return (p <= 38) ? AccountCategory.REVENUE : AccountCategory.REVENUE_OTHER;
	}

	/** 70–76 → SALARY,  77 → IMPAIRMENTS,  78 → DEPRECIATIONS,  79 → OTHER_OPEX */
	private static AccountCategory classify7xxx(String accountNo) {
		int p = twoDigitPrefix(accountNo);
		if (p < 0) return null;
		if (p <= 76) return AccountCategory.SALARY;
		if (p == 77) return AccountCategory.IMPAIRMENTS;
		if (p == 78) return AccountCategory.DEPRECIATIONS;
		return AccountCategory.OTHER_OPEX;
	}

	/** 10–13 → ASSET_LONG,  14–18 → ASSET_SHORT,  19 → ASSET_CASH */
	private static AccountCategory classifyAssets(String accountNo) {
		int p = twoDigitPrefix(accountNo);
		if (p < 0) return null;
		if (p <= 13) return AccountCategory.ASSET_LONG;
		return (p <= 18) ? AccountCategory.ASSET_SHORT : AccountCategory.ASSET_CASH;
	}

	/** 20 → EQUITY,  21 → UNTAXED_RESERVE,  22–27 → LIABILITY_LONG,  28–29 → LIABILITY_SHORT */
	private static AccountCategory classifyEquityAndLiabilities(String accountNo) {
		int p = twoDigitPrefix(accountNo);
		if (p < 0) return null;
		if (p == 20) return AccountCategory.EQUITY;
		if (p == 21) return AccountCategory.UNTAXED_RESERVE;
		if (p <= 27) return AccountCategory.LIABILITY_LONG;
		return AccountCategory.LIABILITY_SHORT;
	}

	/** 80–83 → FINANCIAL_INCOME,  84–86 → FINANCIAL_EXPENSE,  87–89 → TAX_EXPENSE */
	private static AccountCategory classifyFinancial(String accountNo) {
		int p = twoDigitPrefix(accountNo);
		if (p < 0) return null;
		if (p <= 83) return AccountCategory.FINANCIAL_INCOME;
		if (p <= 86) return AccountCategory.FINANCIAL_EXPENSE;
		return AccountCategory.TAX_EXPENSE;
	}

	// -------------------------------------------------------------------------
	// Helper
	// -------------------------------------------------------------------------

	/** Returns the numeric value of the first two digits, or -1 if not parseable. */
	private static int twoDigitPrefix(String accountNo) {
		if (accountNo.length() < 2) return -1;
		try {
			return Integer.parseInt(accountNo.substring(0, 2));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
