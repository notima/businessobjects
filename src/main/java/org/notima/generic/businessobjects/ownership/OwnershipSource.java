package org.notima.generic.businessobjects.ownership;

import java.time.LocalDate;
import java.util.List;

/**
 * Provides the ownership and the loans of a set of companies, e.g. read from their share
 * register files or from a database. Feed the result to {@link OwnershipGraph} and
 * {@link LoanVerifier}.
 */
public interface OwnershipSource {

	/**
	 * @param asOf	The date; null for the current state.
	 * @return	The owners of every company this source covers, at {@code asOf}.
	 */
	List<OwnershipStake> getStakes(LocalDate asOf);

	/**
	 * @param asOf	The date outstanding amounts are calculated at; null for today.
	 * @return	The loans recorded by every company this source covers. Each loan appears
	 * 			once for each party that has recorded it.
	 */
	List<IntercompanyLoan> getLoans(LocalDate asOf);

	/**
	 * @return	The companies this source covers: those whose own records (share register,
	 * 			loans) it reads. A loan to a party outside this list can't have a
	 * 			counterpart record.
	 */
	List<EntityRef> getCoveredCompanies();

}
