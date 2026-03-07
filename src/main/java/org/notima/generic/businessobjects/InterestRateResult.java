package org.notima.generic.businessobjects;

import java.time.LocalDateTime;

/**
 * Wraps an {@link InterestRate} with provenance metadata returned by a
 * {@link org.notima.generic.ifacebusinessobjects.FinancialInfoProvider}.
 * <p>
 * Using a result wrapper rather than the bare rate object lets providers attach
 * additional context (source system, retrieval timestamp, etc.) without
 * modifying {@link InterestRate} itself.
 */
public class InterestRateResult {

	/** The actual interest rate. */
	private InterestRate	interestRate;

	/** Name or identifier of the data source (e.g. {@code "Riksbanken"}, {@code "ECB"}). */
	private String			source;

	/** When this result was fetched from the provider. */
	private LocalDateTime	retrievedAt;

	// ------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------

	public InterestRateResult() {}

	public InterestRateResult(InterestRate interestRate) {
		this.interestRate = interestRate;
		this.retrievedAt  = LocalDateTime.now();
	}

	// ------------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------------

	public InterestRate getInterestRate() {
		return interestRate;
	}

	public InterestRateResult setInterestRate(InterestRate interestRate) {
		this.interestRate = interestRate;
		return this;
	}

	public String getSource() {
		return source;
	}

	public InterestRateResult setSource(String source) {
		this.source = source;
		return this;
	}

	public LocalDateTime getRetrievedAt() {
		return retrievedAt;
	}

	public InterestRateResult setRetrievedAt(LocalDateTime retrievedAt) {
		this.retrievedAt = retrievedAt;
		return this;
	}

	@Override
	public String toString() {
		return (interestRate != null ? interestRate.toString() : "null")
				+ (source != null ? " [" + source + "]" : "");
	}
}
