package org.notima.generic.businessobjects;

import java.math.BigDecimal;

/**
 * Represents a column in a profit/loss statement
 * 
 * @author Daniel Tamm
 *
 */
public class ProfitLossColumn {


	private AccountingPeriod	period;
	private BigDecimal	amount;

	public AccountingPeriod getPeriod() {
		return period;
	}

	public void setPeriod(AccountingPeriod period) {
		this.period = period;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	
}
