package org.notima.generic.ifacebusinessobjects;

import java.io.PrintStream;

import org.notima.generic.businessobjects.PaymentBatch;
import org.notima.generic.businessobjects.PaymentBatchProcessOptions;
import org.notima.generic.businessobjects.PaymentBatchProcessResult;

public interface PaymentBatchProcessor {
	
	/**
	 * Goes through a PaymentBatch and sets the MatchedInvoiceNo to an existing 
	 * destination system invoice if a match is found.
	 * 
	 * @param 	report		The report to be processed / matched.
	 * @param processOptions  Options to consider.
	 * @return	The updated payment report.
	 * @throws Exception	If something goes wrong.
	 */
	public PaymentBatch lookupInvoiceReferences(PaymentBatch report, PaymentBatchProcessOptions processOptions) throws Exception;	
	
	/**
	 * Processes a payment batch
	 * Creates payments and vouchers unless dry run is enabled in the provisioning details. 
	 * 
	 * @param 		report	The batch to be processed / matched.
	 * @param		options	Additional properties to control the payment processing
	 * @return		A result structure describing the success / stats / failure of the processing.
	 * @throws Exception 
	 */
	public PaymentBatchProcessResult processPaymentBatch(PaymentBatch report, PaymentBatchProcessOptions options) throws Exception;	
	
	/**
	 * 
	 * @return		System name for this processor.
	 */
	public String getSystemName();
	
	/**
	 * Sets output stream for this payment report processor.
	 * If the output stream is set, this is where messages will be output.
	 * This is to improve the experience when running using console.
	 * 
	 * @param os	Output stream
	 */
	public void setOutput(PrintStream os);
	
	
}
