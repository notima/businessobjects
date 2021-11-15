package org.notima.generic.businessobjects;

import java.io.PrintStream;
import java.util.Properties;

import org.notima.generic.ifacebusinessobjects.PaymentBatchProcessor;

public abstract class BasicPaymentBatchProcessor implements PaymentBatchProcessor {

	// Get options
	protected boolean accountPayoutOnly;
	protected boolean accountFeeOnly;
	protected String accountTypeOnly;
	protected String accountRefOnly;
	protected Integer trxNoOnly;
	protected boolean dryRunProperty;
	protected String debugOrgNo;
	protected boolean keepFile;
	
	protected Properties props;
	
	protected PrintStream outStream;
	
	@Override
	public void setOutput(PrintStream os) {
		outStream = os;
	}
	
	protected void setOptionsFromProperties(Properties props) {
		
		if (props==null) {
			// Create an empty props to use for setting default below
			props = new Properties();
		}
		
		// Get options
		accountPayoutOnly = props.get(PaymentBatchProcessor.PROP_ACCOUNT_PAYOUT_ONLY)!=null ? (Boolean)props.get(PaymentBatchProcessor.PROP_ACCOUNT_PAYOUT_ONLY) : false;
		accountFeeOnly = props.get(PaymentBatchProcessor.PROP_ACCOUNT_FEES_ONLY)!=null ? (Boolean)props.get(PaymentBatchProcessor.PROP_ACCOUNT_FEES_ONLY) : false;
		accountTypeOnly = (String)props.get(PaymentBatchProcessor.PROP_ACCOUNTTYPE_ONLY);
		accountRefOnly = (String)props.get(PaymentBatchProcessor.PROP_ACCOUNTREF_ONLY);
		trxNoOnly = (Integer)props.get(PaymentBatchProcessor.PROP_ONLY_TRX_NUMBER);
		dryRunProperty = props.get(PaymentBatchProcessor.PROP_DRYRUN)!=null ? (Boolean)props.get(PaymentBatchProcessor.PROP_DRYRUN) : false;
		debugOrgNo = (String)props.get(PaymentBatchProcessor.PROP_DEBUG_ORGNO);
		keepFile = props.get(PaymentBatchProcessor.PROP_KEEP_FILE)!=null ? (Boolean)props.get(PaymentBatchProcessor.PROP_KEEP_FILE) : false;
		
	}
	

}
