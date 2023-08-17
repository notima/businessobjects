package org.notima.generic.ifacebusinessobjects;

/**
 * An interface used to validate an order invoice line.
 * 
 * @author Daniel Tamm
 *
 */
public interface OrderInvoiceLineValidator {

	public void setLineToValidate(OrderInvoiceLine line);
	
	public boolean isLineValid();
	
	public String getValidationMessage();
	
}
