package org.notima.generic.ifacebusinessobjects;

import java.io.IOException;
import java.util.List;

import org.notima.generic.businessobjects.TaxSubjectIdentifier;

public interface PaymentBatchChannelFactory {

	public void populateUnprocessedEntries(boolean flag);
	
	public boolean isPopulateUnprocessedEntries();
	
	public List<PaymentBatchChannel> listChannelsForTenant(TaxSubjectIdentifier tenant);
	
	public List<PaymentBatchChannel> listChannelsWithSourceSystem(String systemName);
	
	public List<PaymentBatchChannel> listChannelsWithDestinationSystem(String systemName);

	public PaymentBatchChannel findChannelWithId(String id);
	
	public PaymentBatchChannel persistChannel(PaymentBatchChannel pbc) throws IOException;
	
	public String getSystemName();
	
	
}
