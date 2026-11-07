package org.notima.generic.businessobjects.tax;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Where the counterparty of a trade is, seen from the company: in the same country, in
 * another EU country, or outside the EU. {@link #ANY} is only used by rules, to match all.
 */
public enum TradeArea {

	DOMESTIC,
	EU,
	NON_EU,
	ANY;

	/** The EU member states, by ISO 3166 code; Greece also by its VAT prefix EL. */
	private static final Set<String> EU_COUNTRIES = new HashSet<String>(Arrays.asList(
			"AT", "BE", "BG", "CY", "CZ", "DE", "DK", "EE", "EL", "ES", "FI", "FR", "GR", "HR", "HU",
			"IE", "IT", "LT", "LU", "LV", "MT", "NL", "PL", "PT", "RO", "SE", "SI", "SK"));

	public static boolean isEu(String country) {
		return country != null && EU_COUNTRIES.contains(country.trim().toUpperCase());
	}

	/**
	 * The trade area of a counterparty in {@code counterpartyCountry}, for a company in
	 * {@code companyCountry}. A counterparty without a known country is taken as domestic.
	 */
	public static TradeArea of(String companyCountry, String counterpartyCountry) {
		if (counterpartyCountry == null || counterpartyCountry.trim().isEmpty()
				|| companyCountry == null || counterpartyCountry.trim().equalsIgnoreCase(companyCountry.trim())) {
			return DOMESTIC;
		}
		return isEu(counterpartyCountry) && isEu(companyCountry) ? EU : NON_EU;
	}

	/** @return	True if a rule of this area applies to a trade in {@code area}. */
	public boolean matches(TradeArea area) {
		return this == ANY || this == area;
	}

}
