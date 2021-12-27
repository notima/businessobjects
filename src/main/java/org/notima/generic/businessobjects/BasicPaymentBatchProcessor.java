package org.notima.generic.businessobjects;

import java.io.PrintStream;

import org.notima.generic.ifacebusinessobjects.PaymentBatchProcessor;

public abstract class BasicPaymentBatchProcessor implements PaymentBatchProcessor {
	
	protected PrintStream outStream;
	
	@Override
	public void setOutput(PrintStream os) {
		outStream = os;
	}
	

}
