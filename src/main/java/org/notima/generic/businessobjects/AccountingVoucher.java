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
