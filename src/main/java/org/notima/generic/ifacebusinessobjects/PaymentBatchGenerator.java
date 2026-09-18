package org.notima.generic.ifacebusinessobjects;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Optional capability for a source system that can generate/fetch its own payment batch source
 * files (e.g. from an external API) for an arbitrary date range, as opposed to relying on files
 * being dropped into a directory by some other process. Implemented alongside {@link PaymentFactory}
 * / {@link PaymentBatchFactory} by adapters that support it.
 */
public interface PaymentBatchGenerator {

	String getSystemName();

	/**
	 * The most recent date already generated using the config found in configDirectory, if any.
	 * Used to compute a default start date when the caller doesn't specify one explicitly.
	 */
	Optional<LocalDate> getLastGeneratedDate(File configDirectory) throws Exception;

	/**
	 * Generates/fetches batch source files for the given date interval (inclusive), using
	 * credentials/config found in configDirectory, writing the resulting files into
	 * outputDirectory. outputDirectory may be the same directory as configDirectory, or a
	 * different one when the caller wants to redirect the output without disturbing whatever
	 * bookkeeping (e.g. a checkpoint) is tied to configDirectory.
	 *
	 * @return the files that were created or updated, in date order.
	 */
	List<File> generateBatchFiles(LocalDate fromDate, LocalDate toDate, File configDirectory, File outputDirectory) throws Exception;

}
