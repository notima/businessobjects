package org.notima.generic.businessobjects.tax;

/**
 * What kind of supply something is for VAT, as {@code C_TaxCategory} in iDempiere: it says
 * <em>what</em> is sold or bought, not what rate it gets. The {@link TaxEngine} works out the
 * {@link TaxRule} (rate, accounts) from the category and the circumstances of the trade.
 *
 * <p>Set on a {@link org.notima.generic.businessobjects.Product product}, and on an invoice
 * line to override the product's or for a line without a product. The keys below are the
 * categories of the EU VAT directive; a company may use others, as long as its tax rules
 * refer to them.</p>
 */
public final class TaxCategory {

	/** The standard rate (Sweden 25 %, Finland 25.5 %). */
	public static final String STANDARD = "STANDARD";
	/** The first reduced rate (Sweden 12 %, Finland 14 %). */
	public static final String REDUCED_1 = "REDUCED_1";
	/** The second reduced rate (Sweden 6 %, Finland 10 %). */
	public static final String REDUCED_2 = "REDUCED_2";
	/** Exempt from VAT (momsfri), ie healthcare, financial services. */
	public static final String EXEMPT = "EXEMPT";

	private TaxCategory() {}

}
