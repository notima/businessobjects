package org.notima.generic.ifacebusinessobjects;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.notima.generic.businessobjects.CurrencyExchangeRateResult;
import org.notima.generic.businessobjects.InterestRateResult;

/**
 * SPI interface for plugins that can supply financial market data: currency
 * exchange rates and interest rates.
 * <p>
 * Implementations are typically thin adapters over a data provider (e.g. a
 * central bank REST API, a financial data feed, or a static configuration).
 * Results are returned as result wrapper objects ({@link CurrencyExchangeRateResult},
 * {@link InterestRateResult}) so that provenance metadata can travel alongside
 * the rate without modifying the core domain objects.
 *
 * @see TaxRateProvider
 */
public interface FinancialInfoProvider {

	/**
	 * @return Identifier of this provider (e.g. {@code "ECB"}, {@code "Riksbanken"}).
	 */
	String getSystemName();

	// ------------------------------------------------------------------
	// Discovery
	// ------------------------------------------------------------------

	/**
	 * Returns the base currencies this provider can supply exchange rates for,
	 * mapped to a human-readable description.
	 * <p>
	 * Example entry: {@code "EUR" → "Euro (European Central Bank)"}.
	 * <p>
	 * Callers should use this to discover valid {@code fromCurrency} arguments
	 * before calling {@link #getExchangeRate} or {@link #getExchangeRates}.
	 *
	 * @return A map of ISO 4217 currency code → description;
	 *         may be empty but never {@code null}.
	 */
	Map<String, String> getAvailableExchangeRateCurrencies();

	/**
	 * Returns the interest rate keys this provider publishes, mapped to a
	 * human-readable description.
	 * <p>
	 * Example entry: {@code "SE_REF_RATE" → "Swedish reference rate (Riksbanken)"}.
	 * <p>
	 * Callers should use this to discover valid {@code rateKey} arguments before
	 * calling {@link #getInterestRate} or {@link #getInterestRates}.
	 *
	 * @return A map of rate key → description;
	 *         may be empty but never {@code null}.
	 */
	Map<String, String> getAvailableInterestRateKeys();

	// ------------------------------------------------------------------
	// Currency exchange rates
	// ------------------------------------------------------------------

	/**
	 * Returns the exchange rate from {@code fromCurrency} to {@code toCurrency}
	 * valid on {@code date}.
	 *
	 * @param fromCurrency	ISO 4217 source currency code (e.g. {@code "USD"}).
	 * @param toCurrency	ISO 4217 target currency code (e.g. {@code "SEK"}).
	 * @param date			The date for which the rate is requested.
	 * @return				A result wrapping the matching rate.
	 * @throws Exception	If the rate cannot be retrieved.
	 */
	CurrencyExchangeRateResult getExchangeRate(String fromCurrency, String toCurrency, LocalDate date) throws Exception;

	/**
	 * Returns all exchange rates with {@code baseCurrency} as the source currency
	 * valid on {@code date}.
	 *
	 * @param baseCurrency	ISO 4217 base currency code.
	 * @param date			The date for which the rates are requested.
	 * @return				A list of results; may be empty but never {@code null}.
	 * @throws Exception	If the rates cannot be retrieved.
	 */
	List<CurrencyExchangeRateResult> getExchangeRates(String baseCurrency, LocalDate date) throws Exception;

	// ------------------------------------------------------------------
	// Interest rates
	// ------------------------------------------------------------------

	/**
	 * Returns the interest rate identified by {@code rateKey} valid on {@code date}.
	 * <p>
	 * The set of supported keys is provider-specific (e.g. {@code "SE_REF_RATE"},
	 * {@code "EURIBOR_3M"}).
	 *
	 * @param rateKey	Provider-specific key for the desired rate.
	 * @param date		The date for which the rate is requested.
	 * @return			A result wrapping the matching rate.
	 * @throws Exception	If the rate cannot be retrieved.
	 */
	InterestRateResult getInterestRate(String rateKey, LocalDate date) throws Exception;

	/**
	 * Returns all interest rates this provider publishes that are valid on
	 * {@code date}.
	 *
	 * @param date		The date for which the rates are requested.
	 * @return			A list of results; may be empty but never {@code null}.
	 * @throws Exception	If the rates cannot be retrieved.
	 */
	List<InterestRateResult> getInterestRates(LocalDate date) throws Exception;
}
