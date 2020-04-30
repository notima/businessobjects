package org.notima.generic.businessobjects;

public class AccountingVoucherLine {

	private String 	debetAcct;
	private String	debetAcctName;
	private String	creditAcct;
	private String	creditAcctName;
	private	String	description;
	
	public String getDebetAcct() {
		return debetAcct;
	}
	public void setDebetAcct(String debetAcct) {
		this.debetAcct = debetAcct;
	}
	public String getDebetAcctName() {
		return debetAcctName;
	}
	public void setDebetAcctName(String debetAcctName) {
		this.debetAcctName = debetAcctName;
	}
	public String getCreditAcct() {
		return creditAcct;
	}
	public void setCreditAcct(String creditAcct) {
		this.creditAcct = creditAcct;
	}
	public String getCreditAcctName() {
		return creditAcctName;
	}
	public void setCreditAcctName(String creditAcctName) {
		this.creditAcctName = creditAcctName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
