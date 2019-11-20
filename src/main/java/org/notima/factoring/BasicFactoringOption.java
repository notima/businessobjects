package org.notima.factoring;

import java.math.BigDecimal;

/**
 * Basic factoring option class.
 * 
 * @author daniel.tamm
 *
 */
public class BasicFactoringOption implements FactoringOption {

	private String 		factoringOptionId;
	private int			c_PaymentTerm_ID;
	private int			xc_KreditorPclassID;
	private String		currency;
	private BigDecimal	grandTotal;
	private BigDecimal	amount;
	private BigDecimal	adminFee;
	private BigDecimal	arrangementFee;
	private BigDecimal	kickbackRate;
	private String		description;
	private int			paymentPeriod;
	private BigDecimal	creditCost;
	private BigDecimal	effectiveInterestPercent;
	private int			interestFreeMonths;
	private BigDecimal	interestPercent;
	private BigDecimal	monthlyPayment;
	private int			paymentFreeMonths;
	
	public String getFactoringOptionId() {
		return factoringOptionId;
	}
	
	public void setFactoringOptionId(String factoringOptionId) {
		this.factoringOptionId = factoringOptionId;
	}
	public int getC_PaymentTerm_ID() {
		return c_PaymentTerm_ID;
	}
	public void setC_PaymentTerm_ID(int c_PaymentTerm_ID) {
		this.c_PaymentTerm_ID = c_PaymentTerm_ID;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAdminFee() {
		return adminFee;
	}
	public void setAdminFee(BigDecimal adminFee) {
		this.adminFee = adminFee;
	}
	public BigDecimal getArrangementFee() {
		return arrangementFee;
	}
	public void setArrangementFee(BigDecimal arrangementFee) {
		this.arrangementFee = arrangementFee;
	}
	public BigDecimal getKickbackRate() {
		return kickbackRate;
	}
	public void setKickbackRate(BigDecimal kickbackRate) {
		this.kickbackRate = kickbackRate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPaymentPeriod() {
		return paymentPeriod;
	}
	public void setPaymentPeriod(int paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}
	public BigDecimal getCreditCost() {
		return creditCost;
	}
	public void setCreditCost(BigDecimal creditCost) {
		this.creditCost = creditCost;
	}
	public BigDecimal getEffectiveInterestPercent() {
		return effectiveInterestPercent;
	}
	public void setEffectiveInterestPercent(BigDecimal effectiveInterestPercent) {
		this.effectiveInterestPercent = effectiveInterestPercent;
	}
	public int getInterestFreeMonths() {
		return interestFreeMonths;
	}
	public void setInterestFreeMonths(int interestFreeMonths) {
		this.interestFreeMonths = interestFreeMonths;
	}
	public BigDecimal getInterestPercent() {
		return interestPercent;
	}
	public void setInterestPercent(BigDecimal interestPercent) {
		this.interestPercent = interestPercent;
	}
	public BigDecimal getMonthlyPayment() {
		return monthlyPayment;
	}
	public void setMonthlyPayment(BigDecimal monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}
	public int getPaymentFreeMonths() {
		return paymentFreeMonths;
	}
	public void setPaymentFreeMonths(int paymentFreeMonths) {
		this.paymentFreeMonths = paymentFreeMonths;
	}

	@Override
	public int getXC_Kreditor_Pclass_ID() {
		return xc_KreditorPclassID;
	}

	@Override
	public void setXC_Kreditor_Pclass_ID(int id) {
		xc_KreditorPclassID = id;
	}

	public String toString() {
            return(factoringOptionId + " " + description);
        }
	
}
