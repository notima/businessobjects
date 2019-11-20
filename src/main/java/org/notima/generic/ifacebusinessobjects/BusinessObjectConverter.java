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
	public Invoice<I> fromNativeInvoice(Object src) throws Exception;
	
}
