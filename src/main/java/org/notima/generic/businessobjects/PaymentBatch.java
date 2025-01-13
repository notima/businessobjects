package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.Date;
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
	private Payment.PaymentType		paymentType;				
	private String					source;
	private List<PayoutLine>		payoutLines;
	private List<PayoutFee>			payoutFees;
	private String					voucherSeries;
	
	private String generalLedgerUnknownTrxAccount;
	
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
	
	public Payment.PaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Payment.PaymentType paymenType) {
		this.paymentType = paymenType;
	}
	
	public Date getFirstPaymentDate() {
		if (!hasPayments()) return null;
		return payments.get(0).getPaymentDate();
	}
	
	public Date getLastPaymentDate() {
		if (!hasPayments()) return null;
		return payments.get(payments.size()-1).getPaymentDate();
	}
	
	public boolean isDateRange() {
		if (getFirstPaymentDate()==null) return false;
		return (getFirstPaymentDate().before(getLastPaymentDate()));
	}
	
	/**
	 * Add payout fee to this payment batch.
	 * 
	 * @param fee
	 */
	public void addPayoutFee(PayoutFee fee) {
		if (fee==null) return;
		if (payoutFees==null) {
			payoutFees = new ArrayList<PayoutFee>();
		}
		payoutFees.add(fee);
	}
	
	/**
	 * Creates payout lines based on the payments in the batch (unless payout lines are already added).
	 * 
	 * @return
	 * @throws DateMismatchException
	 * @throws CurrencyMismatchException
	 */
	public List<PayoutLine> retrievePayoutLines() throws DateMismatchException, CurrencyMismatchException {
		
		List<PayoutLine> result = new ArrayList<PayoutLine>();
		PayoutLine pl = new PayoutLine();
		pl.setDescription(source);
		pl.setPayoutFees(payoutFees);
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
	
	public String getGeneralLedgerUnknownTrxAccount() {
		return generalLedgerUnknownTrxAccount;
	}
	public void setGeneralLedgerUnknownTrxAccount(String generalLedgerUnknownTrxAccount) {
		this.generalLedgerUnknownTrxAccount = generalLedgerUnknownTrxAccount;
	}
	
	public String getVoucherSeries() {
		return voucherSeries;
	}
	public void setVoucherSeries(String voucherSeries) {
		this.voucherSeries = voucherSeries;
	}
	
	
	
}
