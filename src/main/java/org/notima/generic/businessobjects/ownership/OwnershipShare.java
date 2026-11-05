package org.notima.generic.businessobjects.ownership;

import java.math.BigDecimal;

/**
 * A part of a company, as parts of its capital and of its votes (each 0&ndash;1). Used for
 * indirect ownership, where the parts along a chain of owners are multiplied.
 */
public class OwnershipShare {

	public static final OwnershipShare NONE = new OwnershipShare(BigDecimal.ZERO, BigDecimal.ZERO);
	public static final OwnershipShare ALL = new OwnershipShare(BigDecimal.ONE, BigDecimal.ONE);

	private final BigDecimal	capital;
	private final BigDecimal	votes;

	public OwnershipShare(BigDecimal capital, BigDecimal votes) {
		this.capital = capital;
		this.votes = votes;
	}

	/** The direct share a stake gives. */
	public static OwnershipShare of(OwnershipStake stake) {
		return new OwnershipShare(stake.getCapitalFraction(), stake.getVoteFraction());
	}

	/** This share of a company that holds {@code other} of another company. */
	public OwnershipShare times(OwnershipShare other) {
		return new OwnershipShare(capital.multiply(other.capital, OwnershipStake.MC),
				votes.multiply(other.votes, OwnershipStake.MC));
	}

	public OwnershipShare plus(OwnershipShare other) {
		return new OwnershipShare(capital.add(other.capital, OwnershipStake.MC),
				votes.add(other.votes, OwnershipStake.MC));
	}

	/** @return	The part of the share capital, 0&ndash;1. */
	public BigDecimal getCapital() {
		return capital;
	}

	/**
	 * @return	The part of the votes, 0&ndash;1. For indirect ownership this is the product
	 * 			of the vote fractions along the chain, not a measure of control; see
	 * 			{@link OwnershipGraph#getSubsidiaries(EntityRef)} for control.
	 */
	public BigDecimal getVotes() {
		return votes;
	}

	@Override
	public String toString() {
		return "capital " + capital.toPlainString() + ", votes " + votes.toPlainString();
	}

}
