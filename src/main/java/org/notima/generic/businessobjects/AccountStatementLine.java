package org.notima.generic.businessobjects;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.github.threetenjaxb.core.LocalDateXmlAdapter;

/**
 * Represents an account statement line
 * 
 * @author Daniel Tamm
 *
 */
@Entity
public class AccountStatementLine {

	private int		lineNumber;
	private LocalDate	accountDate;
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
	
	@XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
	public LocalDate getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(LocalDate from) {
		this.accountDate = from;
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
