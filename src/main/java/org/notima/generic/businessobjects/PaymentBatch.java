package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import org.notima.generic.businessobjects.exception.CurrencyMismatchException;
import org.notima.generic.businessobjects.exception.DateMismatchException;

/**
 * 
 * Class to represent a payment batch.
 * 
 * @author Daniel Tamm
 *
 */
public class PaymentBatch {
	
	private TaxSubjectIdentifier	batchOwner;
	private BankAccountDetail		bankAccount;
	private List<Payment<?>> 		payments;
	private Payment.PaymentType		paymenType;				
	private String					source;
	private List<PayoutLine>		payoutLines;
	
	
	/**
	 * The Batch Owner is the tax subject that this file belongs to.
	 * 
	 * @return		The batch owner.
	 */
	public TaxSubjectIdentifier getBatchOwner() {
		return batchOwner;
	}
	public void setBatchOwner(TaxSubjectIdentifier batchOwner) {
		this.batchOwner = batchOwner;
	}
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
	
	public List<PayoutLine> retrievePayoutLines() throws DateMismatchException, CurrencyMismatchException {
		
		List<PayoutLine> result = new ArrayList<PayoutLine>();
		PayoutLine pl = new PayoutLine();
		result.add(pl);
		
		// Make sure we have payments to process
		if (hasPayments()) {
			for (Payment<?> p : payments) {
				
				pl.addPayment(p);
				
			}
		}
		
		payoutLines = result;
		
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
