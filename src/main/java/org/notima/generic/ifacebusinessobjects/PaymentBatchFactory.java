package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.PaymentBatch;

/**
 * This interface defines a class that creates payment batches using the specified source
 * for a specified system.
 * 
 */
public interface PaymentBatchFactory {

	/**
	 * Sets the source for the payment batches. Depends on the implementation.
	 * 
	 * @param source		The source. Could be a file directory.
	 */
	public void setSource(String source) throws Exception;
	
	/**
	 * Sets the destination for payment batches. Depends on the implementation.
	 * 
	 * @param dest			The destination. Format depents on implementation.
	 * @throws Exception
	 */
	public void setDestination(String dest) throws Exception;
	
	/**
	 * Reads the payment batches not yet read from the source.
	 * 
	 * @return	A list of payment batches (if any).
	 */
	public List<PaymentBatch> readPaymentBatches();
	
	/**
	 * Writes a payment batch to the destination.
	 * 
	 * @param batch			The batch to write.
	 * @return				The batch that was written.
	 * @throws Exception
	 */
	public PaymentBatch writePaymentBatch(PaymentBatch batch) throws Exception;
	
	/**
	 * 
	 * @return	Name of the system that implements this batch factory.
	 */
	public String getSystemName();
	
}
