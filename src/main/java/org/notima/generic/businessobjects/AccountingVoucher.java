package org.notima.generic.businessobjects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.github.threetenjaxb.core.LocalDateTimeXmlAdapter;
import io.github.threetenjaxb.core.LocalDateXmlAdapter;

public class AccountingVoucher {

	private LocalDate		acctDate;
	private LocalDateTime	regDate;
	private	String			description;
	private String			regBy;
	private String			voucherNo;
	private String			voucherSeries;
	private BigDecimal		totalCredit;
	private	BigDecimal		totalDebet;
	
	private List<AccountingVoucherLine> lines;

	@XmlJavaTypeAdapter(LocalDateXmlAdapter.class)	
	public LocalDate getAcctDate() {
		return acctDate;
	}

	public void setAcctDate(LocalDate acctDate) {
		this.acctDate = acctDate;
	}

	@XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)	
	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRegBy() {
		return regBy;
	}

	public void setRegBy(String regBy) {
		this.regBy = regBy;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getVoucherSeries() {
		return voucherSeries;
	}

	public void setVoucherSeries(String voucherSeries) {
		this.voucherSeries = voucherSeries;
	}

	public void addVoucherLine(AccountingVoucherLine vl) {

		if (lines==null) {
			lines = new ArrayList<AccountingVoucherLine>();
		}
		lines.add(vl);
		
	}
	
	/**
	 * Remaps all occurances of fromAcct with toAcct
	 * 
	 * @param fromAcct		The account to remap from (can be null).
	 * @param toAcct		The account to remap to (can be null)
	 */
	public void remapAccount(String fromAcct, String toAcct) {
		
		if (lines==null) return;
		
		for (AccountingVoucherLine l : lines) {
			if (fromAcct==null) {
				if (l.getAcctNo()==null) {
					l.setAcctNo(toAcct);
				}
			} else if (fromAcct.equals(l.getAcctNo())) {
				l.setAcctNo(toAcct);
			}
		}
		
	}
	
	/**
	 * Remaps all occurances of accountType with to toAcct
	 * 
	 * @param accountType
	 * @param toAcct
	 */
	public void remapAccountType(String accountType, String toAcct) {

		if (lines==null) return;
		
		for (AccountingVoucherLine l : lines) {
			if (accountType==null) {
				if (l.getAcctType()==null) {
					l.setAcctNo(toAcct);
				}
			} else if (accountType.equals(l.getAcctType())) {
				l.setAcctNo(toAcct);
			}
		}
		
	}
	
	public BigDecimal getTotalCredit() {
		
		totalCredit = BigDecimal.ZERO;
		
		if (lines==null)
			return totalCredit;

		for (AccountingVoucherLine l : lines) {
			totalCredit = totalCredit.add(l.getCreditAmount());
		}

		return totalCredit;
	}
	
	public BigDecimal getTotalDebet() {
		
		totalDebet = BigDecimal.ZERO;
		
		if (lines==null)
			return totalDebet;
		
		for (AccountingVoucherLine l : lines) {
			totalDebet = totalDebet.add(l.getDebitAmount());
		}
		
		return totalDebet;
		
	}

	public BigDecimal getBalance() {
		// Calculate total credit and debet
		getTotalDebet();
		getTotalCredit();
		BigDecimal balance = totalDebet.subtract(totalCredit);
		return balance;
	}
	
	/**
	 * Balance voucher with final line
	 * 
	 * @param acctType
	 * @return	If the account is already balanced, null is returned.
	 */
	public AccountingVoucherLine balanceWithLine(String acctType) {

		BigDecimal balance = getBalance();
		
		if (balance.signum()==0) {
			return null;
		}

		AccountingVoucherLine result = new AccountingVoucherLine(balance.negate(), acctType);
		addVoucherLine(result);
		return result;
		
	}
	
	public void addVoucherLine(BigDecimal amount, String acctNo) {

		if (lines==null) {
			lines = new ArrayList<AccountingVoucherLine>();
		}

		AccountingVoucherLine vl = new AccountingVoucherLine();
		if (amount.signum()>0)
			vl.setDebitAmount(amount);
		else
			vl.setCreditAmount(amount.negate());
		vl.setAcctNo(acctNo);
		
		lines.add(vl);
		
	}
	
	public void addVoucherLines(BigDecimal amount, String debetAcct, String creditAcct) {

		if (lines==null) {
			lines = new ArrayList<AccountingVoucherLine>();
		}
		
		AccountingVoucherLine vl = new AccountingVoucherLine();
		vl.setDebitAmount(amount);
		vl.setAcctNo(debetAcct);
		lines.add(vl);
		vl = new AccountingVoucherLine();
		vl.setCreditAmount(amount);
		vl.setAcctNo(creditAcct);
		lines.add(vl);
		
	}
	
	public List<AccountingVoucherLine> getLines() {
		return lines;
	}

	public void setLines(List<AccountingVoucherLine> lines) {
		this.lines = lines;
	}
	
	
	
}
