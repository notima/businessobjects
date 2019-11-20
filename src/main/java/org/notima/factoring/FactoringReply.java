package org.notima.factoring;

import java.util.Vector;

import org.notima.generic.ifacebusinessobjects.FactoringReservation;

/**
 * Class containing a factoring reply.
 * Replies are generated with invoices are sent to factoring.
 * 
 * @author Daniel Tamm
 *
 */
public class FactoringReply {

	public static final int RISK_DEFAULT = 0;
	public static final int	RISK_ON_FACTORINGCOMPANY = 1;
	public static final int RISK_ON_US = 2;
	
	private boolean success = false;
	private String	invoiceRef;
	private String	factoringResponse;
	private Vector<FactoringReservation> reservations;
	private int		riskCode = RISK_DEFAULT;
	private String	responseCode;
	
	public FactoringReply() {}
	
	public FactoringReply(boolean success) {
		this.success = success;
	}
	
	public String getInvoiceRef() {
		return invoiceRef;
	}
	public void setInvoiceRef(String invoiceRef) {
		this.invoiceRef = invoiceRef;
	}
	
	public Vector<FactoringReservation> getReservations() {
		return reservations;
	}
	
	public void setReservations(Vector<FactoringReservation> reservations) {
		this.reservations = reservations;
	}
	
	public void addReservation(FactoringReservation reservation) {
		if (reservations==null) {
			reservations = new Vector<FactoringReservation>();
		}
		reservations.add(reservation);
	}
	
	/**
	 * 
	 * @return	True if there was a reservation used.
	 */
	public boolean hasReservation() {
		return(reservations!=null && reservations.size()>0);
	}
	
	/**
	 * XC_Kreditor_Reference_ID
	 * 
	 * @return	If a reservation is used for this invoice, the first id
	 * 			of the reservation is returned. If no reservation is
	 * 			used, 0 (zero) is returned.
	 */
	public String getFirstReservationId() {
		
		if (reservations==null || reservations.size()==0)
			return null;
		
		return(reservations.get(0).getOurReservationId());
	}
	
	public FactoringReservation getFirstReservation() {
		if (hasReservation()) 
			return(reservations.get(0));
		else
			return null;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setRiskCode(int risk) {
		riskCode = risk;
	}
	
	
	public int getRiskCode() {
		return riskCode;
	}

	/**
	 * Use freely as wished. Normally some human readable text.
	 *  
	 * @return
	 */
	public String getFactoringResponse() {
		return factoringResponse;
	}

	public void setFactoringResponse(String factoringResponse) {
		this.factoringResponse = factoringResponse;
	}

	/**
	 * Use freely as wished. Normally a code.
	 * 
	 * @return
	 */
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
}
