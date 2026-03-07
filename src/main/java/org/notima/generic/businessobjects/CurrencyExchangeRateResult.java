package org.notima.generic.businessobjects;

import java.time.LocalDateTime;

/**
 * Wraps a {@link CurrencyExchangeRate} with provenance metadata returned by a
 * {@link org.notima.generic.ifacebusinessobjects.FinancialInfoProvider}.
 * <p>
 * Using a result wrapper rather than the bare rate object lets providers attach
 * additional context (source system, retrieval timestamp, etc.) without
 * modifying {@link CurrencyExchangeRate} itself.
 */
public class CurrencyExchangeRateResult {

	/** The actual exchange rate. */
	private CurrencyExchangeRate	exchangeRate;

	/** Name or identifier of the data source (e.g. {@code "ECB"}, {@code "Riksbanken"}). */
	private String					source;

	/** When this result was fetched from the provider. */
	private LocalDateTime			retrievedAt;

	// ------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------

	public CurrencyExchangeRateResult() {}

	public CurrencyExchangeRateResult(CurrencyExchangeRate exchangeRate) {
		this.exchangeRate = exchangeRate;
		this.retrievedAt  = LocalDateTime.now();
	}

	// ------------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------------

	public CurrencyExchangeRate getExchangeRate() {
		return exchangeRate;
	}

	public CurrencyExchangeRateResult setExchangeRate(CurrencyExchangeRate exchangeRate) {
		this.exchangeRate = exchangeRate;
		return this;
	}

	public String getSource() {
		return source;
	}

	public CurrencyExchangeRateResult setSource(String source) {
		this.source = source;
		return this;
	}

	public LocalDateTime getRetrievedAt() {
		return retrievedAt;
	}

	public CurrencyExchangeRateResult setRetrievedAt(LocalDateTime retrievedAt) {
		this.retrievedAt = retrievedAt;
		return this;
	}

	@Override
	public String toString() {
		return (exchangeRate != null ? exchangeRate.toString() : "null")
				+ (source != null ? " [" + source + "]" : "");
	}
}
