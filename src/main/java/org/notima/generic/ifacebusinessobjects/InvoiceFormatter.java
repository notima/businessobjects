package org.notima.generic.ifacebusinessobjects;

import java.util.Properties;

import org.notima.generic.businessobjects.Invoice;

/**
 * SPI interface for plugins that format an invoice into a document, for example a PDF
 * or an e-invoice file.
 * <p>
 * Implementations are discovered through {@link java.util.ServiceLoader} (register the
 * class in {@code META-INF/services/org.notima.generic.ifacebusinessobjects.InvoiceFormatter})
 * or as an OSGi service.
 *
 * @author Daniel Tamm
 */
public interface InvoiceFormatter {

	/** Property for the directory the formatted invoice is written to. */
	public static final String OUTPUT_DIR = "OutputDir";

	/** Property for the file name (without extension) of the formatted invoice. */
	public static final String OUTPUT_FILENAME = "OutputFilename";

	/**
	 * Formats an invoice
	 *
	 * @param invoice			The invoice to be formatted.
	 * @param format			One of the formats returned by {@link #getFormats()}.
	 * @param props				Properties sent to the formatter. These properties depends on the specific formatter.
	 * @return					A reference to the created document. Normally a file path.
	 * @throws					Exception if something goes wrong.
	 */
	public String formatInvoice(Invoice<?> invoice, String format, Properties props) throws Exception;

	/**
	 * What formats the formatter provides.
	 *
	 * @return		Return an array of strings representing the format this formatter provides.
	 */
	public String[] getFormats();

}
