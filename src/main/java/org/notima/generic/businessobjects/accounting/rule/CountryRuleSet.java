package org.notima.generic.businessobjects.accounting.rule;

import java.util.List;

import org.notima.generic.businessobjects.AccountCategory;
import org.notima.generic.businessobjects.AccountElement;
import org.notima.generic.businessobjects.AccountElement.AccountElementType;

/**
 * Encapsulates the accounting rules that apply to a specific country.
 *
 * <p>Currently the primary responsibility is classifying each {@link AccountElement}
 * in a chart of accounts: its {@link AccountCategory} and whether it's a balance-sheet or a
 * P&amp;L account. This is the only place where account numbers are interpreted; reports
 * and queries go by the category, so that they work with any country's chart of accounts.
 * Additional country-specific rules (VAT rates, reporting categories, …) can be
 * added as new methods in the future.</p>
 *
 * <p>Implementations for other countries can be registered in
 * {@code META-INF/services/org.notima.generic.businessobjects.accounting.rule.CountryRuleSet},
 * see {@link CountryRuleSetRegistry#withDefaults()}.</p>
 */
public interface CountryRuleSet {

	/** ISO 3166-1 alpha-2 country code this rule set applies to, e.g. {@code "SE"}. */
	String getCountryCode();

	/**
	 * Classifies each account in {@code chartOfAccounts} by setting its
	 * {@code accountElementType} field ({@code BalanceAcct} or {@code PLAcct}).
	 * Accounts that cannot be classified are left unchanged.
	 *
	 * @param chartOfAccounts the list of accounts to classify (modified in place)
	 */
	void classifyAccounts(List<AccountElement> chartOfAccounts);

	/**
	 * The category an account number has in this country's chart of accounts.
	 *
	 * @return	The category, or {@code null} if it can't be told from the number.
	 */
	default AccountCategory classifyCategory(String accountNo) {
		return null;
	}

	/**
	 * Whether an account number is a balance-sheet or a P&amp;L account in this country's
	 * chart of accounts. By default the type of its {@link #classifyCategory(String) category}.
	 *
	 * @return	The type, or {@code null} if it can't be told from the number.
	 */
	default AccountElementType classifyElementType(String accountNo) {
		AccountCategory category = classifyCategory(accountNo);
		return category != null ? category.getElementType() : null;
	}

	/**
	 * Returns {@code true} if section-summary (CLASS) rows should be placed
	 * <em>before</em> their individual account rows in report views,
	 * or {@code false} if they should appear <em>after</em> (i.e. as a subtotal).
	 *
	 * <p>Defaults to {@code true}. Override in country-specific rule sets where
	 * the local reporting convention puts the summary at the bottom of each section.</p>
	 */
	default boolean isSummaryFirst() {
		return true;
	}
}
