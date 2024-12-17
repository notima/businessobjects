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
	private String	taxDomicile;
	private String			costCenter;
	private String			projectCode;
	private Boolean	isService;
	private Boolean deleted = Boolean.FALSE;
	
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

	/**
	 * Reverse the amounts
	 */
	public void reverse() {
		BigDecimal oldDebitAmount = BigDecimal.valueOf(debitAmount.doubleValue());
		debitAmount = creditAmount;
		creditAmount = oldDebitAmount;
		
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
	
	public void setBalance(BigDecimal balance) {
		if (balance.signum()>0) {
			debitAmount = balance;
			creditAmount = BigDecimal.ZERO;
		} else {
			creditAmount = balance.negate();
			debitAmount = BigDecimal.ZERO;
		}
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
	public String getTaxDomicile() {
		return taxDomicile;
	}
	public void setTaxDomicile(String taxDomicile) {
		this.taxDomicile = taxDomicile;
	}

	public Boolean getIsService() {
		return isService;
	}
	public void setIsService(Boolean isService) {
		this.isService = isService;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public Boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	
	
	
}
