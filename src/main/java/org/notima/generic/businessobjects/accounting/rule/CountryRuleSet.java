package org.notima.generic.businessobjects.accounting.rule;

import org.notima.generic.businessobjects.settlement.PostingAccounts;
import org.notima.generic.businessobjects.tax.TaxRule;
import org.notima.generic.businessobjects.accounting.statement.StatementMapping;
import org.notima.generic.businessobjects.accounting.statement.StatementTemplate;
import java.util.ArrayList;
import java.util.Collections;
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

	/**
	 * @return	How the country's chart of accounts is placed on the lines of the annual report's
	 * 			statements, or null if the country has no mapping.
	 */
	default StatementMapping getStatementMapping() {
		return null;
	}

	/**
	 * @param orgNo	The company's organisation number, which may decide its company form; null
	 * 				for the most common form.
	 * @return	How the country's chart of accounts is placed on the lines of the company's
	 * 			statements, or null if the country has no mapping.
	 */
	default StatementMapping getStatementMapping(String orgNo) {
		return getStatementMapping();
	}

	/**
	 * @param orgNo	The company's organisation number, which may decide its company form; null
	 * 				for the most common form.
	 * @return	The statements of the company's annual report (e.g. income statement and balance
	 * 			sheet), whose lines {@link #getStatementMapping(String)} places the accounts on;
	 * 			empty if the country has none.
	 */
	default List<StatementTemplate> getStatementTemplates(String orgNo) {
		return Collections.emptyList();
	}

	/**
	 * @return	The country's usual accounts for posting invoices, payments and allocations,
	 * 			ie from its standard chart of accounts; empty if the country has none.
	 */
	default PostingAccounts getDefaultPostingAccounts() {
		return new PostingAccounts();
	}

	/**
	 * @return	The country's usual tax rules, for the
	 * 			{@link org.notima.generic.businessobjects.tax.TaxEngine tax engine}; empty if the
	 * 			country has none. A new list each time, so the caller can change it.
	 */
	default List<TaxRule> getDefaultTaxRules() {
		return new ArrayList<TaxRule>();
	}

}
