package org.notima.generic.businessobjects;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Parses a general ledger account definition that can contain accounts per currency.
 *
 * Format: <code>defaultAccount,{account:CCY},{account:CCY}...</code>
 *
 * Examples:
 * <ul>
 * <li><code>1991</code> - 1991 is used for all currencies.</li>
 * <li><code>1991,{1992:EUR},{1993:USD}</code> - 1991 for the default currency, 1992 for EUR and 1993 for USD.
 * Other currencies have no account.</li>
 * </ul>
 */
public class CurrencyAccountMap {

	private final String				definition;
	private final String				defaultAccount;
	private final Map<String, String>	currencyAccounts;

	private CurrencyAccountMap(String definition, String defaultAccount, Map<String, String> currencyAccounts) {
		this.definition = definition;
		this.defaultAccount = defaultAccount;
		this.currencyAccounts = Collections.unmodifiableMap(currencyAccounts);
	}

	/**
	 * Parses an account definition.
	 *
	 * @param raw	The definition. Null or blank gives an empty map.
	 * @return	The parsed map.
	 * @throws IllegalArgumentException	If the definition is malformed.
	 */
	public static CurrencyAccountMap parse(String raw) {

		Map<String, String> currencyAccounts = new LinkedHashMap<String, String>();
		String defaultAccount = null;

		if (raw==null || raw.trim().length()==0) {
			return new CurrencyAccountMap(raw, null, currencyAccounts);
		}

		String[] tokens = raw.split(",", -1);
		for (String t : tokens) {
			String token = t.trim();
			if (token.length()==0) {
				throw new IllegalArgumentException("Empty entry in account definition [" + raw + "]");
			}
			if (token.startsWith("{")) {
				if (!token.endsWith("}")) {
					throw new IllegalArgumentException("Missing } in account definition [" + raw + "]");
				}
				String[] parts = token.substring(1, token.length()-1).split(":", -1);
				if (parts.length!=2 || parts[0].trim().length()==0 || parts[1].trim().length()==0) {
					throw new IllegalArgumentException("Entry " + token + " must be {account:currency} in account definition [" + raw + "]");
				}
				String account = parts[0].trim();
				String currency = parts[1].trim().toUpperCase();
				if (currencyAccounts.containsKey(currency)) {
					throw new IllegalArgumentException("Currency " + currency + " is defined more than once in account definition [" + raw + "]");
				}
				currencyAccounts.put(currency, account);
			} else {
				if (token.contains("{") || token.contains("}") || token.contains(":")) {
					throw new IllegalArgumentException("Malformed entry " + token + " in account definition [" + raw + "]");
				}
				if (defaultAccount!=null) {
					throw new IllegalArgumentException("More than one default account in account definition [" + raw + "]");
				}
				defaultAccount = token;
			}
		}

		return new CurrencyAccountMap(raw, defaultAccount, currencyAccounts);
	}

	/**
	 * Resolves the account for given currency.
	 *
	 * @param currency			The currency. If null or blank, the default currency is assumed.
	 * @param defaultCurrency	The default currency of the channel. Can be null.
	 * @return	The account or null if no accounts are defined at all.
	 * @throws UnknownCurrencyAccountException	If accounts are defined per currency, but not for given currency.
	 */
	public String getAccount(String currency, String defaultCurrency) {

		if (isEmpty()) return null;

		// No currency specific accounts means the same account for all currencies.
		if (currencyAccounts.isEmpty()) return defaultAccount;

		String dc = normalize(defaultCurrency);
		String c = normalize(currency);

		if (c==null) {
			if (defaultAccount==null && dc!=null && currencyAccounts.containsKey(dc)) {
				return currencyAccounts.get(dc);
			}
			if (defaultAccount==null) {
				throw new UnknownCurrencyAccountException(null, dc!=null ? dc : "(default)");
			}
			return defaultAccount;
		}

		if (currencyAccounts.containsKey(c)) {
			return currencyAccounts.get(c);
		}

		if (c.equals(dc) && defaultAccount!=null) {
			return defaultAccount;
		}

		throw new UnknownCurrencyAccountException(null, c);
	}

	private static String normalize(String currency) {
		if (currency==null || currency.trim().length()==0) return null;
		return currency.trim().toUpperCase();
	}

	public boolean isEmpty() {
		return defaultAccount==null && currencyAccounts.isEmpty();
	}

	/**
	 * @return	The account for the default currency (the entry without currency). Can be null.
	 */
	public String getDefaultAccount() {
		return defaultAccount;
	}

	/**
	 * @return	The currencies that have an explicit account.
	 */
	public Set<String> getCurrencies() {
		return currencyAccounts.keySet();
	}

	public String getDefinition() {
		return definition;
	}

}
