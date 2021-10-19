package org.notima.generic.ifacebusinessobjects;

import java.io.PrintStream;
import java.util.Properties;

import org.notima.generic.businessobjects.PaymentBatch;
import org.notima.generic.businessobjects.PaymentBatchProcessResult;

public interface PaymentBatchProcessor {

	// Property to say that only payout should be accounted (nothing else)
	public static String PROP_ACCOUNT_PAYOUT_ONLY = "accountPayoutOnly";
	// Property to say that only account fees should be accounted (nothing else)
	public static String PROP_ACCOUNT_FEES_ONLY = "accountFeesOnly";
	// Property to say that only this account / payment type should be processed
	public static String PROP_ACCOUNTTYPE_ONLY = "onlyAccountType";
	// Property to say that only this account reference should be processed
	public static String PROP_ACCOUNTREF_ONLY = "onlyAccountReference";
	// Property to say that only this transaction should be processed
	public static String PROP_ONLY_TRX_NUMBER = "onlyTrxNumber";
	// Property to say this is a dryRun no matter what
	public static String PROP_DRYRUN = "dryRun";
	// Property to say that the file should be kept after processing (not moved)
	public static String PROP_KEEP_FILE = "keepFile";
	// Property for output directory
	public static String PROP_OUTPUTDIR = "outputDirectory";
	// Property for output file prefix
	public static String PROP_OUTPUTFILE_PREFIX = "outputFilePrefix";
	// Property for debugging org no (output is redirected here)
	public static String PROP_DEBUG_ORGNO = "debugOrgNo";
	
	/**
	 * Goes through a PaymentBatch and sets PaymentReportDetail.REF_CLIENT_INVOICE_NO to an existing 
	 * destination system invoice if a match is found.
	 * 
	 * @param 	report		The report to be processed / matched.
	 * @return	The updated payment report.
	 * @throws Exception	If something goes wrong.
	 */
	public PaymentBatch lookupInvoiceReferences(PaymentBatch report) throws Exception;	
	
	/**
	 * Processes a svea webpay payment report.
	 * Creates payments and vouchers unless dry run is enabled in the provisioning details. 
	 * 
	 * @param 		report	The report to be processed / matched.
	 * @param		props	Additional properties to control the payment processing
	 * @return		A result structure describing the success / stats / failure of the processing.
	 * @throws Exception 
	 */
	public PaymentBatchProcessResult processPaymentBatch(PaymentBatch report, Properties props) throws Exception;	
	
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
