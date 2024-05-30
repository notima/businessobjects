package org.notima.generic.ifacebusinessobjects;


public interface PaymentReportRow {

	public boolean isPayment();
	
	public boolean isFee();
	
	public boolean isPayout();
	
	public boolean isDepositAdjustment();
	
	public String getCurrency();
	
	public boolean 	hasPaymentReference();
	
	public String	getPaymentReference();
	
}
