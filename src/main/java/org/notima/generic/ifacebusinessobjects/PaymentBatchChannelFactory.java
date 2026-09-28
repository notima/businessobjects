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
	
	public PaymentBatchChannel findChannelByDescription(String desc);

	/**
	 * Looks up a channel using its ID and, if not found, its description.
	 *
	 * @param idOrDesc		The channel ID or description.
	 * @return	The channel or null if not found.
	 */
	public default PaymentBatchChannel findChannelWithIdOrDescription(String idOrDesc) {
		PaymentBatchChannel channel = findChannelWithId(idOrDesc);
		if (channel==null) {
			channel = findChannelByDescription(idOrDesc);
		}
		return channel;
	}
	
	public List<PaymentBatchChannel> findChannelsBySource(String source);
	
	public PaymentBatchChannel persistChannel(PaymentBatchChannel pbc) throws IOException;
	
	public String getSystemName();
	
	
}
