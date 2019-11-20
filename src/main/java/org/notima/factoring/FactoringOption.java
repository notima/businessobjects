package org.notima.factoring;

import java.math.BigDecimal;

/**
 * Factoring option interface 
 * 
 * @author Daniel Tamm
 */
public interface FactoringOption {

	/**
	 * The factoring provider's identifier
	 * @return
	 */
	public String getFactoringOptionId();
	
	public void setFactoringOptionId(String id);

	/**
	 * Payment term that this option is tied to
	 * @return
	 */
	public int getC_PaymentTerm_ID();
	
	public void setC_PaymentTerm_ID(int id);

	/**
	 * XC_Kreditor_Pclass ID (if any)
	 * @return
	 */
	public int getXC_Kreditor_Pclass_ID();
	
	public void setXC_Kreditor_Pclass_ID(int id);
	
	/**
	 * Currency of the option
	 * @return
	 */
	public String getCurrency();
	
	public void setCurrency(String iso);

	/**
	 * Grand total for the customer including all costs
	 * @return
	 */
	public BigDecimal getGrandTotal();
	
	public void setGrandTotal(BigDecimal total);
	
	/**
	 * Original amount of purchase
	 * @return
	 */
	public BigDecimal getAmount();
	
	public void setAmount(BigDecimal amt);
	
	/**
	 * Monthly admin fee / admin fee per installment
	 * 
	 * @return
	 */
	public BigDecimal getAdminFee();
	
	public void setAdminFee(BigDecimal fee);
	
	/**
	 * Set up fee (one time cost)
	 * 
	 * @return
	 */
	public BigDecimal getArrangementFee();
	
	public void setArrangementFee(BigDecimal fee);
	
	public BigDecimal getKickbackRate();
	
	public void setKickbackRate(BigDecimal rate);
	
	public String getDescription();
	
	public void setDescription(String desc);
	
	/**
	 * Frequency of installments in months.
	 * Ie. quarterly installments is 3 months. 
	 * @return
	 */
	public int getPaymentPeriod();
	
	public void setPaymentPeriod(int period);
	
	/**
	 * The credit cost (original amount - extra costs)
	 * @return
	 */
	public BigDecimal getCreditCost();
	
	public void setCreditCost(BigDecimal cost);
	
	/**
	 * Effective interest
	 * @return
	 */
	public BigDecimal getEffectiveInterestPercent();
	
	public void setEffectiveInterestPercent(BigDecimal percent);
	
	/**
	 * Number of interest free months.
	 * @return
	 */
	public int getInterestFreeMonths();
	
	public void setInterestFreeMonths(int m);
	
	public void setInterestPercent(BigDecimal interest);
	
	public BigDecimal getInterestPercent();
	
	/**
	 * Amount of monthly payment (annuity)
	 * @return
	 */
	public BigDecimal getMonthlyPayment();
	
	public void setMonthlyPayment(BigDecimal pmt);
	
	/**
	 * Number of payment free months.
	 * @return
	 */
	public int getPaymentFreeMonths();
	
	public void setPaymentFreeMonths(int m);
	
}
