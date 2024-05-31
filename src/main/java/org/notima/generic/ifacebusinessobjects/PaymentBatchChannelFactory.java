package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.TaxSubjectIdentifier;

public interface PaymentBatchChannelFactory {

	public List<PaymentBatchChannel> listChannelsForTenant(TaxSubjectIdentifier tenant);
	
	public List<PaymentBatchChannel> listChannelsWithSourceSystem(String systemName);
	
	public List<PaymentBatchChannel> listChannelsWithDestinationSystem(String systemName);

	public PaymentBatchChannel persistChannel(PaymentBatchChannel pbc);
	
	public String getSystemName();
	
	
}
