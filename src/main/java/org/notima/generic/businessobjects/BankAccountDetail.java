package org.notima.generic.businessobjects;

/**
 * Class containing bank account details
 * 
 * @author Daniel Tamm
 *
 */
public class BankAccountDetail {

	// Use the account type to specify what kind this account is (ie, PayPal, IBAN, Bankgiro)
	private String	accountType;
	
	private String 	accountNo;
	private String 	currency;
	private String	IBAN;
	private String	BIC;
	
	private String	bankName;

	// General Ledger Account that's used to reconcile payments for this bank account 
	private String	generalLedgerReconciliationAccount;
	// General Ledger Account that this bank account is linked to.
	private String	generalLedgerBankAccount;
	// General Ledger in transit account
	private String	generalLedgerInTransitAccount;
	// General ledger fee account (for banking fees etc)
	private String	generalLedgerFeeAccount;

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public String getBIC() {
		return BIC;
	}

	public void setBIC(String bIC) {
		BIC = bIC;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getGeneralLedgerAccountToUseForPayments() {
		
		if (generalLedgerReconciliationAccount!=null && generalLedgerReconciliationAccount.trim().length()>0) {
			return generalLedgerReconciliationAccount;
		}
		
		// If no reconciliation account is defined, use the bank account
		return generalLedgerBankAccount;
		
	}
	
	public String getGeneralLedgerInTransitAccount() {
		return generalLedgerInTransitAccount;
	}

	public void setGeneralLedgerInTransitAccount(String generalLedgerInTransitAccount) {
		this.generalLedgerInTransitAccount = generalLedgerInTransitAccount;
	}

	public String getGeneralLedgerReconciliationAccount() {
		return generalLedgerReconciliationAccount;
	}

	public void setGeneralLedgerReconciliationAccount(String generalLedgerReconciliationAccount) {
		this.generalLedgerReconciliationAccount = generalLedgerReconciliationAccount;
	}

	public String getGeneralLedgerBankAccount() {
		return generalLedgerBankAccount;
	}

	public void setGeneralLedgerBankAccount(String generalLedgerBankAccount) {
		this.generalLedgerBankAccount = generalLedgerBankAccount;
	}

	public String getGeneralLedgerFeeAccount() {
		return generalLedgerFeeAccount;
	}

	public void setGeneralLedgerFeeAccount(String generalLedgerFeeAccount) {
		this.generalLedgerFeeAccount = generalLedgerFeeAccount;
	}
	
	
	
}
