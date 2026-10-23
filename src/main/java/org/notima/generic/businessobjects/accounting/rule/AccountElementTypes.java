package org.notima.generic.businessobjects.accounting.rule;

import java.util.Collection;

import org.notima.generic.businessobjects.AccountCategory;
import org.notima.generic.businessobjects.AccountElement;
import org.notima.generic.businessobjects.AccountElement.AccountElementType;
import org.notima.generic.businessobjects.AccountingPeriodData;

/**
 * Decides whether an account is a balance sheet or a P&amp;L account
 * ({@link AccountElement#getAccountElementType()}), which the balance and P&amp;L reports go
 * by: accounts without a type are left out of both.
 *
 * <p>The type follows the account's {@link AccountCategory}; for an account without a
 * category (or {@link AccountCategory#OTHER}) it follows the account number, as the
 * {@link CountryRuleSet rules of the country} tell.</p>
 */
public final class AccountElementTypes {

	private AccountElementTypes() {}

	/**
	 * The type {@code ae} should have, or {@code null} if it can't be told.
	 *
	 * @param rules	The rules of the chart's country, for an account without category; null if unknown.
	 */
	public static AccountElementType of(AccountElement ae, CountryRuleSet rules) {
		AccountCategory ac = AccountCategory.parse(ae.getAccountClass());
		AccountElementType type = ac != null ? ac.getElementType() : null;
		if (type != null) return type;
		return rules != null ? rules.classifyElementType(ae.getAccountNo()) : null;
	}

	/**
	 * Sets the type of {@code ae} from its category / number, when one can be told.
	 *
	 * @param rules	The rules of the chart's country, for an account without category; null if unknown.
	 */
	public static void apply(AccountElement ae, CountryRuleSet rules) {
		AccountElementType type = of(ae, rules);
		if (type != null) ae.setAccountElementType(type);
	}

	/**
	 * Gives every account without a type in {@code periods} its type.
	 *
	 * @param rules	The rules of the chart's country, for accounts without category; null if unknown.
	 * @return the number of accounts that got a type
	 */
	public static int fillMissing(Collection<? extends AccountingPeriodData> periods, CountryRuleSet rules) {
		if (periods == null) return 0;
		int filled = 0;
		for (AccountingPeriodData period : periods) {
			if (period.getChartOfAccounts() == null) continue;
			for (AccountElement ae : period.getChartOfAccounts()) {
				if (ae.getAccountElementType() != null) continue;
				apply(ae, rules);
				if (ae.getAccountElementType() != null) filled++;
			}
		}
		return filled;
	}

}
