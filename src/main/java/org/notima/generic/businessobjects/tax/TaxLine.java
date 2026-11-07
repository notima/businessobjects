package org.notima.generic.businessobjects.tax;

import java.math.BigDecimal;

/**
 * One VAT posting of a {@link TaxRule}: an amount (debit if positive, credit if negative), the
 * account and its {@link org.notima.generic.businessobjects.AccountingType role}.
 */
public class TaxLine {

	private final String		role;
	private final String		acctNo;
	private final BigDecimal	amount;
	private final TaxRule		rule;

	public TaxLine(String role, String acctNo, BigDecimal amount, TaxRule rule) {
		this.role = role;
		this.acctNo = acctNo;
		this.amount = amount;
		this.rule = rule;
	}

	public String getRole() { return role; }
	public String getAcctNo() { return acctNo; }
	public BigDecimal getAmount() { return amount; }
	public TaxRule getRule() { return rule; }

}
