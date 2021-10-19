package org.notima.generic.ifacebusinessobjects;

import java.io.File;

import org.notima.generic.businessobjects.PaymentBatch;

/**
 * Interface to deal with payments (remittance and reconciliation).
 * 
 * @author Daniel Tamm
 *
 */
public interface PaymentFactory {

	/**
	 * Reads a payment file from file. The file format is determined by the implementation.
	 * 
	 * @param 		file		A valid file.
	 * @return		A payment batch
	 */
	public PaymentBatch readPaymentBatchFromFile(File file) throws Exception;
	
}
