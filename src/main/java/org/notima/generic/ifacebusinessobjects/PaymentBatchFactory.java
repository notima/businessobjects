package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.PaymentBatch;

public interface PaymentBatchFactory {

	/**
	 * Sets the source for the payment batches. Depends on the implementation.
	 * 
	 * @param source		The source. Could be a file directory.
	 */
	public void setSource(String source) throws Exception;
	
	/**
	 * Reads the payment batches not yet read from the source.
	 * 
	 * @return	A list of payment batches (if any).
	 */
	public List<PaymentBatch> readPaymentBatches();
	
	public String getSystemName();
	
}
