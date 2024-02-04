package org.notima.generic.businessobjects;

public enum PaymentInterval {

	WEEKLY,
	MONTHLY,
	MONTHLY_END_OF_MONTH,
	QUARTERLY,
	QUARTERLY_END_OF_MONTH,
	SEMI_ANNUALLY,
	ANNUALLY
	;
	
	public boolean isMonthly() {
		return ordinal()==MONTHLY.ordinal() || ordinal()==MONTHLY_END_OF_MONTH.ordinal();
	}
	
}
