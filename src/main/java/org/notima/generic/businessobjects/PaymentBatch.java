package org.notima.generic.businessobjects;

import java.util.List;

/**
 * 
 * Class to represent a payment batch.
 * 
 * @author Daniel Tamm
 *
 */
public class PaymentBatch {

	private BankAccountDetail		bankAccount;
	private List<Payment<?>> 		payments;
	private String					source;
	
	public BankAccountDetail getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(BankAccountDetail bankAccount) {
		this.bankAccount = bankAccount;
	}
	public List<Payment<?>> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment<?>> payments) {
		this.payments = payments;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	
	
}
