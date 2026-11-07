package org.notima.generic.businessobjects.settlement;

import static org.junit.jupiter.api.Assertions.*;
import static org.notima.generic.businessobjects.settlement.SettlementFixtures.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.Payment;
import org.notima.generic.businessobjects.settlement.AllocationLedger.SettlementState;

public class TestAllocationLedger {

	private static AllocationLedger ledger(List<Invoice<?>> invoices, List<Payment<?>> payments, PaymentAllocation... allocations) {
		return new AllocationLedger(invoices, payments, Arrays.asList(allocations));
	}

	@Test
	public void testPartialAndFullPayment() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);	// 1250
		Payment<Object> p1 = payment("P1", true, 500);
		Payment<Object> p2 = payment("P2", true, 750);
		List<Invoice<?>> invoices = Collections.singletonList(inv);
		List<Payment<?>> payments = Arrays.asList(p1, p2);

		AllocationLedger l = ledger(invoices, payments);
		assertEquals(0, bd("1250").compareTo(l.getOpenAmount(inv)));
		assertEquals(SettlementState.OPEN, l.getState(inv));
		assertEquals(2, l.getUnallocatedPayments().size());

		PaymentAllocation a1 = completed(PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("500")));
		l = ledger(invoices, payments, a1);
		assertEquals(0, bd("750").compareTo(l.getOpenAmount(inv)));
		assertEquals(SettlementState.PARTIAL, l.getState(inv));
		assertEquals(0, l.getUnallocatedAmount(p1).signum());

		PaymentAllocation a2 = completed(PaymentAllocationLine.paymentToInvoice("P2", "1001", true, bd("750")));
		l = ledger(invoices, payments, a1, a2);
		assertEquals(0, l.getOpenAmount(inv).signum());
		assertEquals(SettlementState.PAID, l.getState(inv));
		assertTrue(l.getOpenInvoices(true).isEmpty());
		assertTrue(l.getUnallocatedPayments().isEmpty());
		assertEquals(2, l.getAllocationsFor(inv).size());
	}

	@Test
	public void testDraftsDontCount() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		Payment<Object> p = payment("P1", true, 1250);
		PaymentAllocation draft = completed(PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("1250")));
		draft.setStatus(DocumentStatus.DRAFT);
		AllocationLedger l = ledger(Collections.singletonList(inv), Collections.singletonList(p), draft);
		assertEquals(0, bd("1250").compareTo(l.getOpenAmount(inv)));
		assertTrue(l.validate(draft).isEmpty(), () -> l.validate(draft).toString());
	}

	@Test
	public void testReversalNetsAndAllowsReallocation() {
		Invoice<Object> inv1 = invoice("1001", true, 1000, 25);
		Invoice<Object> inv2 = invoice("1002", true, 1000, 25);
		Payment<Object> p = payment("P1", true, 1250);
		List<Invoice<?>> invoices = Arrays.asList(inv1, inv2);
		List<Payment<?>> payments = Collections.singletonList(p);

		// Allocated to the wrong invoice, then reversed and allocated to the right one
		PaymentAllocation wrong = completed(PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("1250")));
		PaymentAllocation reversal = wrong.createReversal(DATE.plusDays(20));
		assertEquals(DocumentStatus.REVERSED, wrong.getStatus());
		assertEquals(DocumentStatus.REVERSED, reversal.getStatus());
		assertEquals(wrong.getAllocationId(), reversal.getReversalOfId());

		AllocationLedger l = ledger(invoices, payments, wrong, reversal);
		assertEquals(0, bd("1250").compareTo(l.getOpenAmount(inv1)));
		assertEquals(0, bd("1250").compareTo(l.getUnallocatedAmount(p)));

		PaymentAllocation right = completed(PaymentAllocationLine.paymentToInvoice("P1", "1002", true, bd("1250")));
		assertTrue(l.validate(right).isEmpty(), () -> l.validate(right).toString());
		AllocationLedger after = ledger(invoices, payments, wrong, reversal, right);
		assertEquals(SettlementState.OPEN, after.getState(inv1));
		assertEquals(SettlementState.PAID, after.getState(inv2));
	}

	@Test
	public void testCreditNoteAgainstInvoiceAndWriteOff() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);		// 1250
		Invoice<Object> credit = invoice("1002", true, -200, 25);	// -250
		List<Invoice<?>> invoices = Arrays.asList(inv, credit);

		PaymentAllocation offset = completed(
				PaymentAllocationLine.paymentToInvoice(null, "1001", true, bd("250")),
				PaymentAllocationLine.paymentToInvoice(null, "1002", true, bd("-250")));
		AllocationLedger l = ledger(invoices, new ArrayList<Payment<?>>());
		assertTrue(l.validate(offset).isEmpty(), () -> l.validate(offset).toString());

		PaymentAllocation writeOff = completed(PaymentAllocationLine.writeOff("1001", true, bd("1000"), null));
		AllocationLedger after = ledger(invoices, new ArrayList<Payment<?>>(), offset, writeOff);
		assertEquals(SettlementState.PAID, after.getState(inv));
		assertEquals(SettlementState.PAID, after.getState(credit));
	}

	@Test
	public void testValidationProblems() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		Invoice<Object> vendorInv = invoice("V1", false, 100, 25);
		Payment<Object> p = payment("P1", true, 2000);
		AllocationLedger l = ledger(Arrays.asList(inv, vendorInv), Collections.singletonList(p));

		// More than is open
		assertEquals(1, l.validate(completed(PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("1300")))).size());
		// More than the payment has
		PaymentAllocation tooMuch = completed(
				PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("1250")),
				PaymentAllocationLine.paymentToAccount("P1", "3990", bd("1000")));
		assertEquals(1, l.validate(tooMuch).size(), () -> l.validate(tooMuch).toString());
		// A customer's payment to a vendor invoice
		assertFalse(l.validate(completed(PaymentAllocationLine.paymentToInvoice("P1", "V1", false, bd("100")))).isEmpty());
		// Unknown documents
		assertFalse(l.validate(completed(PaymentAllocationLine.paymentToInvoice("P9", "9999", true, bd("1")))).isEmpty());
		// Invoice-only lines that don't net to zero
		assertFalse(l.validate(completed(PaymentAllocationLine.paymentToInvoice(null, "1001", true, bd("100")))).isEmpty());
		// Allocating the wrong way
		assertFalse(l.validate(completed(PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("-100")))).isEmpty());
		// Other currency
		PaymentAllocation eur = completed(PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("100")));
		eur.setCurrency("EUR");
		assertEquals(2, l.validate(eur).size());
		// No lines
		assertFalse(l.validate(new PaymentAllocation(DATE, "SEK")).isEmpty());
	}

	@Test
	public void testValidateAllocationAlreadyInLedger() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		Payment<Object> p = payment("P1", true, 1250);
		PaymentAllocation a = completed(PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("1250")));
		AllocationLedger l = ledger(Collections.singletonList(inv), Collections.singletonList(p), a);
		assertTrue(l.validate(a).isEmpty(), () -> l.validate(a).toString());
		// ...and the ledger is unchanged afterwards
		assertEquals(0, l.getOpenAmount(inv).signum());
	}

	@Test
	public void testOpenAmountAsOpening() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		inv.setOpenAmt(250);	// imported: 1000 was paid before
		Payment<Object> p = payment("P1", true, 250);
		PaymentAllocation a = completed(PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("250")));
		AllocationLedger l = new AllocationLedger(Collections.singletonList(inv), Collections.singletonList(p),
				Collections.singletonList(a), AllocationLedger.OPEN_AMOUNT);
		assertEquals(SettlementState.PAID, l.getState(inv));
	}

	@Test
	public void testSuggestAllocation() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		Payment<Object> small = payment("P1", true, 1000);
		Payment<Object> big = payment("P2", true, 2000);
		AllocationLedger l = ledger(Collections.singletonList(inv), Arrays.asList(small, big));

		PaymentAllocation s = l.suggestAllocation(small, inv, DATE);
		assertEquals(0, bd("1000").compareTo(s.getLines().get(0).getAmount()));
		assertEquals(DocumentStatus.DRAFT, s.getStatus());
		PaymentAllocation b = l.suggestAllocation(big, inv, DATE);
		assertEquals(0, bd("1250").compareTo(b.getLines().get(0).getAmount()));

		Payment<Object> refund = payment("P3", true, -100);
		AllocationLedger l2 = ledger(Collections.singletonList(inv), Collections.singletonList(refund));
		assertNull(l2.suggestAllocation(refund, inv, DATE));
	}

	@Test
	public void testCannotReverseDraft() {
		PaymentAllocation draft = new PaymentAllocation(DATE, "SEK");
		assertThrows(IllegalStateException.class, () -> draft.createReversal(null));
	}

	@Test
	public void testUnallocatedAmountUsesWhatThePayerPaid() {
		Payment<Object> p = payment("P1", true, 1250);
		p.setAmount(1230.0);	// 20 in fees deducted before it reached the bank
		AllocationLedger l = ledger(new ArrayList<Invoice<?>>(), Collections.singletonList(p));
		assertEquals(0, new BigDecimal("1250").compareTo(l.getUnallocatedAmount(p)));
	}

}
