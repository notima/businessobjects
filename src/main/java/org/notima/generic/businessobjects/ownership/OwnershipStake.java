package org.notima.generic.businessobjects.ownership;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * An owner's shares in a company at a given date, as recorded in the company's share
 * register (aktiebok).
 *
 * <p>The owner's part of the company is given both as part of the capital
 * ({@link #getCapitalFraction()}, shares of all shares) and of the votes
 * ({@link #getVoteFraction()}). Without share classes every share has one vote and the two
 * are equal.</p>
 */
public class OwnershipStake {

	static final MathContext MC = MathContext.DECIMAL64;

	private EntityRef		owner;
	private EntityRef		company;
	private LocalDate		asOf;
	private long			shares;
	private long			totalShares;
	private BigDecimal		votes;
	private BigDecimal		totalVotes;
	private List<ShareClassHolding>	classHoldings;
	private String			source;

	public OwnershipStake() {}

	public OwnershipStake(EntityRef owner, EntityRef company, LocalDate asOf, long shares, long totalShares) {
		this.owner = owner;
		this.company = company;
		this.asOf = asOf;
		this.shares = shares;
		this.totalShares = totalShares;
	}

	/** @return	The owner's part of the share capital, 0&ndash;1; 0 if the total is unknown. */
	public BigDecimal getCapitalFraction() {
		if (totalShares <= 0) return BigDecimal.ZERO;
		return BigDecimal.valueOf(shares).divide(BigDecimal.valueOf(totalShares), MC);
	}

	/**
	 * @return	The owner's part of the votes, 0&ndash;1. Equals the
	 * 			{@link #getCapitalFraction() capital fraction} when votes are not given.
	 */
	public BigDecimal getVoteFraction() {
		if (votes == null || totalVotes == null) return getCapitalFraction();
		if (totalVotes.signum() <= 0) return BigDecimal.ZERO;
		return votes.divide(totalVotes, MC);
	}

	public EntityRef getOwner() {
		return owner;
	}

	public void setOwner(EntityRef owner) {
		this.owner = owner;
	}

	/** @return	The company whose shares are owned. */
	public EntityRef getCompany() {
		return company;
	}

	public void setCompany(EntityRef company) {
		this.company = company;
	}

	public LocalDate getAsOf() {
		return asOf;
	}

	public void setAsOf(LocalDate asOf) {
		this.asOf = asOf;
	}

	public long getShares() {
		return shares;
	}

	public void setShares(long shares) {
		this.shares = shares;
	}

	/** @return	All the company's shares at {@link #getAsOf()}. */
	public long getTotalShares() {
		return totalShares;
	}

	public void setTotalShares(long totalShares) {
		this.totalShares = totalShares;
	}

	/** @return	The owner's votes; null if share classes are not used (one vote per share). */
	public BigDecimal getVotes() {
		return votes;
	}

	public void setVotes(BigDecimal votes) {
		this.votes = votes;
	}

	/** @return	All votes in the company; null if share classes are not used. */
	public BigDecimal getTotalVotes() {
		return totalVotes;
	}

	public void setTotalVotes(BigDecimal totalVotes) {
		this.totalVotes = totalVotes;
	}

	/** @return	The owner's shares per share class; never null, empty without classes. */
	public List<ShareClassHolding> getClassHoldings() {
		if (classHoldings == null) classHoldings = new ArrayList<>();
		return classHoldings;
	}

	public void setClassHoldings(List<ShareClassHolding> classHoldings) {
		this.classHoldings = classHoldings;
	}

	/** @return	Where the stake was read from, e.g. the path of the share register file. */
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return owner + " -> " + company + ": " + shares + "/" + totalShares;
	}

}
