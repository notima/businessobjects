package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.PaymentBatch;

/**
 * Interface to deal with payments (remittance and reconciliation).
 * 
 * @author Daniel Tamm
 *
 */
public interface PaymentFactory {

	/**
	 * Reads payments from a source. The source format is determined by the implementation.
	 * 
	 * @param 		source			 
	 * @return		A list of payment batches
	 */
	public List<PaymentBatch> readPaymentBatchesFromSource(String source) throws Exception;
	
	public String getSystemName();
	
	
}
