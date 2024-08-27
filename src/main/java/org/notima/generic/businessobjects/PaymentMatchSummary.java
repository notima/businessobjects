package org.notima.generic.businessobjects;

import java.time.LocalDate;

/**
 *	Record class for a payment match summary 
 */
public class PaymentMatchSummary {

	private LocalDate	trxDate;
	private String		reference;
	private int			totalCount;
	private int			matchedCount;
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getMatchedCount() {
		return matchedCount;
	}
	public void setMatchedCount(int matchedCount) {
		this.matchedCount = matchedCount;
	}
	public LocalDate getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(LocalDate trxDate) {
		this.trxDate = trxDate;
	}
	
	public int getUnmatchedCount() {
		return (totalCount - matchedCount);
	}
	
	public double getMatchedRatio() {
		return ((double)matchedCount / (double)totalCount);
	}
	
	public double getUnmatchedRatio() {
		return ((double)1)-getMatchedRatio();
	}
	
	/**
	 * Resets the counters
	 */
	public void reset() {
		matchedCount = 0;
		totalCount = 0;
	}
	
	/**
	 * Adds to total count and matched count.
	 * 
	 */
	public void addMatched() {
		matchedCount++;
		totalCount++;
	}
	
	/**
	 * Adds only to total count
	 */
	public void addUnmatched() {
		totalCount++;
	}
	
}
