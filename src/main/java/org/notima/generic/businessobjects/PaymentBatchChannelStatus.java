package org.notima.generic.businessobjects;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentBatchChannelStatus {

	private LocalDate		reconciledUntil;
	private LocalDateTime	lastRun;
	
	private String			lastProcessedBatch;

	private int				maxUnresolvedTrx;

	
	public LocalDate getReconciledUntil() {
		return reconciledUntil;
	}

	public void setReconciledUntil(LocalDate reconciledUntil) {
		this.reconciledUntil = reconciledUntil;
	}

	public LocalDateTime getLastRun() {
		return lastRun;
	}

	public void setLastRun(LocalDateTime lastRun) {
		this.lastRun = lastRun;
	}

	public String getLastProcessedBatch() {
		return lastProcessedBatch;
	}

	public void setLastProcessedBatch(String lastProcessedBatch) {
		this.lastProcessedBatch = lastProcessedBatch;
	}

	public int getMaxUnresolvedTrx() {
		return maxUnresolvedTrx;
	}

	public void setMaxUnresolvedTrx(int maxUnresolvedTrx) {
		this.maxUnresolvedTrx = maxUnresolvedTrx;
	}
	
	
}
