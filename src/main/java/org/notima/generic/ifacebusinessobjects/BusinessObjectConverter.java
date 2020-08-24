package org.notima.generic.ifacebusinessobjects;

import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.Order;

public interface BusinessObjectConverter<O,I> {

	/**
	 * Converts an order to an invoice
	 * 
	 * @param src
	 * @return
	 */
	public Invoice<I> toInvoice(Order<O> src);
	
	/**
	 * Converts an invoice to an order
	 * 
	 * @param src
	 * @return
	 */
	public Order<O> toOrder(Invoice<I> src);	
	
	/**
	 * Converts a native invoice to business object invoice
	 * 
	 * @param src
	 * @return
	 */
	public Invoice<I> fromNativeInvoice(I src) throws Exception;
	
	/**
	 * Converts an invoice to native invoice
	 */
	public I toNativeInvoice(Invoice<I> src) throws Exception;
	
	/**
	 * Converts a native invoice to business object invoice
	 * 
	 * @param src
	 * @return
	 */
	public O toNativeOrder(Order<?> src) throws Exception;

	/**
	 * Converts the native invoice to a string representation.
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public String nativeInvoiceToString(I src) throws Exception;
	
	public String getSystemName();
	
}
