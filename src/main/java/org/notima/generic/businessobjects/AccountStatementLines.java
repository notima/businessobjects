package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A collection of account statement lines
 * 
 * @author Daniel Tamm
 *
 */
@Entity
@XmlRootElement(name = "AccountStatementLines")
public class AccountStatementLines {

	private List<AccountStatementLine> accountStatementLine = new ArrayList<AccountStatementLine>();

	private java.util.Date startDate;
	private java.util.Date endDate;
	
	private double		   startBalance;
	private double		   endBalance;
	
	public java.util.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public double getStartBalance() {
		return startBalance;
	}

	public void setStartBalance(double startBalance) {
		this.startBalance = startBalance;
	}

	public double getEndBalance() {
		return endBalance;
	}

	public void setEndBalance(double endBalance) {
		this.endBalance = endBalance;
	}

	/**
	 * Adds a transaction line with only date and amount
	 * @param acctDate		the transaction date
	 * @param amount		the amount
	 */
	public void addTransaction(Date acctDate, double amount) {
		AccountStatementLine line = new AccountStatementLine();
		line.setAccountDate(acctDate);
		line.setTrxAmount(amount);
		accountStatementLine.add(line);
	}
	
	@XmlElement(name = "AccountStatementLine")
	public List<AccountStatementLine> getAccountStatementLine() {
		return accountStatementLine;
	}

	public void setAccountStatementLine(List<AccountStatementLine> accountStatementLine) {
		this.accountStatementLine = accountStatementLine;
	} 
	
	
}
