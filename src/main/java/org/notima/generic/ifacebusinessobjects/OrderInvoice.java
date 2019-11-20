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
	
	public List<OrderInvoiceLine> getOrderInvoiceLines();
	
	public void setFactoringReservation(FactoringReservation fr);
	
	public FactoringReservation getFactoringReservation();
	
	public String getStatus();
	
	public void setStatus(String status);
}
