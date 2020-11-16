package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.notima.bg.BgUtil;

public class DunningEntry<B,I> {
	private int	lineNo;

	private int	nextLineNo = 10;
	private int	lineNoIncrement = 10;
	
	private String letterCurrency;
	private String letterNo;
	
	private String bgNo;
	private String ocrNo;
	
	private int bgAmount;
	private int bgAmountCheck;
	private int bgAmountOre;
	private int bgNoDigitsOnly;
	
	private double totalClaims;
	private double totalInterest;
	private double totalLegalCost;
	private double grandTotal;
	
	private java.util.Date letterDate;
	private java.util.Date letterDueDate;
	
	private BusinessPartner<B> debtor = new BusinessPartner<B>();
	private BusinessPartner<B> creditor = new BusinessPartner<B>();
	
	private ArrayList<Invoice<I>> invoices = new ArrayList<Invoice<I>>();
	
	private String letterNoPrefix;
	private String ocrNoPrefix;
	
	private int letterNoLengthCheck = 0; // used to not get double prefixes for the letterNo.

	public DunningEntry() {
		
	}
	
	public void calculateValues(){
		//set grandTotal
		grandTotal = 0;
		for(Invoice<I> invoice: invoices){
			invoice.calculateGrandTotal();
			grandTotal+= invoice.getGrandTotal();
		}
		grandTotal = InvoiceLine.round(grandTotal, 2);
		
		bgAmount = (int) grandTotal;
		int ore = (int)((grandTotal - (long) grandTotal) * 100 + 0.5);
		bgAmountOre = ore;
		
		// set letter dates
		letterDate = new Date();
		
		Date tempDue = new Date();
		Calendar cal = Calendar.getInstance();
        cal.setTime(tempDue);
        cal.add(Calendar.DATE, 8);
        letterDueDate = cal.getTime();
		

	}
	
	public void calculateLetterNo(){
		if(letterNoPrefix != null && letterNoPrefix != "" && letterNoLengthCheck < letterNo.length()){
			letterNo = letterNoPrefix + letterNo;
			letterNoLengthCheck = letterNo.length();
		}
	}
	public void calculateOcrNo(){
		if(ocrNo == null){
			String ocr;
			if(letterNoPrefix != null && letterNoPrefix != "" && letterNo.substring(0,letterNoPrefix.length()) == letterNoPrefix){
				ocr = ocrNoPrefix + letterNo.substring(letterNoPrefix.length());
			}
			else{
				ocr = ocrNoPrefix + letterNo;
			}
			ocrNo = BgUtil.toOCRNumberWithLengthCheck(ocr);
		}
	}
	
	
	public void addInvoice(Invoice<I> i) {
		if (i.getLineNo()==0) {
			i.setLineNo(nextLineNo);
			nextLineNo+=lineNoIncrement;
		}
		invoices.add(i);
	}
	
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public int getNextLineNo() {
		return nextLineNo;
	}
	public void setNextLineNo(int nextLineNo) {
		this.nextLineNo = nextLineNo;
	}
	public int getLineNoIncrement() {
		return lineNoIncrement;
	}
	public void setLineNoIncrement(int lineNoIncrement) {
		this.lineNoIncrement = lineNoIncrement;
	}
	public String getLetterCurrency() {
		return letterCurrency;
	}
	public void setLetterCurrency(String letterCurrency) {
		this.letterCurrency = letterCurrency;
	}
	public String getLetterNo() {
		return letterNo;
	}
	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}
	public String getBgNo() {
		return bgNo;
	}
	public void setBgNo(String bgNo) {
		this.bgNo = bgNo;
	}
	public String getOcrNo() {
		return ocrNo;
	}
	public void setOcrNo(String ocrNo) {
		this.ocrNo = ocrNo;
	}
	public int getBgAmount() {
		return bgAmount;
	}
	public void setBgAmount(int bgAmount) {
		this.bgAmount = bgAmount;
	}
	public int getBgAmountCheck() {
		return bgAmountCheck;
	}
	public void setBgAmountCheck(int bgAmountcheck) {
		this.bgAmountCheck = bgAmountcheck;
	}
	public int getBgAmountOre() {
		return bgAmountOre;
	}
	public void setBgAmountOre(int bgAmountOre) {
		this.bgAmountOre = bgAmountOre;
	}
	public int getBgNoDigitsOnly() {
		return bgNoDigitsOnly;
	}
	public void setBgNoDigitsOnly(int bgNoDigitsOnly) {
		this.bgNoDigitsOnly = bgNoDigitsOnly;
	}
	public double getTotalClaims() {
		return totalClaims;
	}
	public void setTotalClaims(double totalClaims) {
		this.totalClaims = totalClaims;
	}
	public double getTotalInterest() {
		return totalInterest;
	}
	public void setTotalInterest(double totalInterest) {
		this.totalInterest = totalInterest;
	}
	public double getTotalLegalCost() {
		return totalLegalCost;
	}
	public void setTotalLegalCost(double totalLegalCost) {
		this.totalLegalCost = totalLegalCost;
	}
	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}
	public java.util.Date getLetterDate() {
		return letterDate;
	}
	public void setLetterDate(java.util.Date letterDate) {
		this.letterDate = letterDate;
	}
	public java.util.Date getLetterDueDate() {
		return letterDueDate;
	}
	public void setLetterDueDate(java.util.Date letterDueDate) {
		this.letterDueDate = letterDueDate;
	}
	public BusinessPartner<B> getDebtor() {
		return debtor;
	}
	public void setDebtor(BusinessPartner<B> debtor) {
		this.debtor = debtor;
	}
	public BusinessPartner<B> getCreditor() {
		return creditor;
	}
	public void setCreditor(BusinessPartner<B> creditor) {
		this.creditor = creditor;
	}
	public ArrayList<Invoice<I>> getInvoices() {
		return invoices;
	}
	public void setInvoices(ArrayList<Invoice<I>> invoices) {
		this.invoices = invoices;
	}

	public String getLetterNoPrefix() {
		return letterNoPrefix;
	}

	public void setLetterNoPrefix(String letterNoPrefix) {
		this.letterNoPrefix = letterNoPrefix;
	}

	public String getOcrNoPrefix() {
		return ocrNoPrefix;
	}

	public void setOcrNoPrefix(String ocrNoPrefix) {
		this.ocrNoPrefix = ocrNoPrefix;
	}

	public int getLetterNoLengthCheck() {
		return letterNoLengthCheck;
	}

	public void setLetterNoLengthCheck(int letterNoLengthCheck) {
		this.letterNoLengthCheck = letterNoLengthCheck;
	}
	
}
