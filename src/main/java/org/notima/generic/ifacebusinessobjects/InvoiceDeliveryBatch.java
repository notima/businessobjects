package org.notima.generic.ifacebusinessobjects;

import java.io.File;

import org.notima.generic.businessobjects.Invoice;

/**
 * One delivery of invoices, created by {@link InvoiceDeliveryMethod#startBatch}.
 *
 * Depending on the method, an invoice is sent when delivered (ie e-mail) or queued
 * and sent when the batch is completed (ie Ekopost).
 */
public interface InvoiceDeliveryBatch {

	/**
	 * Delivers (or queues) an invoice.
	 *
	 * @param invoice			The invoice.
	 * @param formattedInvoice	The invoice formatted in the method's format, ie the pdf or peppol file.
	 * @return	A short description of what was done, ie "sent to a@b.se".
	 * 			Null if the method doesn't deliver this invoice (ie the customer doesn't want e-mail invoices).
	 * @throws Exception	If the delivery fails.
	 */
	public String deliver(Invoice<?> invoice, File formattedInvoice) throws Exception;

	/**
	 * Sends what's queued and ends the batch.
	 *
	 * @return	The number of invoices sent in the batch.
	 * @throws Exception	If sending fails.
	 */
	public int complete() throws Exception;

}
