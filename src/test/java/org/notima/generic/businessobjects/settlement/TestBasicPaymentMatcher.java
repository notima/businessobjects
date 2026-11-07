package org.notima.generic.businessobjects.settlement;

import static org.junit.jupiter.api.Assertions.*;
import static org.notima.generic.businessobjects.settlement.SettlementFixtures.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.Payment;
import org.notima.generic.businessobjects.settlement.PaymentMatch.Confidence;

public class TestBasicPaymentMatcher {

	private final PaymentMatcher matcher = new BasicPaymentMatcher();

	private static AllocationLedger ledger(List<Invoice<?>> invoices, Payment<?> p) {
		return new AllocationLedger(invoices, Collections.singletonList(p), Collections.<PaymentAllocation>emptyList());
	}

	@Test
	public void testOcr() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		inv.setOcr("100153");
		Invoice<Object> other = invoice("1002", true, 1000, 25);
		Payment<Object> p = payment("P1", true, 1250);
		p.setDestinationSystemReference("00100153");
		List<PaymentMatch> m = matcher.match(p, ledger(Arrays.asList(other, inv), p));
		assertEquals(Confidence.HIGH, m.get(0).getConfidence());
		assertSame(inv, m.get(0).getInvoice());
		assertTrue(m.get(0).isExactAmount());
	}

	@Test
	public void testInvoiceNumberByRegex() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		Payment<Object> p = payment("P1", true, 500);	// partial
		p.setDestinationSystemReference("Faktura 1001 tack");
		p.setDestinationSystemReferenceRegex("(\\d{4})");
		List<PaymentMatch> m = matcher.match(p, ledger(Collections.singletonList(inv), p));
		assertEquals(1, m.size());
		assertEquals(Confidence.HIGH, m.get(0).getConfidence());
		assertFalse(m.get(0).isExactAmount());
	}

	@Test
	public void testPartsOfAReference() {
		Invoice<Object> inv = invoice("107193", true, 4000, 25);	// 5000
		Invoice<Object> other = invoice("107208", true, 2720, 25);	// 3400
		Payment<Object> p = payment("P1", true, 5000);
		p.setDestinationSystemReference("107193 5000.00");
		List<PaymentMatch> m = matcher.match(p, ledger(Arrays.asList(other, inv), p));
		assertSame(inv, m.get(0).getInvoice());
		assertEquals(Confidence.HIGH, m.get(0).getConfidence());

		Payment<Object> q = payment("P2", true, 3400);
		q.setDestinationSystemReference("107208,");
		assertSame(other, matcher.match(q, ledger(Arrays.asList(other, inv), q)).get(0).getInvoice());
	}

	@Test
	public void testPartnerAndAmount() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		Invoice<Object> otherPartner = invoice("1002", true, 1000, 25);
		otherPartner.setBusinessPartner(partner("2002", "Annan AB"));
		Payment<Object> p = payment("P1", true, 1250);
		p.setBusinessPartnerKey("1001");
		List<PaymentMatch> m = matcher.match(p, ledger(Arrays.asList(otherPartner, inv), p));
		assertEquals(1, m.size());
		assertEquals(Confidence.MEDIUM, m.get(0).getConfidence());
		assertSame(inv, m.get(0).getInvoice());
	}

	@Test
	public void testAmountOnlyWhenUnique() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		Invoice<Object> other = invoice("1002", true, 2000, 25);
		Payment<Object> p = payment("P1", true, 1250);
		List<PaymentMatch> m = matcher.match(p, ledger(Arrays.asList(inv, other), p));
		assertEquals(1, m.size());
		assertEquals(Confidence.LOW, m.get(0).getConfidence());

		Invoice<Object> same = invoice("1003", true, 1000, 25);
		assertTrue(matcher.match(p, ledger(Arrays.asList(inv, same), p)).isEmpty());
	}

	@Test
	public void testOnlyInvoicesOfTheRightKindAndCurrency() {
		Invoice<Object> vendorInv = invoice("1001", false, 1000, 25);
		Invoice<Object> eurInv = invoice("1002", true, 1000, 25);
		eurInv.setCurrency("EUR");
		Payment<Object> p = payment("P1", true, 1250);
		p.setInvoiceNo("1001");
		assertTrue(matcher.match(p, ledger(Arrays.asList(vendorInv, eurInv), p)).isEmpty());
	}

	@Test
	public void testPaidInvoicesAreNotCandidates() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		Payment<Object> earlier = payment("P0", true, 1250);
		Payment<Object> p = payment("P1", true, 1250);
		p.setInvoiceNo("1001");
		PaymentAllocation a = completed(PaymentAllocationLine.paymentToInvoice("P0", "1001", true, bd("1250")));
		AllocationLedger l = new AllocationLedger(Collections.singletonList(inv), Arrays.asList(earlier, p),
				Collections.singletonList(a));
		assertTrue(matcher.match(p, l).isEmpty());
	}

}
