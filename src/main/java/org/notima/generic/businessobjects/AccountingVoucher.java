package org.notima.generic.businessobjects;

import java.beans.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.github.threetenjaxb.core.LocalDateTimeXmlAdapter;
import io.github.threetenjaxb.core.LocalDateXmlAdapter;

/**
 * A general accounting voucher.
 * 
 * @author daniel
 *
 */
public class AccountingVoucher {

	private LocalDate		acctDate;
	private LocalDateTime	regDate;
	private	String			description;
	private String			regBy;
	private String			voucherNo;
	private String			voucherSeries;
	private String			costCenter;
	private String			projectCode;
	private BigDecimal		totalCredit;
	private	BigDecimal		totalDebet;
	private String			comments;
	private String			sourceCurrency;
	private String			accountingCurrency;
	
	private Integer			precision = 2;
	
	private List<AccountingVoucherLine> lines;

	/**
	 * Creates an accounting voucher from a payout line
	 * 
	 * @param pl
	 * @return		An accounting voucher
	 */
	public static AccountingVoucher buildVoucherFromPayoutLine(PayoutLine pl) {
		
		AccountingVoucher voucher = new AccountingVoucher();
		voucher.setAcctDate(pl.getAcctDate());
		voucher.setDescription(pl.getDescription());
		
		AccountingVoucherLine vl;
		
		if (pl.getFeeAmount()!=0) {
			vl = new AccountingVoucherLine(BigDecimal.valueOf(pl.getFeeAmount()), AccountingType.OTHER_EXPENSES_SALES);
			voucher.addVoucherLine(vl);
		}

		if (pl.getTaxAmount()!=0) {
			vl = new AccountingVoucherLine(BigDecimal.valueOf(pl.getTaxAmount()), AccountingType.CLAIM_VAT);
			voucher.addVoucherLine(vl);
		}
		
		if (pl.getPaidOut()!=0) {
			vl = new AccountingVoucherLine(BigDecimal.valueOf(pl.getPaidOut()), AccountingType.LIQUID_ASSET_CASH);
			voucher.addVoucherLine(vl);
		}
		
		voucher.balanceWithLine(AccountingType.LIQUID_ASSET_AR);
		
		return voucher;
		
	}
	
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

	public AccountingVoucherIdentification createIdentification() {
		return new AccountingVoucherIdentification(voucherSeries, voucherNo);
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
	
	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public void addVoucherLine(AccountingVoucherLine vl) {

		if (lines==null) {
			lines = new ArrayList<AccountingVoucherLine>();
		}
		if (precision!=null) {
			vl.setCreditAmount(roundToPrecision(vl.getCreditAmount()));
			vl.setDebitAmount(roundToPrecision(vl.getDebitAmount()));
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
	 * 
	 * 
	 * @param acctType		The account type to look for
	 * @return				True if exists
	 */
	public boolean hasVoucherLinesWithType(String acctType) {
		
		if (lines==null) return false;
		
		boolean result = false;
		
		for (AccountingVoucherLine l : lines) {
			if (l.getAcctType()!=null && l.getAcctType().equalsIgnoreCase(acctType)) {
				return true;
			}
		}
		
		return result;
		
	}
	
	/**
	 * Merges all fromTypes into the toType. The description is taken from the.
	 * 
	 * @param 	fromTypes			The types to merge from.
	 * @param 	toType				The type to merge to.
	 * @param	lineDescription		A description on the new merged line.
	 * @return
	 */
	public int mergeAccountTypesToAccountType(Set<String> fromTypes, String toType, String lineDescription) {
		int linesMerged = 0;
		
		if (lines==null || fromTypes==null || toType==null) return linesMerged;
		
		BigDecimal mergeBalance = new BigDecimal(0);
		
		List<AccountingVoucherLine> linesToMerge = new ArrayList<AccountingVoucherLine>();
		for (AccountingVoucherLine line : lines) {
			if (fromTypes.contains(line.getAcctType())) {
				linesToMerge.add(line);
				mergeBalance = mergeBalance.add(line.getBalance());
			}
		}

		// Remove the lines that have been merged
		for (AccountingVoucherLine rmLine : linesToMerge) {
			lines.remove(rmLine);
		}

		// Add the merged line (unless the merged lines cancel eachother out).
		if (mergeBalance.signum()!=0) {
			// Create a merge row
			AccountingVoucherLine mergedRow = new AccountingVoucherLine();
			mergedRow.setAcctType(toType);
			mergedRow.setDescription(lineDescription);
			mergedRow.setBalance(mergeBalance);
			lines.add(mergedRow);
		}
		
		return linesMerged;
	}
	
	/**
	 * 
	 * @return	True if all voucher lines has an account no
	 */
	public boolean allVoucherLinesHasAccountNo() {
		
		if (lines==null) return false;
		
		boolean result = true;
		for (AccountingVoucherLine l : lines) {
			if (l.getAcctNo()==null || l.getAcctNo().trim().length()==0) {
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Remaps all occurrances of accountType and taxKey to toAcct
	 * 
	 * @param accountType
	 * @param taxKey			Must not be null
	 * @param toAcct
	 */
	public void remapAccountTypeAndTaxKey(String accountType, String taxKey, String toAcct) {

		if (lines==null) return;
		
		for (AccountingVoucherLine l : lines) {
			if (accountType==null) {
				if (l.getAcctType()==null && taxKey.equals(l.getTaxKey())) {
					l.setAcctNo(toAcct);
				}
			} else if (accountType.equals(l.getAcctType()) && taxKey.equals(l.getTaxKey())) {
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
		
		balance = roundToPrecision(balance);
		
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
		// Re-balance total credit and debit
		getBalance();
		return result;
		
	}
	
	public void addVoucherLine(BigDecimal amount, String acctNo) {

		amount = roundToPrecision(amount);		
		
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

		amount = roundToPrecision(amount);
		
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

	public void reverseWithComment(String comment) {
		
		if (description!=null && description.trim().length()>0) {
			description += " : " + comment;
		} else {
			description = comment;
		}
		
		if (lines==null) return;
		
		for (AccountingVoucherLine avl : lines) {
			avl.reverse();
		}
		
	}
	
	/**
	 * Purges this voucher from lines with amounts that are zero when rounded with precision.
	 */
	public void purge() {
		
		if (lines==null) return;
		
		List<AccountingVoucherLine> purgeThese = new ArrayList<AccountingVoucherLine>();
		
		for (AccountingVoucherLine avl : lines) {
			avl.setCreditAmount(roundToPrecision(avl.getCreditAmount()));
			avl.setDebitAmount(roundToPrecision(avl.getDebitAmount()));
			if (avl.getCreditAmount().signum()==0 &&
					avl.getDebitAmount().signum()==0) {
				purgeThese.add(avl);
			}
		}
		
		if (purgeThese.size()>0) {
			for (AccountingVoucherLine avl : purgeThese) {
				lines.remove(avl);
			}
		}
		
		balanceWithLine(AccountingType.ROUNDING);
		
	}
	
	/**
	 * Sets rounding precision for this voucher. If null, no rounding is used.
	 * 
	 * The number is the number of digits after the decimal separator.
	 * 
	 * @return
	 */
	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
	
	/**
	 * If precision is set, this big decimal is rounded to precision.
	 * 
	 * @param bd
	 * @return		The rounded value.
	 */
	private BigDecimal roundToPrecision(BigDecimal bd) {
		if (precision!=null) {
			BigInteger bi = bd.movePointRight(precision).toBigInteger();
			bd = new BigDecimal(bi).movePointLeft(precision);
		}
		return bd;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * 
	 * @return		True if processing this voucher needs currency conversion (ie source and accounting currency differs)
	 * 
	 */
	@Transient
	public boolean needsCurrencyConversion() {
		if (sourceCurrency==null || accountingCurrency==null) return false;
		if (sourceCurrency.toLowerCase().equals(accountingCurrency.toLowerCase())) return false;
		return true;
	}
	
	/**
	 * The currency that amounts are specified in
	 * 
	 * @return
	 */
	public String getSourceCurrency() {
		return sourceCurrency;
	}

	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}

	/**
	 * The currency that are used in the general ledger (accounting currency)
	 * 
	 * @return
	 */
	public String getAccountingCurrency() {
		return accountingCurrency;
	}

	public void setAccountingCurrency(String accountingCurrency) {
		this.accountingCurrency = accountingCurrency;
	}

	
	
	
	
}
