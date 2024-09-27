package org.notima.generic.businessobjects;

/**
 * Fee that isn't tied to a specific payment. For fees tied to a specific payment, use "PaymentWriteOff".
 * 
 */
public class PayoutFee {

	private	String	currency;
	private Double	feeAmount = Double.valueOf(0);
	private Double	vatAmount = Double.valueOf(0);
	private String	taxKey;
	private String	account;
	private Double	amount = Double.valueOf(0);
	private String	description;
	private boolean	depositAdjustment = false;
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}
	public Double getVatAmount() {
		return vatAmount;
	}
	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
	}
	public String getTaxKey() {
		return taxKey;
	}
	public void setTaxKey(String vatCode) {
		this.taxKey = vatCode;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * 
	 * @return	Fee and VAT total
	 */
	public Double getFeeAndVatTotal() {
		return vatAmount + feeAmount;
	}
	
	/**
	 * This is the amount that the fee is based on (if any).
	 * Is this is a deposit / balance adjustment, this is the amount.
	 * 
	 * @return
	 */
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/*
	 * True if this is a deposit adjustment.
	 */
	public boolean isDepositAdjustment() {
		return depositAdjustment;
	}
	public void setDepositAdjustment(boolean depositAdjustment) {
		this.depositAdjustment = depositAdjustment;
	}
	
	
	
}
