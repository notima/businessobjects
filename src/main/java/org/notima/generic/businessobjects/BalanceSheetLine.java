package org.notima.generic.businessobjects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a P/L line.
 * 
 * @author Daniel Tamm
 *
 */
public class BalanceSheetLine {

	private String	accountNo;
	private String	accountClass;
	private String	accountingType;
	
	private String	description;
	
	private List<BalanceSheetColumn>	columns;

	/**
	 * Adds a given amount to this line for the given period.
	 * 
	 * @param amount
	 * @param period
	 */
	public void addAmountForPeriod(BigDecimal amount, AccountingPeriod period) {

		if (columns==null)
			columns = new ArrayList<BalanceSheetColumn>();
		
		BalanceSheetColumn plc = getColumnForPeriod(period);
		if (plc==null) {
			plc = new BalanceSheetColumn();
			plc.setAmount(amount);
			plc.setPeriod(period);
			columns.add(plc);
		} else {
			plc.setAmount(plc.getAmount().add(amount));
		}
		
	}
	
	public BalanceSheetColumn getColumnForPeriod(AccountingPeriod period) {
		
		if (columns==null) return null;
		
		
		for (BalanceSheetColumn plc : columns) {
			if (plc.getPeriod().equals(period)) {
				return plc;
			}
		}
		
		return null;
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

	public List<BalanceSheetColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<BalanceSheetColumn> columns) {
		this.columns = columns;
	}
	
}
