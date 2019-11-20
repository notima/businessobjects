package org.notima.generic.ifacebusinessobjects;

public interface FactoringReservation {
	
	/**
	 *     Below constants are defined in 
	 *     Should be a value found in the list "_Payment Rule status"
	 **/
	public static final	String	FACTORING_OK = "E";
	public static final String 	FACTORING_DENIED = "F";
	public static final String	FACTORING_ERROR_IN_COMMUNICATION = "Z";
	public static final String  FACTORING_REQUEST_SENT = "I";
	public static final String	FACTORING_RESERVATION_REMOVED = "K";
	
	
	public String getKey();
	
	/**
	 * @return The factoring company's reservation ID
	 */
	public String getTheirReservationId();
	
	public void setTheirReservationId(String resNo);

	/**
	 * @return Our reservation ID
	 */
	public String getOurReservationId();
	
	/**
	 * @return Response code of the reservation
	 * 
	 *     	   Should be a value found in the list _Payment Rule status) 
	 * 		   E = Factoring OK, F = Factoring Denied, 
	 * 		   Z = Error in communication
	 *  
	 */
	public String getReservationResponseCode();
	
	public void setReservationResponseCode(String code);
	
	/**
	 * Additional information. Can provide more information about
	 * how a reservation call turned out.
	 *  
	 * @return
	 */
	public String getInfoText();
	
	/**
	 * Additional information. Can provide more information about
	 * how a reservation call turned out.
	 *  
	 */
	public void setInfoText(String infoText);
	
	public void setPaymentTermKey(String ptKey);
	
	public String getPaymentTermKey();
	
	public void setAmount(double amount);
	
	public double getAmount();
	
	public void setCurrency(String currency);
	
	public String getCurrency();
	
	public void setOrderNo(String orderNo);
	
	public String getOrderNo();
	
	public void setArrangementFee(double fee);
	
	public double getArrangementFee();
	
	public void setAdminFee(double fee);
	
	public void setMonthlyPayment(double amt);
	
	public double getMonthlyPayment();
	
	public void setEffectiveInterestPercent(double p);
	
	public void setInterestPercent(double ip);
	
	public void setPaymentPeriod(double months);
	
	public void setPaymentFreeMonths(double months);
	
	public void setInterestFreeMonths(double months);
	
	public void setGrandTotal(double amt);
	
	public double getGrandTotal();
	
	public void setCreditCost(double amt);
	
	public double getUsedAmount();
	
	public void setCancelled(boolean flag);
	
	
}
