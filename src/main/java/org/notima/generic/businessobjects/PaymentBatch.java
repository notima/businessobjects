package org.notima.generic.businessobjects;

import java.util.ArrayList;
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
	private Payment.PaymentType		paymenType;				
	private String					source;
	private List<PayoutLine>		payoutLines;
	
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
	
	public Payment.PaymentType getPaymenType() {
		return paymenType;
	}
	public void setPaymenType(Payment.PaymentType paymenType) {
		this.paymenType = paymenType;
	}
	
	public List<PayoutLine> retrievePayoutLines() {
		return payoutLines;
	}
	
	public void addPayment(Payment<?> payment) {
		if (payments==null)
			payments = new ArrayList<Payment<?>>();
		payments.add(payment);
	}
	
	public boolean hasPayments() {
		return payments!=null && payments.size()>0;
	}
	
	public boolean hasPayouts() {
		return payoutLines!=null && payoutLines.size()>0;
	}
	
	public boolean isEmpty() {
		return (!hasPayments() && !hasPayouts());
	}
	
	
}
