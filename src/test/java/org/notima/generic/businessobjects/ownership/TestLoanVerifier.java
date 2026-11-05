package org.notima.generic.businessobjects.ownership;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestLoanVerifier {

	private final EntityRef parent = EntityRef.organization("o1", "556000-0001", "Moder AB");
	private final EntityRef sub = EntityRef.organization("o2", "556000-0002", "Dotter AB");
	private final EntityRef bank = EntityRef.organization("o9", "502000-0009", "Banken");
	private final List<EntityRef> covered = Arrays.asList(parent, sub);

	private static IntercompanyLoan loan(EntityRef lender, EntityRef borrower, EntityRef recordedBy,
			String principal, String outstanding, String agreementId) {
		IntercompanyLoan l = new IntercompanyLoan();
		l.setLender(lender);
		l.setBorrower(borrower);
		l.setRecordedBy(recordedBy);
		l.setCurrency("SEK");
		l.setPrincipal(new BigDecimal(principal));
		l.setOutstanding(outstanding != null ? new BigDecimal(outstanding) : null);
		l.setStartDate(LocalDate.of(2025, 1, 1));
		l.setStatus(IntercompanyLoan.STATUS_ACTIVE);
		l.setAgreementId(agreementId);
		return l;
	}

	@Test
	public void matchingRecordsHaveNoDiscrepancies() {
		List<LoanDiscrepancy> d = new LoanVerifier().verify(Arrays.asList(
				loan(parent, sub, parent, "100000", "80000", null),
				loan(parent, sub, sub, "100000.00", "80000", null)), covered);
		assertTrue(d.isEmpty(), d.toString());
	}

	@Test
	public void differingOutstandingIsReported() {
		List<LoanDiscrepancy> d = new LoanVerifier().verify(Arrays.asList(
				loan(parent, sub, parent, "100000", "80000", "a1"),
				loan(parent, sub, sub, "100000", "90000", "a1")), covered);
		assertEquals(1, d.size());
		assertEquals(LoanDiscrepancy.Type.OUTSTANDING, d.get(0).getType());
		assertEquals(0, new BigDecimal("80000").compareTo((BigDecimal) d.get(0).getValue()));
	}

	@Test
	public void missingCounterpartOnlyForCoveredParties() {
		List<LoanDiscrepancy> d = new LoanVerifier().verify(Arrays.asList(
				loan(parent, sub, parent, "100000", null, null),
				loan(bank, sub, sub, "500000", null, null)), covered);
		assertEquals(1, d.size());
		assertEquals(LoanDiscrepancy.Type.MISSING_COUNTERPART, d.get(0).getType());
		assertTrue(d.get(0).getLoan().getBorrower().matches(sub));
	}

	@Test
	public void agreementIdsDecidePairing() {
		// Two loans with equal amounts; the agreement ids pair them crosswise
		IntercompanyLoan l1 = loan(parent, sub, parent, "100", "100", "x");
		IntercompanyLoan l2 = loan(parent, sub, parent, "100", "50", "y");
		IntercompanyLoan b1 = loan(parent, sub, sub, "100", "50", "y");
		IntercompanyLoan b2 = loan(parent, sub, sub, "100", "100", "x");
		assertTrue(new LoanVerifier().verify(Arrays.asList(l1, l2, b1, b2), covered).isEmpty());
	}

	@Test
	public void differentAgreementIdsAreNotPaired() {
		List<LoanDiscrepancy> d = new LoanVerifier().verify(Arrays.asList(
				loan(parent, sub, parent, "100", null, "x"),
				loan(parent, sub, sub, "100", null, "y")), covered);
		assertEquals(2, d.size());
		assertTrue(d.stream().allMatch(x -> x.getType() == LoanDiscrepancy.Type.MISSING_COUNTERPART));
	}

	@Test
	public void lastResortPairingReportsDifferences() {
		IntercompanyLoan l = loan(parent, sub, parent, "100", null, null);
		IntercompanyLoan b = loan(parent, sub, sub, "120", null, null);
		b.setStartDate(LocalDate.of(2025, 2, 1));
		List<LoanDiscrepancy> d = new LoanVerifier().verify(Arrays.asList(l, b), covered);
		assertEquals(2, d.size());
		assertEquals(LoanDiscrepancy.Type.PRINCIPAL, d.get(0).getType());
		assertEquals(LoanDiscrepancy.Type.START_DATE, d.get(1).getType());
	}

}
