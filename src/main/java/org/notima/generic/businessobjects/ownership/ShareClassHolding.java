package org.notima.generic.businessobjects.ownership;

import java.math.BigDecimal;

/**
 * The shares of one share class (aktieslag) within an {@link OwnershipStake}.
 */
public class ShareClassHolding {

	private String		shareClass;
	private long		shares;
	private BigDecimal	votesPerShare;

	public ShareClassHolding() {}

	public ShareClassHolding(String shareClass, long shares, BigDecimal votesPerShare) {
		this.shareClass = shareClass;
		this.shares = shares;
		this.votesPerShare = votesPerShare;
	}

	/** @return	The share class code, e.g. "A" or "B"; null for shares of no defined class. */
	public String getShareClass() {
		return shareClass;
	}

	public void setShareClass(String shareClass) {
		this.shareClass = shareClass;
	}

	public long getShares() {
		return shares;
	}

	public void setShares(long shares) {
		this.shares = shares;
	}

	/** @return	The vote value (röstvärde) of one share of the class, e.g. 1 or 0.1. */
	public BigDecimal getVotesPerShare() {
		return votesPerShare;
	}

	public void setVotesPerShare(BigDecimal votesPerShare) {
		this.votesPerShare = votesPerShare;
	}

	/** @return	The votes of these shares: shares &times; votes per share (1 if not set). */
	public BigDecimal getVotes() {
		BigDecimal v = votesPerShare != null ? votesPerShare : BigDecimal.ONE;
		return v.multiply(BigDecimal.valueOf(shares));
	}

}
