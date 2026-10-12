package org.notima.generic.businessobjects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
	/**
	 * True when the line has been struck from the voucher after it was registered
	 * (SIE #BTRANS). A struck line stays on the voucher for the audit trail but does
	 * not count in any amount or balance.
	 */
	private Boolean deleted = Boolean.FALSE;
	/** Who struck the line. */
	private String deletedBy;
	/** When the line was struck. */
	private LocalDateTime deletedDate;
	/**
	 * True when the line was added to the voucher after it was registered, as a
	 * correction (SIE #RTRANS).
	 */
	private Boolean addedLater;
	/** Who added the line, when {@link #addedLater}. */
	private String addedBy;
	/** When the line was added, when {@link #addedLater}. */
	private LocalDateTime addedDate;
	
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

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public LocalDateTime getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(LocalDateTime deletedDate) {
		this.deletedDate = deletedDate;
	}

	public Boolean getAddedLater() {
		return addedLater;
	}

	public void setAddedLater(Boolean addedLater) {
		this.addedLater = addedLater;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public LocalDateTime getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(LocalDateTime addedDate) {
		this.addedDate = addedDate;
	}
	
	
	
	
}
