package org.notima.generic.ifacebusinessobjects;

import java.time.LocalDate;

import org.notima.generic.businessobjects.AccountStatementLines;
import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.ProfitLossReport;

/**
 * Provides accounting report information.
 * 
 * @author Daniel Tamm
 *
 */
public interface AccountingReportProvider {

	/**
	 * Returns the account class of the given account number (found in the source system).
	 * 
	 * @param accountNo			The account number
	 * @return					The account class
	 * @see org.notima.generic.businessobjects.AccountClass
	 */
	public String getAccountClass(String accountNo);
	
	/**
	 * Return true if this provider has accounting report for this business partner.
	 * 	
	 * @param bp		The business partner
	 * @return			True if accounting exist.
	 */
	public boolean hasAccountingFor(BusinessPartner<?> bp);

	/**
	 * Returns a P/L report
	 * 
	 * @param bp			For the business partner.
	 * @param fromDate		From date
	 * @param untilDate		Until date
	 * @return				A PL report.
	 * @throws Exception	If something goes wrong
	 */
	public ProfitLossReport getProfitLossReport(BusinessPartner<?> bp, LocalDate fromDate, LocalDate untilDate) throws Exception;
	
	/**
	 * Get account statement lines for given account number.
	 * 
	 * @param bp			The business partner
	 * @param accountNo		The actual account number
	 * @param fromDate		From date
	 * @param untilDate		Until date
	 * @param orderByAmount	Order by amount (after date)
	 * @return				Account statement lines if any.
	 * @throws		Exception if anything goes wrong.
	 */
	public AccountStatementLines getAccountStatementLines(
			BusinessPartner<?> bp, 
			String accountNo, 
			LocalDate fromDate, 
			LocalDate untilDate,
			boolean orderByAmount) throws Exception;
	
}
