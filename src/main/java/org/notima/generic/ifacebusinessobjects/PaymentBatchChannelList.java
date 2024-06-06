package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.TaxSubjectIdentifier;

public interface PaymentBatchChannelList {

	public List<PaymentBatchChannel> getChannelList();
	
	public List<PaymentBatchChannel> listChannelsForTenant(TaxSubjectIdentifier tenant);
	
}
