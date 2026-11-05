package org.notima.generic.businessobjects.ownership;

/**
 * A difference between two parties' records of the same loan, or a record whose
 * counterpart is missing. Found by {@link LoanVerifier}.
 */
public class LoanDiscrepancy {

	public enum Type {
		/** The counterparty's records are covered, but it has no record of the loan. */
		MISSING_COUNTERPART,
		CURRENCY,
		PRINCIPAL,
		START_DATE,
		/** The outstanding principal differs at the date of comparison. */
		OUTSTANDING,
		STATUS,
		CLOSED_DATE
	}

	private final Type				type;
	private final IntercompanyLoan	loan;
	private final IntercompanyLoan	counterpart;
	private final Object			value;
	private final Object			counterpartValue;

	public LoanDiscrepancy(Type type, IntercompanyLoan loan, IntercompanyLoan counterpart,
			Object value, Object counterpartValue) {
		this.type = type;
		this.loan = loan;
		this.counterpart = counterpart;
		this.value = value;
		this.counterpartValue = counterpartValue;
	}

	public Type getType() {
		return type;
	}

	/** @return	One party's record; the lender's when both exist. */
	public IntercompanyLoan getLoan() {
		return loan;
	}

	/** @return	The other party's record; null for {@link Type#MISSING_COUNTERPART}. */
	public IntercompanyLoan getCounterpart() {
		return counterpart;
	}

	/** @return	The differing value in {@link #getLoan()}. */
	public Object getValue() {
		return value;
	}

	/** @return	The differing value in {@link #getCounterpart()}. */
	public Object getCounterpartValue() {
		return counterpartValue;
	}

	@Override
	public String toString() {
		return type + ": " + loan + (counterpart != null ? " <> " + counterpart : "")
				+ (value != null || counterpartValue != null ? " [" + value + " / " + counterpartValue + "]" : "");
	}

}
