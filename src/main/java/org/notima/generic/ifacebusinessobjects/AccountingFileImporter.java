package org.notima.generic.ifacebusinessobjects;

import java.io.File;

import org.notima.generic.businessobjects.AccountingFileData;

/**
 * SPI interface for plugins that can read structured accounting data
 * (company, chart of accounts, balances and vouchers) from a file format,
 * for example SIE.
 *
 * @see AccountingFileExporter
 */
public interface AccountingFileImporter {

	/**
	 * @return Identifier of the system / adapter providing this importer, e.g. {@code "SIE"}.
	 */
	String getSystemName();

	/**
	 * @return Short human-readable format name, e.g. {@code "SIE4"}.
	 */
	String getFormatName();

	/**
	 * @return File-chooser description, e.g. {@code "SIE Files (*.si, *.se, *.sie)"}.
	 */
	String getFileDescription();

	/**
	 * @return File extensions (without leading dot) handled by this importer,
	 * 			e.g. {@code {"si", "se", "sie"}}.
	 */
	String[] getFileExtensions();

	/**
	 * @return ISO 3166-1 alpha-2 country code of the accounting convention used by
	 * 			this format, or {@code null} if no specific country applies.
	 */
	default String getCountryCode() { return null; }

	/**
	 * Parses the file and returns its accounting data.
	 *
	 * @param file		The file to read.
	 * @return			The accounting data found in the file.
	 * @throws Exception	If the file can't be read or parsed.
	 */
	AccountingFileData importFile(File file) throws Exception;

}
