package org.notima.generic.businessobjects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.notima.generic.ifacebusinessobjects.PaymentBatchChannel;

public class BasicPaymentBatchChannel implements PaymentBatchChannel {

	private String channelId;
	private TaxSubjectIdentifier tenant;
	private String	destinationSystem;
	private String	sourceSystem;
	private String	channelDescription;
	private PaymentBatchChannelOptions options;
	private PaymentBatchChannelStatus status;
	private List<String>	unprocessedEntries = new ArrayList<String>();
	
	@Override
	public String getChannelId() {
		return channelId;
	}

	@Override
	public void setChannelId(String id) {
		channelId = id;
	}

	@Override
	public TaxSubjectIdentifier getTenant() {
		return tenant;
	}

	@Override
	public void setTenant(TaxSubjectIdentifier tenant) {
		this.tenant = tenant;
	}

	@Override
	public String getChannelDescription() {
		return channelDescription;
	}

	@Override
	public void setChannelDescription(String description) {
		channelDescription = description;
	}

	@Override
	public String getDestinationSystem() {
		return destinationSystem;
	}

	@Override
	public void setDestinationSystem(String dsystem) {
		destinationSystem = dsystem;
	}

	@Override
	public String getSourceSystem() {
		return sourceSystem;
	}

	@Override
	public void setSourceSystem(String ssystem) {
		sourceSystem = ssystem;
	}

	@Override
	public PaymentBatchChannelOptions getOptions() {
		return options;
	}

	@Override
	public void setPaymentBatchChannelOptions(PaymentBatchChannelOptions opts) {
		options = opts;
	}

	@Override
	public void parseDestinationOptions(String options) {
	}

	protected void setDestinationOption(String key, String value) {
		initOptions();
	}

	protected void setSourceOption(String key, String value) {
		initOptions();
		options.setSourceProperty(key, value);
	}
	
	private void initOptions() {
		if (options==null) {
			options = new PaymentBatchChannelOptions();
		}
	}
	
	@Override
	public void parseSourceOptions(String options) {
		initOptions();
		this.options.setSourceDirectory(options);
	}

	@Override
	public PaymentBatchChannelStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(PaymentBatchChannelStatus status) {
		this.status = status;
	}

	@Override
	public void setReconciledUntil(LocalDate ld) {
		if (status==null) {
			status = new PaymentBatchChannelStatus();
		}
		status.setReconciledUntil(ld);
	}

	public void setLastProcessedBatch(String batch) {
		if (status==null) {
			status = new PaymentBatchChannelStatus();
		}
		status.setLastProcessedBatch(batch);
	}
	
	@Override
	public List<String> getUnprocessedEntries() {
		return unprocessedEntries;
	}

	@Override
	public void setUnprocessedEntries(List<String> entries) {
		unprocessedEntries = entries;
	}
	
	
}
