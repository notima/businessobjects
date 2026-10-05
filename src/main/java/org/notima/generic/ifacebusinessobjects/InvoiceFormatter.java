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
	 * Property for a template file (e.g. a report design) to format the invoice with,
	 * instead of the formatter's default. Ignored by formatters that don't use templates.
	 */
	public static final String TEMPLATE_FILE = "TemplateFile";

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

	/**
	 * Built-in templates of this formatter, e.g. different layouts of the payment slip.
	 * Any of them can be passed as {@link #TEMPLATE_FILE} instead of a file path.
	 *
	 * @return		Names of the built-in templates; empty if the formatter has none.
	 */
	public default String[] getTemplates() {
		return new String[0];
	}

}
