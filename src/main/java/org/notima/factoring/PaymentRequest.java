package org.notima.factoring;

import java.math.BigDecimal;

import org.notima.generic.businessobjects.*;

/**
 * Represents a request for a payment.
 * 
 * @author Daniel Tamm
 *
 */

public interface PaymentRequest {

	/**
	 * Amount of the request.
	 * @return
	 */
	public BigDecimal getAmount();
	
	/**
	 * Currency of amount
	 * @return
	 */
	public String getCurrencyIso();
	
	public String getReference();
	
	public BusinessPartner<?> getbPartner();

	public Invoice<?> getInvoice();
	
	public Order<?> getOrder();	
	
}
