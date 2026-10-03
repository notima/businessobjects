package org.notima.generic.ifacebusinessobjects;

import java.io.File;

import org.notima.generic.businessobjects.InvoiceFileData;

/**
 * SPI interface for plugins that can read customer (AR) and/or vendor (AP) invoices
 * from a file, for example an invoice list or an open-items report exported from an
 * accounting system.
 * <p>
 * The file may contain all invoices or only unpaid ones (see
 * {@link InvoiceFileData#isOpenItemsOnly()}). Since such files often share file
 * extensions, {@link #canImport(File)} lets the caller pick the right importer by
 * looking at the file's content.
 *
 * @see AccountingFileImporter
 */
public interface InvoiceFileImporter {

	/**
	 * @return Identifier of the system / adapter providing this importer, e.g. {@code "Fortnox"}.
	 */
	String getSystemName();

	/**
	 * @return Short human-readable format name, e.g. {@code "Fortnox AP report"}.
	 */
	String getFormatName();

	/**
	 * @return File-chooser description, e.g. {@code "Fortnox AP report (*.txt)"}.
	 */
	String getFileDescription();

	/**
	 * @return File extensions (without leading dot) handled by this importer, e.g. {@code {"txt"}}.
	 */
	String[] getFileExtensions();

	/**
	 * Checks whether the file is in the format this importer reads.
	 *
	 * @param file		The file to check.
	 * @return			True if the file can be imported by this importer.
	 */
	boolean canImport(File file);

	/**
	 * Reads the invoices in the file.
	 *
	 * @param file			The file to read.
	 * @return				The invoices found in the file.
	 * @throws Exception	If the file can't be read or parsed.
	 */
	InvoiceFileData importFile(File file) throws Exception;

}
