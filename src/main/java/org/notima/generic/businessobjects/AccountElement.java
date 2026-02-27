package org.notima.generic.businessobjects;

/**
 *
 * Represents an accounting element. Use this to relay more information than just
 * the account number.
 *
 */
public class AccountElement {

	/**
	 * Indicates whether this account element belongs to the balance sheet or the
	 * profit &amp; loss statement.
	 * <p>
	 * The constant names are chosen to match their JSON representation directly
	 * (i.e. {@code "BalanceAcct"} and {@code "PLAcct"}), so no extra serializer
	 * configuration is required.
	 */
	public enum AccountElementType {
		/** Balance sheet account (assets, liabilities, equity). */
		BalanceAcct,
		/** Profit &amp; loss / income statement account. */
		PLAcct
	}

	private String	accountNo;
	private String	accountClass;
	
	private String	project;
	private String	costCenter;
	
	private String	name;
	
	private String	taxKey;

	private AccountElementType	accountElementType;
	
	/**
	 * If this account element is owned by an accounting period, this number specifies the
	 * opening balance.
	 */
	private double  openingBalance;

	/**
	 * If this account element is owned by an accounting period, this number specifies the
	 * ending balance. Note, this could also be calculated from transactions.
	 */
	private double	endingBalance;
	

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
	
	/**
	 * 
	 * @return	The account class.
	 * @see AccountClass
	 */
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

	/**
	 * 
	 * @return	A tax key if set. 
	 * @see Tax
	 */
	public String getTaxKey() {
		return taxKey;
	}

	public AccountElement setTaxKey(String taxKey) {
		this.taxKey = taxKey;
		return this;
	}

	public AccountElementType getAccountElementType() {
		return accountElementType;
	}

	public AccountElement setAccountElementType(AccountElementType accountElementType) {
		this.accountElementType = accountElementType;
		return this;
	}
	
	public String toString() {
		return accountNo;
	}

	public double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public double getEndingBalance() {
		return endingBalance;
	}

	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}
	
	
}
