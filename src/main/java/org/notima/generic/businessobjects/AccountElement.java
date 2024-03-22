package org.notima.generic.businessobjects;

/**
 * 
 * Represents an accounting element. Use this to relay more information than just
 * the account number.
 * 
 */
public class AccountElement {

	private String	accountNo;
	private String	accountClass;
	
	private String	project;
	private String	costCenter;
	
	private String	name;
	
	private String	vatCode;
	
	public AccountElement() {}
	
	public AccountElement(String acctNo) {
		accountNo = acctNo;
	}
	
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountClass() {
		return accountClass;
	}
	public void setAccountClass(String accountClass) {
		this.accountClass = accountClass;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVatCode() {
		return vatCode;
	}

	public AccountElement setVatCode(String vatCode) {
		this.vatCode = vatCode;
		return this;
	}
	
	
}
