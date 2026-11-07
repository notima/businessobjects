package org.notima.generic.businessobjects.settlement;

import static org.junit.jupiter.api.Assertions.*;
import static org.notima.generic.businessobjects.settlement.SettlementFixtures.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.Payment;
import org.notima.generic.businessobjects.PaymentWriteOff;
import org.notima.generic.businessobjects.accounting.rule.se.SeCountryRuleSet;
import org.notima.generic.businessobjects.tax.RuleBasedTaxEngine;
import org.notima.generic.businessobjects.tax.TaxEngine;

public class TestSettlementVouchers {

	private static final PostingAccounts BAS = new SeCountryRuleSet().getDefaultPostingAccounts();
	private static final TaxEngine SE_TAX = new RuleBasedTaxEngine(new SeCountryRuleSet().getDefaultTaxRules());

	private static InvoiceVoucherBuilder invoiceBuilder() {
		return new InvoiceVoucherBuilder(BAS, SE_TAX, "SE");
	}

	/** Net (debit - credit) per account, as plain strings without trailing zeros. */
	private static Map<String, String> net(AccountingVoucher v) {
		Map<String, BigDecimal> net = new LinkedHashMap<String, BigDecimal>();
		for (AccountingVoucherLine l : v.getLines()) {
			net.merge(String.valueOf(l.getAcctNo()), l.getDebitAmount().subtract(l.getCreditAmount()), BigDecimal::add);
		}
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (Map.Entry<String, BigDecimal> e : net.entrySet()) {
			if (e.getValue().signum() != 0) result.put(e.getKey(), e.getValue().stripTrailingZeros().toPlainString());
		}
		return result;
	}

	private static Map<String, String> expect(String... acctAndAmount) {
		Map<String, String> m = new LinkedHashMap<String, String>();
		for (int i = 0; i < acctAndAmount.length; i += 2) m.put(acctAndAmount[i], acctAndAmount[i + 1]);
		return m;
	}

	private static void assertBalancedAndComplete(AccountingVoucher v) {
		assertEquals(0, v.getBalance().signum(), "balance");
		assertTrue(PostingAccounts.missingAccounts(v).isEmpty(), () -> "missing " + PostingAccounts.missingAccounts(v));
	}

	@Test
	public void testCustomerInvoiceWithTwoRates() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25, 200, 12);	// 1250 + 224
		AccountingVoucher v = invoiceBuilder().build(inv);
		assertBalancedAndComplete(v);
		assertEquals(expect("1510", "1474", "3001", "-1000", "3002", "-200", "2611", "-250", "2621", "-24"), net(v));
		assertEquals(DATE, v.getAcctDate());
		assertEquals("Invoice 1001 Kund AB", v.getDescription());
	}

	@Test
	public void testLineAccountAndRounding() {
		Invoice<Object> inv = invoice("1001", true, 99.6, 25);	// 124.50
		inv.getLines().get(0).setAccountNo("3051");
		inv.setGrandTotal(125);									// rounded to whole kronor
		AccountingVoucher v = invoiceBuilder().build(inv);
		assertBalancedAndComplete(v);
		assertEquals(expect("1510", "125", "3051", "-99.6", "2611", "-24.9", "3740", "-0.5"), net(v));
	}

	@Test
	public void testVendorInvoiceAndCreditNote() {
		Invoice<Object> inv = invoice("V1", false, 800, 25);
		AccountingVoucher v = invoiceBuilder().build(inv);
		assertBalancedAndComplete(v);
		assertEquals(expect("2440", "-1000", "4010", "800", "2641", "200"), net(v));

		Invoice<Object> credit = invoice("1002", true, -200, 25);
		AccountingVoucher c = invoiceBuilder().build(credit);
		assertEquals(expect("1510", "-250", "3001", "200", "2611", "50"), net(c));
		assertTrue(c.getDescription().startsWith("Credit note"));
	}

	@Test
	public void testUnallocatedPaymentWithFees() {
		Payment<Object> p = payment("P1", true, 1250);
		p.setAmount(1230.0);
		p.setGlAccountNo("1580");
		AccountingVoucher v = new PaymentVoucherBuilder(BAS).build(p);
		assertBalancedAndComplete(v);
		assertEquals(expect("1580", "1230", "6570", "20", "2420", "-1250"), net(v));
	}

	@Test
	public void testPaymentWriteOffsGoToTheirAccounts() {
		Payment<Object> p = payment("P1", true, 1250);
		p.setAmount(1230.0);
		p.addPaymentWriteOff(new PaymentWriteOff(15.0, "6064", "fee"));
		AccountingVoucher v = new PaymentVoucherBuilder(BAS).build(p);
		assertBalancedAndComplete(v);
		assertEquals(expect("1930", "1230", "6064", "15", "6570", "5", "2420", "-1250"), net(v));
	}

	@Test
	public void testPaymentAllocatedAtOnceIsOneVoucherAgainstReceivables() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		Payment<Object> p = payment("P1", true, 1250);
		PaymentAllocation a = completed(PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("1250")));
		AccountingVoucher v = new PaymentVoucherBuilder(BAS).build(p, a, null);
		assertBalancedAndComplete(v);
		assertEquals(expect("1930", "1250", "1510", "-1250"), net(v));
		assertEquals(2, v.getLines().size());
	}

	@Test
	public void testPartlyAllocatedPaymentLeavesPrepayment() {
		Payment<Object> p = payment("P1", true, 1500);
		PaymentAllocation a = completed(PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("1250")));
		AccountingVoucher v = new PaymentVoucherBuilder(BAS).build(p, a, null);
		assertBalancedAndComplete(v);
		assertEquals(expect("1930", "1500", "2420", "-250", "1510", "-1250"), net(v));
	}

	@Test
	public void testLaterAllocationWithDiscountAndWriteOff() {
		PaymentAllocationLine l = PaymentAllocationLine.paymentToInvoice("P1", "1001", true, bd("1200"));
		l.setDiscountAmount(bd("30"));
		l.setWriteOffAmount(bd("20"));
		PaymentAllocation a = completed(l);
		AccountingVoucher v = new AllocationVoucherBuilder(BAS).build(a, null);
		assertBalancedAndComplete(v);
		assertEquals(expect("2420", "1200", "1510", "-1250", "3731", "30", "6351", "20"), net(v));

		// The reversal is the reversed voucher
		AccountingVoucher r = new AllocationVoucherBuilder(BAS).build(a.createReversal(null), null);
		assertEquals(expect("2420", "-1200", "1510", "1250", "3731", "-30", "6351", "-20"), net(r));
		assertEquals("Reversed allocation", r.getDescription());
	}

	@Test
	public void testPaymentBookedToChargeAccount() {
		Payment<Object> p = payment("P1", true, 100);
		AllocationLedger ledger = new AllocationLedger(Collections.<Invoice<?>>emptyList(),
				Collections.singletonList(p), Collections.<PaymentAllocation>emptyList());
		PaymentAllocation a = completed(PaymentAllocationLine.paymentToAccount("P1", "3990", bd("100")));
		AccountingVoucher v = new AllocationVoucherBuilder(BAS).build(a, ledger);
		assertEquals(expect("2420", "100", "3990", "-100"), net(v));
	}

	@Test
	public void testCreditNoteOffsetLeavesNoLines() {
		PaymentAllocation a = completed(
				PaymentAllocationLine.paymentToInvoice(null, "1001", true, bd("250")),
				PaymentAllocationLine.paymentToInvoice(null, "1002", true, bd("-250")));
		AccountingVoucher v = new AllocationVoucherBuilder(BAS).build(a, null);
		assertTrue(v.getLines().isEmpty());
	}

	@Test
	public void testVendorPaymentAndAllocation() {
		Payment<Object> p = payment("U1", false, 1000);
		PaymentAllocation a = completed(PaymentAllocationLine.paymentToInvoice("U1", "V1", false, bd("1000")));
		AccountingVoucher v = new PaymentVoucherBuilder(BAS).build(p, a, null);
		assertBalancedAndComplete(v);
		assertEquals(expect("1930", "-1000", "2440", "1000"), net(v));

		AccountingVoucher unallocated = new PaymentVoucherBuilder(BAS).build(p);
		assertEquals(expect("1930", "-1000", "1480", "1000"), net(unallocated));

		AccountingVoucher later = new AllocationVoucherBuilder(BAS).build(a,
				new AllocationLedger(null, Arrays.asList(p), null));
		assertEquals(expect("1480", "-1000", "2440", "1000"), net(later));
	}

	@Test
	public void testMissingAccountsAreReported() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		InvoiceVoucherBuilder b = new InvoiceVoucherBuilder(new PostingAccounts(), SE_TAX, "SE");
		AccountingVoucher v = b.build(inv);
		assertEquals(0, v.getBalance().signum());
		assertTrue(b.getProblems().isEmpty());
		// The revenue and VAT accounts come from the tax rule; only receivables is missing
		assertEquals(Arrays.asList("liquid-asset-ar"), PostingAccounts.missingAccounts(v));
	}

	@Test
	public void testLineWithoutTaxRuleIsReported() {
		Invoice<Object> inv = invoice("1001", true, 1000, 25);
		InvoiceVoucherBuilder b = new InvoiceVoucherBuilder(BAS, new RuleBasedTaxEngine(null), "SE");
		AccountingVoucher v = b.build(inv);
		assertEquals(1, b.getProblems().size());
		assertTrue(PostingAccounts.missingAccounts(v).contains("liability-vat"));
	}

}
