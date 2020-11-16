package org.notima.generic.businessobjects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.github.threetenjaxb.core.LocalDateXmlAdapter;

/**
 * A collection of account statement lines
 * 
 * @author Daniel Tamm
 *
 */
@XmlRootElement(name = "AccountStatementLines")
public class AccountStatementLines {

	private List<AccountStatementLine> accountStatementLine = new ArrayList<AccountStatementLine>();

	private java.time.LocalDate startDate;
	private java.time.LocalDate endDate;
	
	private double		   startBalance;
	private double		   endBalance;
	
	
	public void sortStatementLinesByDate() {
		
	}
	
	public java.time.LocalDate getStartDate() {
		return startDate;
	}

	@XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
	public void setStartDate(java.time.LocalDate startDate) {
		this.startDate = startDate;
	}

	public java.time.LocalDate getEndDate() {
		return endDate;
	}

	@XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
	public void setEndDate(java.time.LocalDate endDate) {
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
	 * @param from		the transaction date
	 * @param amount		the amount
	 */
	public void addTransaction(LocalDate from, double amount) {
		AccountStatementLine line = new AccountStatementLine();
		line.setAccountDate(from);
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
