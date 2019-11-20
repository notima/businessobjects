package org.notima.factoring;

public interface PaymentRequestProcessor {

	/**
	 * Processes the request. Status of the request is returned in the 
	 * status fields of the returned payment request record.
	 * 
	 * @param req
	 * @return
	 */
	public PaymentRequest processRequest(PaymentRequest req);
	
}
