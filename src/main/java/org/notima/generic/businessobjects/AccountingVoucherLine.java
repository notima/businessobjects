package org.notima.generic.businessobjects;

import java.math.BigDecimal;

public class AccountingVoucherLine {

	private String 	acctNo;
	private String	acctName;
	private	String	description;
	private BigDecimal	creditAmount = BigDecimal.ZERO;
	private BigDecimal	debitAmount = BigDecimal.ZERO;
	private String	acctType;
	private String	taxKey;
	private Boolean	isService;
	
	public AccountingVoucherLine() {}
	
	/**
	 * Creates an accounting line with given amount and acctType.
	 * 
	 * @param amount		If negative, it's on the credit side. If positive on the debet side.
	 * @param acctType
	 */
	public AccountingVoucherLine(BigDecimal amount, String acctType) {
		
		if (amount!=null) {
			if (amount.signum()>0) {
				debitAmount = BigDecimal.valueOf(amount.doubleValue());
			} else {
				creditAmount = BigDecimal.valueOf(amount.negate().doubleValue());
			}
		}
		this.acctType = acctType;
		
	}
	
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public String getAcctName() {
		return acctName;
	}
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}

	public BigDecimal getBalance() {
		return debitAmount.subtract(creditAmount);
	}
	public String getAcctType() {
		return acctType;
	}
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}
	public String getTaxKey() {
		return taxKey;
	}
	public void setTaxKey(String taxKey) {
		this.taxKey = taxKey;
	}
	public Boolean getIsService() {
		return isService;
	}
	public void setIsService(Boolean isService) {
		this.isService = isService;
	}
	
	
	
	
}
