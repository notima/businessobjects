package org.notima.generic.ifacebusinessobjects;

import org.notima.generic.businessobjects.PaymentBatchChannelOptions;

/**
 * This interface represents a payment batch channel. 
 * 
 * This means that we have a source payment batch factory and a destination for the batches.
 * 
 */
public interface PaymentBatchChannel {

	public String getChannelId();
	
	public String getChannelDescription();
	
	public String getDestinationSystem();
	
	public String getSourceSystem();

	public PaymentBatchChannelOptions getOptions();
	
}
