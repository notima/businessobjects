package org.notima.generic.ifacebusinessobjects;

import java.time.LocalDate;
import java.util.List;

import org.notima.generic.businessobjects.TaxSubjectIdentifier;

public interface PaymentReportGeneric {

	public String getFullPath();
	
	public LocalDate	getSettlementDate();
	
	public TaxSubjectIdentifier getTaxSubject();
	
	public String getCurrency();
	
	public List<PaymentReportRow> getReportRows();
	
}
