package org.notima.generic.ifacebusinessobjects;

import java.time.LocalDate;
import java.util.List;

import org.notima.generic.businessobjects.AccountStatementLines;
import org.notima.generic.businessobjects.AccountingPeriod;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.BalanceSheetReport;
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
	 * @param period				From date
	 * @param comparisonPeriod		Until date
	 * @param props			Properties to customize the report.
	 * @return				A PL report.
	 * @throws Exception	If something goes wrong
	 */
	public ProfitLossReport getProfitLossReport(BusinessPartner<?> bp, AccountingPeriod period, AccountingPeriod comparisonPeriod, java.util.Properties props) throws Exception;
	

	/**
	 * Returns a balance sheet
	 * 
	 * @param bp			For the business partner.
	 * @param period				From date
	 * @param comparisonPeriod		Until date
	 * @param props			Properties to customize the report. 
	 * @return				A PL report.
	 * @throws Exception	If something goes wrong
	 */
	public BalanceSheetReport getBalanceSheet(BusinessPartner<?> bp, AccountingPeriod period, AccountingPeriod comparisonPeriod, java.util.Properties props) throws Exception;
	
	/**
	 * Returns a specific accounting voucher
	 * 
	 * @param bp			For the business partner
	 * @param period		In which accounting period the voucher is found.
	 * @param series		The series
	 * @param voucherNo		The voucher number (can be non-numerical)
	 * @return				A voucher if found.
	 * @throws Exception	If something goes wrong.
	 */
	public AccountingVoucher  getAccountingVoucher(BusinessPartner<?> bp, AccountingPeriod period, String series, String voucherNo) throws Exception;
	
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

	/**
	 * Returns a list of accounting vouchers
	 * 
	 * @param bp			For the business partner
	 * @param period		In which accounting period the vouchers are to be found.
	 * @param series		The series
	 * @return				A list of vouchers
	 * @throws Exception	If something goes wrong.
	 */
	public List<AccountingVoucher> getAccountingVoucherList(BusinessPartner<?> bp, AccountingPeriod period, String series) throws Exception;
	
}
