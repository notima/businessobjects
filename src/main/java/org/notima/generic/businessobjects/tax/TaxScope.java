package org.notima.generic.businessobjects.tax;

/** Whether a {@link TaxRule} applies to sales, purchases or both (iDempiere's SOPOType). */
public enum TaxScope {

	SALES,
	PURCHASE,
	BOTH;

	/** @return	True if a rule of this scope applies to a sale ({@code sales}) or a purchase. */
	public boolean appliesTo(boolean sales) {
		return this == BOTH || (sales ? this == SALES : this == PURCHASE);
	}

}
