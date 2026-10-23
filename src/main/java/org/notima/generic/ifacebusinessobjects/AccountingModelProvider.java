package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.AccountingFileData;
import org.notima.generic.businessobjects.AccountingPeriod;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;

/**
 * Provides the accounting model of tenants (company, chart of accounts with balances,
 * vouchers) from an accounting system, ie through its adapter, or from imported files.
 *
 * <p>The model is format-neutral ({@link AccountingFileData}), so that balances and reports
 * can be calculated the same way whatever the source, with
 * {@link org.notima.generic.businessobjects.accounting.LedgerCalculator}.</p>
 *
 * <p>In Karaf, implementations are registered as OSGi services; elsewhere in
 * {@code META-INF/services/org.notima.generic.ifacebusinessobjects.AccountingModelProvider}.</p>
 */
public interface AccountingModelProvider {

	/**
	 * @return	The system the model comes from, ie the adapter's system name ("Fortnox").
	 */
	String getSystemName();

	/**
	 * @param tenant	The tenant.
	 * @return	True if this provider can provide the accounting of {@code tenant}.
	 */
	boolean hasAccountingFor(TaxSubjectIdentifier tenant);

	/**
	 * @param tenant	The tenant.
	 * @return	The tenant's fiscal years, oldest first.
	 * @throws Exception	If the source can't be read.
	 */
	List<AccountingPeriod> listFiscalYears(TaxSubjectIdentifier tenant) throws Exception;

	/**
	 * Reads the accounting of one fiscal year.
	 *
	 * @param tenant		The tenant.
	 * @param fiscalYear	The fiscal year, one of {@link #listFiscalYears(TaxSubjectIdentifier)}.
	 * @return	The accounting, with at least the period of {@code fiscalYear}; null if there is none.
	 * @throws Exception	If the source can't be read.
	 */
	AccountingFileData getAccountingModel(TaxSubjectIdentifier tenant, AccountingPeriod fiscalYear) throws Exception;

}
