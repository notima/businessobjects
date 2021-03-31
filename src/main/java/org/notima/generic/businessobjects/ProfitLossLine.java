package org.notima.generic.businessobjects;

import java.util.List;

/**
 * Represents a P/L line.
 * 
 * @author Daniel Tamm
 *
 */
public class ProfitLossLine {

	private String	accountNo;
	private String	accountClass;
	private String	accountingType;
	
	private String	description;
	
	private List<ProfitLossColumn>	columns;

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

	public String getAccountingType() {
		return accountingType;
	}

	public void setAccountingType(String accountingType) {
		this.accountingType = accountingType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ProfitLossColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ProfitLossColumn> columns) {
		this.columns = columns;
	}
	
	
	
}
