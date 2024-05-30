package org.notima.generic.businessobjects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.notima.generic.businessobjects.exception.CurrencyMismatchException;
import org.notima.generic.businessobjects.exception.DateMismatchException;
import org.notima.util.LocalDateUtils;

/**
 * This line describes a payout and if applicable associated accounting accounts and amounts.
 * 
 * @author Daniel Tamm
 *
 */
public class PayoutLine {

	private String	paymentType;
	private String	paymentTypeReference;
	private LocalDate	acctDate;
	
	private int		trxCount = 0;
	private Double	paidByCustomer = new Double(0);
	// Summarized fee amount of the payment specific fees in this payout
	private Double	totalFeeAmount = new Double(0);
	// Summarized tax amount of the payment specific fees in this payout.
	private Double	totalFeeTaxAmount = new Double(0);
	private String	taxKey;
	private Double	taxRate;
	private Double	paidOut = new Double(0);
	private Double	openingBalance = new Double(0);
	private Double	endingBalance = new Double(0);
	// Summarized deposit adjustment. +value means the deposit is added to. -value means the deposit is withdrawn.
	private Double	totalDepositAdjustment = new Double(0);

	private String	taxAcctNo;
	private String	feeAcctNo;
	private String	receivablesAcctNo;
	private String	paidOutAcctNo;
	
	private String	currency;
	private double	currencyRateToAccountingCurrency;
	
	private boolean	includedInOtherPayout = false;
	private boolean	allowPaymentDateAfterPayoutDate = false;
	
	private String	description;

	// Fees that are not tied to a specific payment
	private List<PayoutFee>	payoutFees;
	
	/**
	 * Adds the payout of this payment to the total of this payoutLine.
	 * Payment fees are summarized as the difference between paid amount and original amount.
	 * 
	 * @param payment
	 * @return
	 */
	public PayoutLine addPayment(Payment<?> payment) throws DateMismatchException, CurrencyMismatchException {
		
		alignPaymentAndPayoutDate(payment);
		alignCurrency(payment);
		
		double paid = payment.getAmount();
		double originalAmt = payment.getOriginalAmount(); 
		double fee = originalAmt - paid;
		// TODO: Include a VAT amount detector
		
		paidByCustomer += originalAmt;
		totalFeeAmount += fee;
		paidOut += paid;
		
		trxCount ++;
		
		return this;
		
	}
	
	private void alignPaymentAndPayoutDate(Payment<?> payment) throws DateMismatchException {
		
		if (payment.getPaymentDate()!=null) {
			LocalDate paymentDate = LocalDateUtils.asLocalDate(payment.getPaymentDate()); 
			if (acctDate==null) {
				acctDate = LocalDateUtils.asLocalDate(payment.getPaymentDate());
			} else {
				// Compare
				if (!allowPaymentDateAfterPayoutDate) {
					if (!paymentDate.isEqual(acctDate)) {
						throw new DateMismatchException(acctDate, paymentDate);
					}
				}
			}
		}
		
	}
	
	private void alignCurrency(Payment<?> payment) throws CurrencyMismatchException {
		
		if (payment.getCurrency()!=null) {
			
			if (currency==null) {
				currency = payment.getCurrency();
			} else {
				// Compare
				if (!currency.equalsIgnoreCase(payment.getCurrency())) {
					throw new CurrencyMismatchException(currency, payment.getCurrency());
				}
			}
			
		}
		
	}
	
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentTypeReference() {
		return paymentTypeReference;
	}
	public void setPaymentTypeReference(String paymentTypeReference) {
		this.paymentTypeReference = paymentTypeReference;
	}
	public LocalDate getAcctDate() {
		return acctDate;
	}
	public void setAcctDate(LocalDate acctDate) {
		this.acctDate = acctDate;
	}
	public boolean isIncludedInOtherPayout() {
		return includedInOtherPayout;
	}
	public void setIncludedInOtherPayout(boolean includedInOtherPayout) {
		this.includedInOtherPayout = includedInOtherPayout;
	}
	public int getTrxCount() {
		return trxCount;
	}
	public void setTrxCount(int trxCount) {
		this.trxCount = trxCount;
	}
	public Double getPaidByCustomer() {
		return paidByCustomer;
	}
	public void setPaidByCustomer(Double paidByCustomer) {
		this.paidByCustomer = paidByCustomer;
	}

	/**
	 * Deposit adjustment. A positive value means that payout is decreased and deposit increased.
	 * 
	 * @return
	 */
	public Double getTotalDepositAdjustment() {
		return totalDepositAdjustment;
	}

	public void setTotalDepositAdjustment(Double totalDepositAdjustment) {
		this.totalDepositAdjustment = totalDepositAdjustment;
	}

	/**
	 * Summarized fee amount of all the payments contained in this payout.
	 * @return
	 */
	public Double getTotalFeeAmount() {
		return totalFeeAmount;
	}
	public void setTotalFeeAmount(Double feeAmount) {
		this.totalFeeAmount = feeAmount;
	}
	
	/**
	 * Summarized tax amount of all the payments contained in this payout.
	 * @return
	 */
	public Double getTotalFeeTaxAmount() {
		return totalFeeTaxAmount;
	}
	
	public void setTotalFeeTaxAmount(Double taxAmount) {
		this.totalFeeTaxAmount = taxAmount;
	}
	public String getTaxKey() {
		return taxKey;
	}
	public void setTaxKey(String taxKey) {
		this.taxKey = taxKey;
	}
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	public Double getPaidOut() {
		return paidOut;
	}
	public void setPaidOut(Double paidOut) {
		this.paidOut = paidOut;
	}
	public String getTaxAcctNo() {
		return taxAcctNo;
	}
	public void setTaxAcctNo(String taxAcctNo) {
		this.taxAcctNo = taxAcctNo;
	}
	public String getFeeAcctNo() {
		return feeAcctNo;
	}
	public void setFeeAcctNo(String feeAcctNo) {
		this.feeAcctNo = feeAcctNo;
	}
	public String getReceivablesAcctNo() {
		return receivablesAcctNo;
	}
	public void setReceivablesAcctNo(String receivablesAcctNo) {
		this.receivablesAcctNo = receivablesAcctNo;
	}
	public String getPaidOutAcctNo() {
		return paidOutAcctNo;
	}
	public void setPaidOutAcctNo(String paidOutAcctNo) {
		this.paidOutAcctNo = paidOutAcctNo;
	}
	
	public Double getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}
	public Double getEndingBalance() {
		return endingBalance;
	}
	public void setEndingBalance(Double endingBalance) {
		this.endingBalance = endingBalance;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void addPayoutFee(PayoutFee fee) {
		if (fee==null) return;
		if (payoutFees==null) {
			payoutFees = new ArrayList<PayoutFee>();
		}
		payoutFees.add(fee);
	}
	
	public List<PayoutFee> getPayoutFees() {
		return payoutFees;
	}

	public void setPayoutFees(List<PayoutFee> payoutFees) {
		this.payoutFees = payoutFees;
	}

	/**
	 * Adds payout fees to fee total.
	 * Handy to call if the fees are not specified in the voucher.
	 */
	public void addPayoutFeesToFeeTotal() {

		if (payoutFees!=null) {
			for (PayoutFee fee : payoutFees) {
				addPayoutFeeToTotal(fee);
				deductPaidOutWithPayoutFee(fee);
			}
		}
		
	}

	private void addPayoutFeeToTotal(PayoutFee fee) {
		if (fee!=null) {
			if (fee.getFeeAmount()!=null) {
				totalFeeAmount += fee.getFeeAmount();
			}
			if (fee.getVatAmount()!=null) {
				totalFeeTaxAmount += fee.getVatAmount();
			}
		}
	}
	
	public boolean isAllowPaymentDateAfterPayoutDate() {
		return allowPaymentDateAfterPayoutDate;
	}

	public void setAllowPaymentDateAfterPayoutDate(boolean allowPaymentDateAfterPayoutDate) {
		this.allowPaymentDateAfterPayoutDate = allowPaymentDateAfterPayoutDate;
	}

	private void deductPaidOutWithPayoutFee(PayoutFee fee) {
		if (fee!=null) {
			paidOut -= fee.getFeeAndVatTotal();
		}
	}
	
	public double getCurrencyRateToAccountingCurrency() {
		return currencyRateToAccountingCurrency;
	}

	public void setCurrencyRateToAccountingCurrency(double currencyRateToAccountingCurrency) {
		this.currencyRateToAccountingCurrency = currencyRateToAccountingCurrency;
	}
	
}
