package org.notima.generic.businessobjects;

import java.util.List;

public class ProfitLossReport {

	private BusinessPartner<?>		reporter;
	
	private List<ProfitLossLine>	lines;
	
	private AccountingPeriod		reportPeriod;

	public BusinessPartner<?> getReporter() {
		return reporter;
	}

	public void setReporter(BusinessPartner<?> reporter) {
		this.reporter = reporter;
	}

	public List<ProfitLossLine> getLines() {
		return lines;
	}

	public void setLines(List<ProfitLossLine> lines) {
		this.lines = lines;
	}

	public AccountingPeriod getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(AccountingPeriod reportPeriod) {
		this.reportPeriod = reportPeriod;
	}
	
	
	
}
