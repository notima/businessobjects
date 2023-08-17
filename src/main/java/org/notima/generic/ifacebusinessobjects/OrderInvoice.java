package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Location;
import org.notima.generic.businessobjects.Person;

public interface OrderInvoice {

	public String getDocumentKey();
	
	public void setDocumentKey(String key);
	
	public Person getBillPerson();
	
	public BusinessPartner<?> getBusinessPartner();
	
	public BusinessPartner<?> getSender();
	
	public Location getShipLocation();
	
	public BusinessPartner<?> getBillBpartner();
	
	public java.util.Date getDocumentDate();
	
	public java.util.Date getDueDate();
	
	public String getPaymentTermKey();
	
	public String getCurrency();
	
	public void setDocumentDate(java.util.Date date);
	
	public void setGrandTotal(double amt);
	
	public double getGrandTotal();
	
	public double getVatTotal();
	
	public double getFreightAmount();
	
	public String getShipmentNo();
	
	public boolean isCreditNote();
	
	public String getOurCustomerNo();
	
	public boolean isInvoice();
	
	public boolean isOrder();

	/**
	 * Validator to use to validate the order / invoiec.
	 * 
	 * @param validator
	 */
	public void setOrderInvoiceLineValidator(OrderInvoiceLineValidator validator);
	
	/**
	 * 
	 * @return The validator.
	 */
	public OrderInvoiceLineValidator getOrderInvoiceLineValidator();
	
	/**
	 * Method meant to return any lines that doesn't pass validation.
	 * 
	 * @return	All invalid lines. Empty list if no invalid lines are found.
	 * @see #setOrderInvoiceLineValidator(OrderInvoiceLineValidator)
	 */
	public List<OrderInvoiceLine> getInvalidLines(); 
	
	public List<OrderInvoiceLine> getOrderInvoiceLines();
	
	public void setFactoringReservation(FactoringReservation fr);
	
	public FactoringReservation getFactoringReservation();
	
	public String getStatus();
	
	public void setStatus(String status);
}
