package org.notima.generic.businessobjects.accounting.rule;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.notima.generic.businessobjects.AccountingPeriodData;
import org.notima.generic.businessobjects.accounting.rule.se.SeCountryRuleSet;

/**
 * Registry of {@link CountryRuleSet} implementations.
 *
 * <p>Register all rule sets once at startup; after each file import call
 * {@link #applyTo(String, Collection)} to classify the accounts in every period.</p>
 *
 * <pre>
 *   CountryRuleSetRegistry registry = CountryRuleSetRegistry.withDefaults();
 *   ...
 *   registry.applyTo(data.getCountryCode(), data.getPeriods());
 * </pre>
 */
public class CountryRuleSetRegistry {

	private final Map<String, CountryRuleSet> ruleSets = new LinkedHashMap<String, CountryRuleSet>();

	/**
	 * A registry with the built-in rule sets ({@link SeCountryRuleSet}) and those registered
	 * with {@link ServiceLoader} on the class path of this class.
	 */
	public static CountryRuleSetRegistry withDefaults() {
		CountryRuleSetRegistry registry = new CountryRuleSetRegistry();
		registry.register(new SeCountryRuleSet());
		for (CountryRuleSet ruleSet : ServiceLoader.load(CountryRuleSet.class, CountryRuleSetRegistry.class.getClassLoader())) {
			registry.register(ruleSet);
		}
		return registry;
	}

	/**
	 * Returns the rule set registered for {@code countryCode}, or {@code null} if none.
	 */
	public CountryRuleSet lookup(String countryCode) {
		return countryCode != null ? ruleSets.get(countryCode) : null;
	}

	/** Registers {@code ruleSet}; overwrites any previously registered rule set for the same country. */
	public void register(CountryRuleSet ruleSet) {
		ruleSets.put(ruleSet.getCountryCode(), ruleSet);
	}

	/**
	 * Applies the rule set of {@code countryCode} to every period's chart of accounts.
	 * Does nothing if the country code is {@code null} or no rule set is registered for it.
	 */
	public void applyTo(String countryCode, Collection<? extends AccountingPeriodData> periods) {
		CountryRuleSet rules = lookup(countryCode);
		if (rules == null || periods == null) return;
		for (AccountingPeriodData period : periods) {
			if (period.getChartOfAccounts() != null) {
				rules.classifyAccounts(period.getChartOfAccounts());
			}
		}
	}
}
