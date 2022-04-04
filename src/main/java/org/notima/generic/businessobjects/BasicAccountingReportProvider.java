package org.notima.generic.businessobjects;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import org.notima.generic.ifacebusinessobjects.AccountingReportProvider;

public class BasicAccountingReportProvider implements AccountingReportProvider {

	@Override
	public boolean hasAccountingFor(BusinessPartner<?> bp) {
		return false;
	}

	@Override
	public AccountStatementLines getAccountStatementLines(BusinessPartner<?> bp, String accountNo, LocalDate fromDate,
			LocalDate untilDate, boolean orderByAmount) throws Exception {
		return null;
	}
	
	/**
	 * Returns a P/L report
	 * 
	 * @param bp			For the business partner.
	 * @param period				From date
	 * @param comparisonPeriod		Until date
	 * @param props					Properties to customize the report.
	 * @return				A PL report.
	 * @throws Exception	If something goes wrong
	 */
	@Override
	public ProfitLossReport getProfitLossReport(BusinessPartner<?> bp, AccountingPeriod period, AccountingPeriod comparisonPeriod, Properties props)
			throws Exception {
		return null;
	}

	
	
	/**
	 * Gets the account class for a given account
	 * 
	 * Defaults to Swedish chart of accounts.
	 * 
	 */
	@Override
	public String getAccountClass(String accountNo) {
		
		return getAccountClassSwe(accountNo);
	}

	/**
	 * Convenience class to classify swedish accounts.
	 * 
	 * @param accountNo
	 * @return
	 */
	protected String getAccountClassSwe(String accountNo) {

		if (accountNo==null || accountNo.trim().length()<4) return AccountClass.UNKNOWN_CLASS;

		// Parse the first two digits
		int classNo = 0;
		String prefix = accountNo.subSequence(0, 2).toString();
		
		try {
			classNo = Integer.parseInt(prefix);
		} catch (NumberFormatException ee) {
			return AccountClass.UNKNOWN_CLASS;
		}

		if (classNo>=10 && classNo<=13) {
			return AccountClass.ASSETS_LONG;
		}
		if (classNo>13 && classNo<20) {
			return AccountClass.ASSETS_SHORT;
		}
		if (classNo==20) 
			return AccountClass.EQUITY;
		
		if (classNo==21 || classNo==22) {
			return AccountClass.UNTAXED_RESERVES;
		}
		
		if (classNo==23)
			return AccountClass.LIABILITIES_LONG;
		
		if (classNo>=24 && classNo<=29) {
			return AccountClass.LIABILITIES_SHORT;
		}
		
		if (classNo>=30 && classNo<=39) {
			return AccountClass.REVENUE;
		}
		if (classNo>=40 && classNo<=49) {
			return AccountClass.COGS;
		}
		
		if ((classNo>=50 && classNo<=69) || classNo==79) {
			return AccountClass.OTHER_EXP;
		}
		
		if (classNo>=70 && classNo<=76) {
			return AccountClass.STAFF;
		}
		
		if (classNo==77 || classNo==78) {
			return AccountClass.DEPRECIATIONS;
		}
		
		if (classNo>=80 && classNo<=87) {
			return AccountClass.FINANCIAL;
		}
		
		if (classNo==88) {
			return AccountClass.CORPORATE_TAX_AJUSTMENTS;
		}
		
		if (classNo==89) {
			if (accountNo.startsWith("899")) {
				return AccountClass.RESULT;
			} else {
				return AccountClass.CORPORATE_TAX;
			}
		}
		
		return AccountClass.UNKNOWN_CLASS;
		
	}


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
	@Override
	public AccountingVoucher  getAccountingVoucher(BusinessPartner<?> bp, AccountingPeriod period, String series, String voucherNo) throws Exception {
		return null;
	}
	
	
	@Override
	public BalanceSheetReport getBalanceSheet(BusinessPartner<?> bp, AccountingPeriod period,
			AccountingPeriod comparisonPeriod, Properties props)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public List<AccountingVoucher> getAccountingVoucherList(BusinessPartner<?> bp, AccountingPeriod ap, String series) throws Exception {
		return null;
	}
	
	
}
