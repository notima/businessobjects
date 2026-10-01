package org.notima.generic.ifacebusinessobjects;

import java.util.Properties;

import org.notima.generic.businessobjects.InvoiceList;

/**
 * A way of delivering formatted invoices to the customers, ie by e-mail or as
 * e-invoices through a Peppol access point. Registered as an OSGi service and
 * used by print-invoices --delivery [type].
 */
public interface InvoiceDeliveryMethod {

	/**
	 * @return	The type of the delivery method, ie email or ekopost.
	 */
	public String getType();

	/**
	 * @return	The invoice format this method delivers, ie pdf or peppol.
	 * 			Used when no format is given.
	 */
	public String getDefaultFormat();

	/**
	 * Starts delivering a list of invoices. The batch holds the state of the delivery,
	 * so that the method itself can be shared.
	 *
	 * @param invoices		The invoices to be delivered (ie for the creditor).
	 * @param props			Options for the delivery method. These depend on the method.
	 * @return	A batch to deliver the invoices in.
	 * @throws Exception	If the delivery can't be started, ie missing configuration.
	 */
	public InvoiceDeliveryBatch startBatch(InvoiceList invoices, Properties props) throws Exception;

}
