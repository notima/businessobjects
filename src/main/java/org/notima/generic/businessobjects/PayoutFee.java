package org.notima.generic.businessobjects;

/**
 * Fee that isn't tied to a specific payment. For fees tied to a specific payment, use "PaymentWriteOff".
 * 
 */
public class PayoutFee {

	private	String	currency;
	private String	feeAmount;
	private String	vatAmount;
	private String	taxKey;
	private String	account;
	private Double	amount;
	private String	description;
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}
	public String getVatAmount() {
		return vatAmount;
	}
	public void setVatAmount(String vatAmount) {
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
	
}
