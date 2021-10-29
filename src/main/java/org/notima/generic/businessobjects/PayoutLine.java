package org.notima.generic.businessobjects;

import java.time.LocalDate;

/**
 * This line describes a payout and if applicable associated accounting accounts and amounts.
 * 
 * @author Daniel Tamm
 *
 */
public class PayoutLine {

	private String	paymentType;
	private String	paymentTypeReference;
	private LocalDate	acctDate;
	
	private int		trxCount;
	private Double	paidByCustomer;
	private Double	feeAmount;
	private Double	taxAmount;
	private String	taxKey;
	private Double	taxRate;
	private Double	paidOut;
	private Double	openingBalance;
	private Double	endingBalance;

	private String	taxAcctNo;
	private String	feeAcctNo;
	private String	receivablesAcctNo;
	private String	paidOutAcctNo;
	
	private String	currency;
	
	private boolean	includedInOtherPayout = false;
	
	private String	description;
	
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentTypeReference() {
		return paymentTypeReference;
	}
	public void setPaymentTypeReference(String paymentTypeReference) {
		this.paymentTypeReference = paymentTypeReference;
	}
	public LocalDate getAcctDate() {
		return acctDate;
	}
	public void setAcctDate(LocalDate acctDate) {
		this.acctDate = acctDate;
	}
	public boolean isIncludedInOtherPayout() {
		return includedInOtherPayout;
	}
	public void setIncludedInOtherPayout(boolean includedInOtherPayout) {
		this.includedInOtherPayout = includedInOtherPayout;
	}
	public int getTrxCount() {
		return trxCount;
	}
	public void setTrxCount(int trxCount) {
		this.trxCount = trxCount;
	}
	public Double getPaidByCustomer() {
		return paidByCustomer;
	}
	public void setPaidByCustomer(Double paidByCustomer) {
		this.paidByCustomer = paidByCustomer;
	}
	public Double getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public String getTaxKey() {
		return taxKey;
	}
	public void setTaxKey(String taxKey) {
		this.taxKey = taxKey;
	}
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	public Double getPaidOut() {
		return paidOut;
	}
	public void setPaidOut(Double paidOut) {
		this.paidOut = paidOut;
	}
	public String getTaxAcctNo() {
		return taxAcctNo;
	}
	public void setTaxAcctNo(String taxAcctNo) {
		this.taxAcctNo = taxAcctNo;
	}
	public String getFeeAcctNo() {
		return feeAcctNo;
	}
	public void setFeeAcctNo(String feeAcctNo) {
		this.feeAcctNo = feeAcctNo;
	}
	public String getReceivablesAcctNo() {
		return receivablesAcctNo;
	}
	public void setReceivablesAcctNo(String receivablesAcctNo) {
		this.receivablesAcctNo = receivablesAcctNo;
	}
	public String getPaidOutAcctNo() {
		return paidOutAcctNo;
	}
	public void setPaidOutAcctNo(String paidOutAcctNo) {
		this.paidOutAcctNo = paidOutAcctNo;
	}
	
	public Double getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}
	public Double getEndingBalance() {
		return endingBalance;
	}
	public void setEndingBalance(Double endingBalance) {
		this.endingBalance = endingBalance;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}
