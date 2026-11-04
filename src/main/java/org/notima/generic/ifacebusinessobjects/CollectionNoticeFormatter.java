package org.notima.generic.ifacebusinessobjects;

import java.util.Properties;

import org.notima.generic.businessobjects.DunningEntry;

/**
 * SPI interface for plugins that format a dunning entry as a collection notice (inkassokrav).
 * <p>
 * Implementations are discovered through {@link java.util.ServiceLoader} (register the
 * class in {@code META-INF/services/org.notima.generic.ifacebusinessobjects.CollectionNoticeFormatter})
 * or as an OSGi service.
 * <p>
 * The debt is the open amount of the entry's invoices, plus late payment interest from each
 * invoice's due date until today, the entry's reminder fee and the collection fee.
 * <p>
 * Besides the properties below, the generic {@link InvoiceFormatter#OUTPUT_DIR},
 * {@link InvoiceFormatter#OUTPUT_FILENAME} and {@link InvoiceFormatter#TEMPLATE_FILE}
 * apply.
 */
public interface CollectionNoticeFormatter {

	/** Collection fee (inkassokostnad), a Double or a string such as "180". */
	public static final String COLLECTION_FEE = "CollectionFee";

	/** Yearly late payment interest (dröjsmålsränta) as a fraction, 0.08 = 8 %. A Double or a string. */
	public static final String INTEREST_RATE = "InterestRate";

	/** The payment slip / wording to use, one of the TEMPLATE_* values. */
	public static final String TEMPLATE = "CollectionNoticeTemplate";

	/** Payment slip for bankgiro. The default. */
	public static final String TEMPLATE_BANKGIRO = "bankgiro";
	/** Payment slip for plusgiro. */
	public static final String TEMPLATE_PLUSGIRO = "plusgiro";
	/** Bankgiro, stating that a samfällighet's fee is directly enforceable. */
	public static final String TEMPLATE_BANKGIRO_SAMF = "bankgiro-samf";

	/**
	 * Formats a dunning entry as a collection notice.
	 *
	 * @param dunningEntry		The debtor's invoices to collect.
	 * @param format			The format, one of {@link #getFormats()}.
	 * @param props				Properties, see the constants of this interface.
	 * @return					A reference to the created notice. Normally a file path.
	 * @throws Exception		If something goes wrong.
	 */
	public String formatCollectionNotice(DunningEntry<?,?> dunningEntry, String format, Properties props) throws Exception;

	/**
	 * @return		The formats this formatter provides.
	 */
	public String[] getFormats();

}
