package org.notima.generic.businessobjects;

import java.time.LocalDate;

public class AccountingPeriod {

	private String 		name;
	
	private	LocalDate	periodStart;
	private	LocalDate	periodEnd;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getPeriodStart() {
		return periodStart;
	}
	public void setPeriodStart(LocalDate periodStart) {
		this.periodStart = periodStart;
	}
	public LocalDate getPeriodEnd() {
		return periodEnd;
	}
	public void setPeriodEnd(LocalDate periodEnd) {
		this.periodEnd = periodEnd;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AccountingPeriod))
			return false;

		AccountingPeriod that = (AccountingPeriod)obj;

		if (that.getPeriodStart().equals(this.getPeriodStart()) && that.getPeriodEnd().equals(this.getPeriodEnd())) {
			return true;
		}
		
		return false;
	}

	
	
}
