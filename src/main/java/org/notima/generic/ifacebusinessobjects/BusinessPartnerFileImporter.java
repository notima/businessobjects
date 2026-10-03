package org.notima.generic.ifacebusinessobjects;

import java.io.File;

import org.notima.generic.businessobjects.BusinessPartnerFileData;

/**
 * SPI interface for plugins that can read business partners (customers and/or suppliers)
 * from a file, for example a customer or supplier register exported from an accounting
 * system. No connection to the source system is needed.
 * <p>
 * Since such files often share file extensions, {@link #canImport(File)} lets the caller
 * pick the right importer by looking at the file's content.
 *
 * @see BusinessPartnerProvider	for reading business partners directly from a connected system.
 */
public interface BusinessPartnerFileImporter {

	/**
	 * @return Identifier of the system / adapter providing this importer, e.g. {@code "Fortnox"}.
	 */
	String getSystemName();

	/**
	 * @return Short human-readable format name, e.g. {@code "Fortnox customer register"}.
	 */
	String getFormatName();

	/**
	 * @return File-chooser description, e.g. {@code "Fortnox customer register (*.csv)"}.
	 */
	String getFileDescription();

	/**
	 * @return File extensions (without leading dot) handled by this importer, e.g. {@code {"csv"}}.
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
	 * Reads the business partners in the file.
	 *
	 * @param file			The file to read.
	 * @return				The business partners found in the file.
	 * @throws Exception	If the file can't be read or parsed.
	 */
	BusinessPartnerFileData importFile(File file) throws Exception;

}
