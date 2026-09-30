package org.notima.generic.businessobjects.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.BankAccountDetail;
import org.notima.generic.businessobjects.Payment;
import org.notima.generic.businessobjects.PaymentBatch;
import org.notima.generic.businessobjects.PaymentBatchChannelThresholds;
import org.notima.generic.businessobjects.ThresholdCheckResult;

public class TestPaymentBatchChannelThresholds {

	private static Payment<Object> payment(double amount, String currency, String matchedInvoice, Double openAmount) {
		Payment<Object> p = new Payment<Object>();
		p.setAmount(amount);
		p.setCurrency(currency);
		p.setMatchedInvoiceNo(matchedInvoice);
		p.setMatchedInvoiceOpenAmount(openAmount);
		return p;
	}

	private static Payment<Object> matched(double amount, String currency) {
		return payment(amount, currency, "1001", amount);
	}

	private static Payment<Object> unmatched(double amount, String currency) {
		return payment(amount, currency, null, null);
	}

	@SafeVarargs
	private static PaymentBatch batch(String currency, Payment<Object>... payments) {
		PaymentBatch pb = new PaymentBatch();
		BankAccountDetail bad = new BankAccountDetail();
		bad.setCurrency(currency);
		pb.setBankAccount(bad);
		for (Payment<Object> p : payments) {
			pb.addPayment(p);
		}
		return pb;
	}

	private static List<PaymentBatch> file(PaymentBatch... batches) {
		return Arrays.asList(batches);
	}

	@Test
	public void noLimitsIsNeverBreached() {
		PaymentBatchChannelThresholds t = new PaymentBatchChannelThresholds();
		assertFalse(t.hasLimits());
		ThresholdCheckResult r = t.evaluate(file(batch("SEK", unmatched(100, "SEK"), unmatched(200, "SEK"))));
		assertFalse(r.isBreached());
		assertEquals(2, r.getUnmatchedCount());
	}

	@Test
	public void countLimit() {
		PaymentBatchChannelThresholds t = new PaymentBatchChannelThresholds();
		t.setMaxUnmatchedCount(3);
		assertTrue(t.hasLimits());
		PaymentBatch three = batch("SEK", unmatched(1, "SEK"), unmatched(1, "SEK"), unmatched(1, "SEK"), matched(1, "SEK"));
		assertFalse(t.evaluate(file(three)).isBreached());
		three.addPayment(unmatched(1, "SEK"));
		ThresholdCheckResult r = t.evaluate(file(three));
		assertTrue(r.isBreached());
		assertEquals("4 unmatched payments, limit 3", r.getBreaches().get(0));
	}

	@Test
	public void percentLimit() {
		PaymentBatchChannelThresholds t = new PaymentBatchChannelThresholds();
		t.setMaxUnmatchedPercent(10d);
		PaymentBatch pb = batch("SEK", unmatched(1, "SEK"), unmatched(1, "SEK"));
		for (int i = 0; i < 8; i++) pb.addPayment(matched(1, "SEK"));
		ThresholdCheckResult r = t.evaluate(file(pb));
		assertEquals(20d, r.getUnmatchedPercent(), 0.001);
		assertTrue(r.isBreached());

		t.setMaxUnmatchedPercent(20d);
		assertFalse(t.evaluate(file(pb)).isBreached());
	}

	@Test
	public void amountLimitPerCurrency() {
		PaymentBatchChannelThresholds t = new PaymentBatchChannelThresholds();
		t.setMaxUnmatchedAmount("5000,{500:EUR}");

		ThresholdCheckResult r = t.evaluate(file(
				batch("EUR", unmatched(600, "EUR")),
				batch("SEK", unmatched(4000, "SEK"))));
		assertTrue(r.isBreached());
		assertEquals(1, r.getBreaches().size());
		assertEquals("Unmatched EUR 600.00, limit 500.00", r.getBreaches().get(0));

		// Plain value applies to currencies not listed
		assertFalse(t.evaluate(file(batch("NOK", unmatched(4999, "NOK")))).isBreached());
		assertTrue(t.evaluate(file(batch("SEK", unmatched(5001, "SEK")))).isBreached());
	}

	@Test
	public void amountLimitWithoutValueForCurrencyIsBreached() {
		PaymentBatchChannelThresholds t = new PaymentBatchChannelThresholds();
		t.setMaxUnmatchedAmount("{500:EUR}");
		ThresholdCheckResult r = t.evaluate(file(batch("NOK", unmatched(1, "NOK"))));
		assertTrue(r.isBreached());
		assertTrue(r.getBreaches().get(0).contains("no limit defined for NOK"), r.getBreaches().get(0));
	}

	@Test
	public void refundsCountByAbsoluteAmount() {
		PaymentBatchChannelThresholds t = new PaymentBatchChannelThresholds();
		t.setMaxUnmatchedAmount("100");
		assertTrue(t.evaluate(file(batch("SEK", unmatched(-150, "SEK")))).isBreached());
	}

	@Test
	public void alreadyPaidInvoiceCountsAsUnmatched() {
		assertTrue(PaymentBatchChannelThresholds.isUnmatched(payment(100, "SEK", "1001", 0d)));
		assertTrue(PaymentBatchChannelThresholds.isUnmatched(payment(100, "SEK", null, null)));
		assertFalse(PaymentBatchChannelThresholds.isUnmatched(payment(100, "SEK", "1001", 100d)));
		// Open amount unknown (invoice couldn't be looked up) counts as unmatched
		assertTrue(PaymentBatchChannelThresholds.isUnmatched(payment(100, "SEK", "1001", null)));
	}

	@Test
	public void mixedCurrencyFileIsOneFileForCountAndPercent() {
		PaymentBatchChannelThresholds t = new PaymentBatchChannelThresholds();
		t.setMaxUnmatchedCount(1);
		ThresholdCheckResult r = t.evaluate(file(
				batch("EUR", unmatched(10, "EUR"), matched(10, "EUR")),
				batch("SEK", unmatched(10, "SEK"), matched(10, "SEK"))));
		assertEquals(4, r.getPaymentCount());
		assertEquals(2, r.getUnmatchedCount());
		assertEquals(50d, r.getUnmatchedPercent(), 0.001);
		assertTrue(r.isBreached());
		assertEquals(2, r.getUnmatchedAmountPerCurrency().size());
	}

	@Test
	public void paymentWithoutCurrencyUsesBatchCurrency() {
		PaymentBatchChannelThresholds t = new PaymentBatchChannelThresholds();
		ThresholdCheckResult r = t.evaluate(file(batch("EUR", unmatched(10, null))));
		assertEquals(10d, r.getUnmatchedAmountPerCurrency().get("EUR"), 0.001);
	}

	@Test
	public void amountDefinitionIsValidated() {
		PaymentBatchChannelThresholds t = new PaymentBatchChannelThresholds();
		assertThrows(IllegalArgumentException.class, () -> t.setMaxUnmatchedAmount("abc"));
		assertThrows(IllegalArgumentException.class, () -> t.setMaxUnmatchedAmount("100,{x:EUR}"));
		t.setMaxUnmatchedAmount("  ");
		assertNull(t.getMaxUnmatchedAmount());
		assertFalse(t.hasLimits());
	}

	@Test
	public void emptyFile() {
		PaymentBatchChannelThresholds t = new PaymentBatchChannelThresholds();
		t.setMaxUnmatchedPercent(0d);
		ThresholdCheckResult r = t.evaluate(Collections.<PaymentBatch>emptyList());
		assertFalse(r.isBreached());
		assertEquals(0, r.getPaymentCount());
	}

}
