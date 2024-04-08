package org.notima.generic.businessobjects.tax;

import java.time.LocalDate;

import org.notima.generic.businessobjects.AccountElement;

public class VatRule {

	private String				vatCode;
	private String				vatReportCode;
	private String				description;
	private AccountElement		defaultRevenueAccount;
	private AccountElement		defaultTaxDueAccount;
	private AccountElement		defaultTaxClaimAccount;
	private LocalDate			validFrom;
	private LocalDate			validUntil;
	private double				vatRate;
	private boolean				isService;
	private boolean				isReverse;
	private String				taxDomicile;
	
	public String getVatCode() {
		return vatCode;
	}
	public void setVatCode(String vatCode) {
		this.vatCode = vatCode;
	}
	public String getVatReportCode() {
		return vatReportCode;
	}
	public void setVatReportCode(String vatReportCode) {
		this.vatReportCode = vatReportCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public AccountElement getDefaultRevenueAccount() {
		return defaultRevenueAccount;
	}
	public void setDefaultRevenueAccount(AccountElement defaultRevenueAccount) {
		this.defaultRevenueAccount = defaultRevenueAccount;
	}
	public AccountElement getDefaultTaxDueAccount() {
		return defaultTaxDueAccount;
	}
	public void setDefaultTaxDueAccount(AccountElement defaultTaxDueAccount) {
		this.defaultTaxDueAccount = defaultTaxDueAccount;
	}
	public AccountElement getDefaultTaxClaimAccount() {
		return defaultTaxClaimAccount;
	}
	public void setDefaultTaxClaimAccount(AccountElement defaultTaxClaimAccount) {
		this.defaultTaxClaimAccount = defaultTaxClaimAccount;
	}
	public LocalDate getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}
	public LocalDate getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(LocalDate validUntil) {
		this.validUntil = validUntil;
	}
	public double getVatRate() {
		return vatRate;
	}
	public void setVatRate(double vatRate) {
		this.vatRate = vatRate;
	}
	public boolean isService() {
		return isService;
	}
	public void setService(boolean isService) {
		this.isService = isService;
	}
	public boolean isReverse() {
		return isReverse;
	}
	public void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}
	public String getTaxDomicile() {
		return taxDomicile;
	}
	public void setTaxDomicile(String taxDomicile) {
		this.taxDomicile = taxDomicile;
	}
	
	
	
}
