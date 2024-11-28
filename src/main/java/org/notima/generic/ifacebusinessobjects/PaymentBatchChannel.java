package org.notima.generic.ifacebusinessobjects;

import java.time.LocalDate;
import java.util.List;

import org.notima.generic.businessobjects.PaymentBatchChannelOptions;
import org.notima.generic.businessobjects.PaymentBatchChannelStatus;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;

/**
 * This interface represents a payment batch channel. 
 * 
 * This means that we have a source payment batch factory and a destination for the batches.
 * 
 */
public interface PaymentBatchChannel {

	public String getChannelId();
	
	public void setChannelId(String id);
	
	public TaxSubjectIdentifier getTenant();
	
	public void setTenant(TaxSubjectIdentifier tenant);
	
	public String getChannelDescription();
	
	public void setChannelDescription(String description);
	
	public String getDestinationSystem();
	
	public void setDestinationSystem(String dsystem);
	
	public String getSourceSystem();

	public void setSourceSystem(String ssystem);
	
	/**
	 * A list of unprocessed entries
	 * @return
	 */
	public List<String> getUnprocessedEntries();

	public void setUnprocessedEntries(List<String> entries);
	
	public PaymentBatchChannelOptions getOptions();
	
	public void setPaymentBatchChannelOptions(PaymentBatchChannelOptions opts);
	
	public void parseDestinationOptions(String options);
	
	public void parseSourceOptions(String options);
	
	public PaymentBatchChannelStatus getStatus();
	
	public void setStatus(PaymentBatchChannelStatus status);
	
	public void setReconciledUntil(LocalDate ld);
	
	public void setLastProcessedBatch(String batchname);

	
}
