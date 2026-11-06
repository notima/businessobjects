package org.notima.generic.businessobjects.accounting.statement;

import java.util.HashMap;
import java.util.Map;

/**
 * Places accounts on the lines of statement templates. A country's mapping comes from its
 * {@link org.notima.generic.businessobjects.accounting.rule.CountryRuleSet}; a company with
 * its own use of some accounts adds overrides with {@link #withOverrides(Map)}.
 */
public interface StatementMapping {

	/**
	 * @param accountNo	The account.
	 * @return	Where the account goes, {@link AccountPlacement#CLOSING} for accounts that
	 * 			close the year, or null if the account isn't mapped.
	 */
	AccountPlacement place(String accountNo);

	/**
	 * @param overrides	Account number to line id. An override puts the account on the line
	 * 					whatever its sign.
	 * @return	A mapping that uses the overrides before this mapping.
	 */
	default StatementMapping withOverrides(Map<String, String> overrides) {
		if (overrides == null || overrides.isEmpty()) return this;
		final Map<String, String> copy = new HashMap<String, String>(overrides);
		final StatementMapping base = this;
		return new StatementMapping() {
			@Override
			public AccountPlacement place(String accountNo) {
				String lineId = copy.get(accountNo);
				return lineId != null ? AccountPlacement.on(lineId) : base.place(accountNo);
			}
		};
	}

}
