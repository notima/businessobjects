package org.notima.generic.businessobjects;

import java.time.LocalDate;

/**
 * Represents a named interest rate valid over a date range.
 * <p>
 * Typical examples are central-bank reference rates, late-payment rates, or
 * any other rate used as input to interest calculations.
 *
 * @see org.notima.generic.businessobjects.util.InterestCalculator
 */
public class InterestRate {

	/**
	 * Provider-specific key that identifies this rate (e.g. {@code "SE_REF_RATE"},
	 * {@code "EURIBOR_3M"}).
	 */
	private String		rateKey;

	/** Human-readable name of the rate. */
	private String		name;

	/**
	 * The interest rate expressed as a percentage (e.g. {@code 3.5} means 3.5 %).
	 */
	private double		rate;

	/** First date on which this rate is valid (inclusive). */
	private LocalDate	validFrom;

	/** Last date on which this rate is valid (inclusive), or {@code null} if open-ended. */
	private LocalDate	validUntil;

	/**
	 * ISO 4217 currency code if this rate is currency-specific, or {@code null}
	 * when the rate applies regardless of currency.
	 */
	private String		currency;

	// ------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------

	public InterestRate() {}

	public InterestRate(String rateKey, double rate) {
		this.rateKey = rateKey;
		this.rate    = rate;
	}

	// ------------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------------

	public String getRateKey() {
		return rateKey;
	}

	public InterestRate setRateKey(String rateKey) {
		this.rateKey = rateKey;
		return this;
	}

	public String getName() {
		return name;
	}

	public InterestRate setName(String name) {
		this.name = name;
		return this;
	}

	public double getRate() {
		return rate;
	}

	public InterestRate setRate(double rate) {
		this.rate = rate;
		return this;
	}

	public LocalDate getValidFrom() {
		return validFrom;
	}

	public InterestRate setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
		return this;
	}

	public LocalDate getValidUntil() {
		return validUntil;
	}

	public InterestRate setValidUntil(LocalDate validUntil) {
		this.validUntil = validUntil;
		return this;
	}

	public String getCurrency() {
		return currency;
	}

	public InterestRate setCurrency(String currency) {
		this.currency = currency;
		return this;
	}

	@Override
	public String toString() {
		return (rateKey != null ? rateKey : "?") + "@" + rate + "%"
				+ (validFrom != null ? " from=" + validFrom : "")
				+ (validUntil != null ? " until=" + validUntil : "");
	}
}
