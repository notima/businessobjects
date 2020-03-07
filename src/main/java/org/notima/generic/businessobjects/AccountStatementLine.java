package org.notima.generic.businessobjects;

import javax.persistence.Entity;

/**
 * Represents an account statement line
 * 
 * @author Daniel Tamm
 *
 */
@Entity
public class AccountStatementLine {

	private int		lineNumber;
	private java.util.Date	accountDate;
	private String	thisAccountNo;
	private String	otherAccountNo;
	private String	description;
	private double	trxAmount;
	private double	balanceAmount;
	private Payment<?>	payment;
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public java.util.Date getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(java.util.Date accountDate) {
		this.accountDate = accountDate;
	}
	public String getThisAccountNo() {
		return thisAccountNo;
	}
	public void setThisAccountNo(String thisAccountNo) {
		this.thisAccountNo = thisAccountNo;
	}
	public String getOtherAccountNo() {
		return otherAccountNo;
	}
	public void setOtherAccountNo(String otherAccountNo) {
		this.otherAccountNo = otherAccountNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getTrxAmount() {
		return trxAmount;
	}
	public void setTrxAmount(double trxAmount) {
		this.trxAmount = trxAmount;
	}
	public double getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public Payment<?> getPayment() {
		return payment;
	}
	public void setPayment(Payment<?> payment) {
		this.payment = payment;
	}
	
	
	
}
