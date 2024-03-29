package org.notima.generic.businessobjects;


public class PaymentWriteOff {

	private String 	accountNo;
	private String	comment;
	private Double	amount;
	
	public PaymentWriteOff() {};
	
	public PaymentWriteOff(Double amount, String accountNo, String comment) {
		this.amount = amount;
		this.accountNo = accountNo;
		this.comment = comment;
	}
	
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
