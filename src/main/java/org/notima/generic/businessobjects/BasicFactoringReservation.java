package org.notima.generic.businessobjects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.notima.generic.ifacebusinessobjects.FactoringReservation;

/**
 * Basic Factoring Reservation
 * 
 * @author Daniel Tamm
 *
 */
@Entity
public class BasicFactoringReservation implements FactoringReservation {

	protected String	theirReservationId;
	@Id
	@GeneratedValue
	protected String	ourReservationId;
	protected String	reservationResponseCode;
	protected String	infoText;
	protected double 	amount;
	protected String	orderNo;
	protected String	currency;
	private boolean cancelled;
	private double usedAmount;
	private double creditCost;
	private double grandTotal;
	private double interestFreeMonths;
	private double paymentFreeMonths;
	private double paymentPeriod;
	private double interestPercent;
	private double effectiveInterestPercent;
	private double monthlyPayment;
	private double adminFee;
	private double arrangementFee;
	private String paymentTermKey;
	
	public String getTheirReservationId() {
		return theirReservationId;
	}

	public String getOurReservationId() {
		return ourReservationId;
	}

	public void setTheirReservationId(String theirReservationId) {
		this.theirReservationId = theirReservationId;
	}

	public void setOurReservationId(String ourReservationId) {
		this.ourReservationId = ourReservationId;
	}

	public String getReservationResponseCode() {
		return reservationResponseCode;
	}

	public void setReservationResponseCode(String reservationResponseCode) {
		this.reservationResponseCode = reservationResponseCode;
	}

	public String getInfoText() {
		return infoText;
	}

	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getKey() {
		return ourReservationId;
	}
	
	public void setKey(String key) {
		ourReservationId = key;
	}

	public void setPaymentTermKey(String ptKey) {
		paymentTermKey = ptKey;
	}

	public String getPaymentTermKey() {
		return paymentTermKey;
	}

	public void setArrangementFee(double fee) {
		arrangementFee = fee;
	}

	public double getArrangementFee() {
		return arrangementFee;
	}

	public void setAdminFee(double fee) {
		adminFee = fee;
	}

	public void setMonthlyPayment(double amt) {
		monthlyPayment = amt;
	}

	public double getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setEffectiveInterestPercent(double p) {
		effectiveInterestPercent = p;
	}

	public void setInterestPercent(double ip) {
		interestPercent = ip;
	}

	public void setPaymentPeriod(double months) {
		paymentPeriod = months;
	}

	public void setPaymentFreeMonths(double months) {
		paymentFreeMonths = months;
	}

	public void setInterestFreeMonths(double months) {
		interestFreeMonths = months;
	}

	public void setGrandTotal(double amt) {
		grandTotal = amt;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setCreditCost(double amt) {
		creditCost = amt;
	}

	public double getUsedAmount() {
		return usedAmount;
	}

	public void setCancelled(boolean flag) {
		cancelled = flag;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public double getCreditCost() {
		return creditCost;
	}

	public double getInterestFreeMonths() {
		return interestFreeMonths;
	}

	public double getPaymentFreeMonths() {
		return paymentFreeMonths;
	}

	public double getPaymentPeriod() {
		return paymentPeriod;
	}

	public double getInterestPercent() {
		return interestPercent;
	}

	public double getEffectiveInterestPercent() {
		return effectiveInterestPercent;
	}

	public double getAdminFee() {
		return adminFee;
	}

	public void setUsedAmount(double usedAmount) {
		this.usedAmount = usedAmount;
	}

}
