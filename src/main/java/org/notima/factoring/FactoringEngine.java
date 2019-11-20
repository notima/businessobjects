package org.notima.factoring;

import java.util.Properties;

import org.notima.generic.businessobjects.*;
import org.notima.generic.ifacebusinessobjects.FactoringReservation;

/**
 * Interface for factoring modules used in Adempiere.
 * A class using this interface can be used to provide factoring for a payment term.
 * 
 * This class is the so called Factoring Class that can be set in Payment Term.
 * 
 * @author Daniel Tamm
 *
 */
public interface FactoringEngine {
	
	/**
	 * Checks factoring status for a given customer / order
	 * 
	 * @param order
	 * @param factoringCompany
	 * @return 	Should be a value found in the list _Payment Rule status) 
	 * 			E = Factoring OK, F = Factoring Denied, 
	 * 			Z = Error in communication
	 * 
	 * @throws Exception
	 */
	public FactoringReservation reserveForFactoring(Order<?> order,
									   BusinessPartner<?> factoringCompany
									   ) throws Exception;
	

	/**
	 * Checks factoring status for a given customer / order
	 * 
	 * @param invoice
	 * @param factoringCompany
	 * @return 	Possible return values:
	 * 			E = Factoring OK, F = Factoring Denied 
	 * 			Z = Error in communication
	 * @throws Exception - If anything in the order is incomplete.
	 */
	public FactoringReservation reserveForFactoring(Invoice<?> invoice, BusinessPartner<?> factoringCompany) throws Exception;	
	
	/**
	 * Cancels an existing reservation for given customer / order
	 * 
	 * @param reservation
	 * @return
	 * @throws Exception
	 */
	public FactoringReservation cancelReservation(Order<?> order, FactoringReservation reservation) throws Exception;
	
	/**
	 * Return the highest credit amount for the given customer.
	 * 
	 * @param customer
	 * @param currency;
	 * @param factoringCompany
	 * @return	Possible amount in the given currency
	 * @throws Exception
	 */
	public double getPossibleFactoringAmount(BusinessPartner<?> customer, Currency currency, BusinessPartner<?> factoringCompany) throws Exception;
	
	/**
	 * Sends invoice to factoring company.
	 * If everything is OK, the field SentToFactoring on the invoice is updated.
	 * 
	 * @param factoringCompany
	 * @param invoice
	 * @return	The factoring company's reference/invoice no
	 * 			Null if it was denied or some other problem occurred
	 * 			If false usually an error message/reason can be found by calling getErrorMessage
	 * 			If an error occurs
	 * 			an exception is thrown.
	 * @throws Exception
	 * @see		getErrorMessage()
	 */
	public FactoringReply sendToFactoring(BusinessPartner<?> factoringCompany, Invoice<?> invoice) throws Exception;

	/**
	 * This "hook" is called after sendToFactoring has been called. The purpose of this hook is
	 * if any clean up etc must be done after an invoice has been sent.
	 * 
	 * @param factoringCompany
	 * @param invoice
	 * @param reply
	 * @throws Exception
	 */
	public void afterSendToFactoring(BusinessPartner<?> factoringCompany, Invoice<?> invoice, FactoringReply reply) throws Exception;
	
	/**
	 * Creates an address that's usable by the factoring engine.
	 * 
	 * @param customer
	 * @param user
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public AbstractAddress makeAddressFromLocation(BusinessPartner<?> customer, Person user, Location location) throws Exception;
	
	/**
	 * @return	Error message of last operation
	 */
	public String getErrorMessage();
	
	/**
	 * Returns earlier factored invoice to factoring company. 
	 * Normally it first creates a payment (AP Payment) with amt of invoice and tries to do online processing
	 * @param factoringCompany
	 * @param invoice
	 * @return	True if invoice was successfully returned. False if it was denied. If an error occurs
	 * 			an exception is thrown.
	 * @throws Exception
	 */	
	public FactoringReply returnToFactoring(BusinessPartner<?> factoringCompany, Invoice<?> invoice) throws Exception;
	
	/**
	 * This method can be used to customize the behaviour of a factoring engine.
	 * 
	 * @param props
	 */
	public void setFactoringProperties(Properties props);
	
}
