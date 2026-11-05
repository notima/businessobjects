package org.notima.generic.businessobjects.ownership;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Checks that loans between parties are recorded the same way by both: the lender's claim
 * against the borrower's debt.
 *
 * <p>Records are paired when they have the same lender and borrower, were recorded by
 * different parties, and either have the same {@link IntercompanyLoan#getAgreementId()
 * agreement id} or &mdash; when either lacks one &mdash; the same currency, principal and
 * start date (failing that, currency and principal, or as a last resort any remaining record
 * between the two parties, whose differences are then reported). Records with different agreement ids are never paired.</p>
 */
public class LoanVerifier {

	/**
	 * @param loans		The loan records of all covered parties, e.g. from
	 * 					{@link OwnershipSource#getLoans(java.time.LocalDate)}.
	 * @param covered	The parties whose records are included; a missing counterpart is
	 * 					only reported when the counterparty is one of them.
	 * @return	The discrepancies found; empty if all records agree.
	 */
	public List<LoanDiscrepancy> verify(Collection<IntercompanyLoan> loans, Collection<EntityRef> covered) {
		List<LoanDiscrepancy> result = new ArrayList<>();
		List<IntercompanyLoan> all = new ArrayList<>();
		for (IntercompanyLoan l : loans) {
			if (l != null && l.getLender() != null && l.getBorrower() != null && l.getRecordedBy() != null)
				all.add(l);
		}
		Set<IntercompanyLoan> paired = Collections.newSetFromMap(new IdentityHashMap<>());

		// Pass 1: agreement ids; 2: currency, principal and start date; 3: currency and
		// principal; 4: any remaining record between the same parties
		for (int pass = 1; pass <= 4; pass++) {
			for (IntercompanyLoan loan : all) {
				if (paired.contains(loan) || !loan.isRecordedByLender()) continue;
				IntercompanyLoan match = null;
				for (IntercompanyLoan other : all) {
					if (other == loan || paired.contains(other) || other.isRecordedByLender()) continue;
					if (!sameParties(loan, other)) continue;
					if (matches(loan, other, pass)) { match = other; break; }
				}
				if (match != null) {
					paired.add(loan);
					paired.add(match);
					compare(loan, match, result);
				}
			}
		}

		for (IntercompanyLoan loan : all) {
			if (paired.contains(loan)) continue;
			EntityRef counterparty = loan.getCounterparty();
			if (isCovered(counterparty, covered))
				result.add(new LoanDiscrepancy(LoanDiscrepancy.Type.MISSING_COUNTERPART, loan, null, null, null));
		}
		return result;
	}

	private static boolean sameParties(IntercompanyLoan a, IntercompanyLoan b) {
		return a.getLender().matches(b.getLender()) && a.getBorrower().matches(b.getBorrower())
				&& !a.getRecordedBy().matches(b.getRecordedBy());
	}

	private static boolean matches(IntercompanyLoan a, IntercompanyLoan b, int pass) {
		String ida = blankToNull(a.getAgreementId());
		String idb = blankToNull(b.getAgreementId());
		if (ida != null && idb != null) return pass == 1 && ida.equals(idb);
		switch (pass) {
			case 2:
				return Objects.equals(a.getCurrency(), b.getCurrency()) && sameAmount(a.getPrincipal(), b.getPrincipal())
						&& Objects.equals(a.getStartDate(), b.getStartDate());
			case 3:
				return Objects.equals(a.getCurrency(), b.getCurrency()) && sameAmount(a.getPrincipal(), b.getPrincipal());
			case 4:
				return true;
			default:
				return false;
		}
	}

	private static void compare(IntercompanyLoan a, IntercompanyLoan b, List<LoanDiscrepancy> result) {
		if (!Objects.equals(a.getCurrency(), b.getCurrency()))
			result.add(new LoanDiscrepancy(LoanDiscrepancy.Type.CURRENCY, a, b, a.getCurrency(), b.getCurrency()));
		if (!sameAmount(a.getPrincipal(), b.getPrincipal()))
			result.add(new LoanDiscrepancy(LoanDiscrepancy.Type.PRINCIPAL, a, b, a.getPrincipal(), b.getPrincipal()));
		if (!Objects.equals(a.getStartDate(), b.getStartDate()))
			result.add(new LoanDiscrepancy(LoanDiscrepancy.Type.START_DATE, a, b, a.getStartDate(), b.getStartDate()));
		if (a.getOutstanding() != null && b.getOutstanding() != null && !sameAmount(a.getOutstanding(), b.getOutstanding()))
			result.add(new LoanDiscrepancy(LoanDiscrepancy.Type.OUTSTANDING, a, b, a.getOutstanding(), b.getOutstanding()));
		if (!Objects.equals(a.getStatus(), b.getStatus()))
			result.add(new LoanDiscrepancy(LoanDiscrepancy.Type.STATUS, a, b, a.getStatus(), b.getStatus()));
		if (!Objects.equals(a.getClosedDate(), b.getClosedDate()))
			result.add(new LoanDiscrepancy(LoanDiscrepancy.Type.CLOSED_DATE, a, b, a.getClosedDate(), b.getClosedDate()));
	}

	private static boolean sameAmount(BigDecimal a, BigDecimal b) {
		if (a == null || b == null) return a == b;
		return a.compareTo(b) == 0;
	}

	private static boolean isCovered(EntityRef ref, Collection<EntityRef> covered) {
		if (ref == null || covered == null) return false;
		for (EntityRef c : covered) {
			if (c != null && c.matches(ref)) return true;
		}
		return false;
	}

	private static String blankToNull(String s) {
		return s == null || s.trim().isEmpty() ? null : s.trim();
	}

}
