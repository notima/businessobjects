package org.notima.generic.businessobjects.settlement;

/**
 * Status of a settlement document ({@link org.notima.generic.businessobjects.Payment} or
 * {@link PaymentAllocation}), as in iDempiere.
 *
 * <p>Documents are never changed once completed: a completed document is undone by a
 * reversal, a new document with negated amounts. Both the original and the reversal are
 * then {@link #REVERSED}, and both still count, so they net to zero.</p>
 */
public enum DocumentStatus {

	/** Being prepared; doesn't count in any balance and isn't posted. */
	DRAFT,
	/** Done; counts in balances and is posted. */
	COMPLETED,
	/** Reversed, or the reversal of another document. Counts, netting with its counterpart. */
	REVERSED;

	/** @return	True if a document with this status counts in balances (null is taken as completed). */
	public static boolean counts(DocumentStatus status) {
		return status != DRAFT;
	}

}
