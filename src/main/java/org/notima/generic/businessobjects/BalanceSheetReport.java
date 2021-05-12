package org.notima.generic.businessobjects;

import java.util.List;

public class BalanceSheetReport {

	private BusinessPartner<?>		reporter;
	
	private List<BalanceSheetLine>	lines;
	
	private AccountingPeriod		reportPeriod;

	public BusinessPartner<?> getReporter() {
		return reporter;
	}

	public void setReporter(BusinessPartner<?> reporter) {
		this.reporter = reporter;
	}

	public List<BalanceSheetLine> getLines() {
		return lines;
	}

	public void setLines(List<BalanceSheetLine> lines) {
		this.lines = lines;
	}

	public AccountingPeriod getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(AccountingPeriod reportPeriod) {
		this.reportPeriod = reportPeriod;
	}
	
}
