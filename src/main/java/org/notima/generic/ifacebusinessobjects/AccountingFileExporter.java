package org.notima.generic.ifacebusinessobjects;

import java.io.File;

import org.notima.generic.businessobjects.AccountingPeriodData;
import org.notima.generic.businessobjects.BusinessPartner;

/**
 * SPI interface for plugins that can write structured accounting data
 * (chart of accounts, balances and optionally vouchers) to a file format,
 * for example SIE.
 *
 * @see AccountingFileImporter
 */
public interface AccountingFileExporter {

	/**
	 * @return Identifier of the system / adapter providing this exporter, e.g. {@code "SIE"}.
	 */
	String getSystemName();

	/**
	 * @return Short human-readable format name, e.g. {@code "SIE4"}.
	 */
	String getFormatName();

	/**
	 * @return File-chooser description, e.g. {@code "SIE Files (*.si)"}.
	 */
	String getFileDescription();

	/**
	 * @return File extensions (without leading dot) handled by this exporter, e.g. {@code {"si"}}.
	 */
	String[] getFileExtensions();

	/**
	 * @return ISO 3166-1 alpha-2 country code this exporter is restricted to, or {@code null} for any.
	 */
	default String getCountryCode() { return null; }

	/**
	 * @return True if this exporter can write full transaction detail, not only account balances.
	 */
	boolean supportsTransactions();

	/**
	 * Writes the period to a file.
	 *
	 * @param dest					Destination file (created or overwritten).
	 * @param period				The period to export.
	 * @param company				The company the period belongs to (may be null).
	 * @param includeTransactions	True to include vouchers, false for account balances only.
	 * @throws Exception			If the export fails.
	 */
	void exportFile(File dest, AccountingPeriodData period,
					BusinessPartner<?> company, boolean includeTransactions) throws Exception;

}
