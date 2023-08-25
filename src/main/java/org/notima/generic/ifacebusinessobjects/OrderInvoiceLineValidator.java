package org.notima.generic.ifacebusinessobjects;

/**
 * An interface used to validate an order invoice line.
 * 
 * @author Daniel Tamm
 *
 */
public interface OrderInvoiceLineValidator {

	/**
	 * Call this method to set the order to perform validation on
	 * 
	 * @param oi
	 */
	public void setOrderInvoice(OrderInvoice oi);
	
	/**
	 * Call this method to set the order line to perform validation on.
	 * This method may use the OrderInvoice.
	 * 
	 * @param line
	 */
	public void setLineToValidate(OrderInvoiceLine line);
	
	public boolean isLineValid();
	
	public String getValidationMessage();
	
}
