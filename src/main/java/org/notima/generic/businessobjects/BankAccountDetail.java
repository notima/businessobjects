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
	
	
	
}
