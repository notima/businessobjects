package org.notima.generic.businessobjects.tax;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;
import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.InvoiceLine;
import org.notima.generic.businessobjects.Product;
import org.notima.generic.businessobjects.accounting.rule.se.SeCountryRuleSet;
import org.notima.generic.businessobjects.accounting.rule.se.SeTaxRules;
import org.notima.generic.businessobjects.settlement.InvoiceVoucherBuilder;
import org.notima.util.LocalDateUtils;

public class TestRuleBasedTaxEngine {

	private static final LocalDate DATE = LocalDate.of(2026, 10, 1);
	private final TaxEngine engine = new RuleBasedTaxEngine(new SeCountryRuleSet().getDefaultTaxRules());

	private static TaxContext sale() {
		return new TaxContext().date(DATE).sales(true).companyCountry("SE");
	}

	private static TaxContext purchase() {
		return new TaxContext().date(DATE).sales(false).companyCountry("SE");
	}

	private String key(TaxContext ctx) {
		TaxDetermination d = engine.determine(ctx);
		assertTrue(d.isDetermined(), d.getProblem());
		return d.getRule().getKey();
	}

	@Test
	public void testDomesticByCategory() {
		assertEquals("MP1", key(sale().category(TaxCategory.STANDARD)));
		assertEquals("MP2", key(sale().category(TaxCategory.REDUCED_1).counterpartyCountry("SE")));
		assertEquals("MP3", key(sale().category(TaxCategory.REDUCED_2)));
		assertEquals("MF", key(sale().category(TaxCategory.EXEMPT)));
		TaxDetermination d = engine.determine(purchase().category(TaxCategory.STANDARD));
		assertEquals("2641", d.getRule().getInputVatAccount());
		assertEquals(TaxScope.PURCHASE, d.getRule().getScope());
	}

	@Test
	public void testWithoutCategoryTheChargedRateDecides() {
		assertEquals("MP2", key(sale().rate(12.0)));
		assertEquals("MP1", key(purchase().rate(25.0)));
	}

	@Test
	public void testEuSales() {
		TaxContext b2bGoods = sale().category(TaxCategory.STANDARD).counterpartyCountry("DE").vatRegistered(true).service(false);
		assertEquals("VTEU", key(b2bGoods));
		assertEquals("FTEU", key(sale().category(TaxCategory.STANDARD).counterpartyCountry("DE").vatRegistered(true).service(true)));
		// A consumer in another EU country pays Swedish VAT
		assertEquals("MP1", key(sale().category(TaxCategory.STANDARD).counterpartyCountry("DE").vatRegistered(false).service(false)));
		// Goods or services unknown: can't choose
		TaxDetermination d = engine.determine(sale().category(TaxCategory.STANDARD).counterpartyCountry("FI").vatRegistered(true));
		assertFalse(d.isDetermined());
		assertEquals(2, d.getCandidates().size());
		assertTrue(d.getProblem().contains("goods or services"));
	}

	@Test
	public void testExportAndDomesticReverseCharge() {
		assertEquals("E", key(sale().category(TaxCategory.STANDARD).counterpartyCountry("NO").service(false)));
		assertEquals("ES", key(sale().category(TaxCategory.STANDARD).counterpartyCountry("US").service(true)));
		assertEquals("REVD", key(sale().category(TaxCategory.STANDARD).vatType(SeTaxRules.VAT_TYPE_SE_REVERSED)));
		assertEquals("IREVD", key(purchase().category(TaxCategory.STANDARD).vatType(SeTaxRules.VAT_TYPE_SE_REVERSED)));
	}

	@Test
	public void testEuPurchasesAndImport() {
		assertEquals("IEUT25", key(purchase().category(TaxCategory.STANDARD).counterpartyCountry("IE").service(true)));
		assertEquals("IEUV12", key(purchase().category(TaxCategory.REDUCED_1).counterpartyCountry("DE").service(false)));
		assertEquals("IIMT25", key(purchase().category(TaxCategory.STANDARD).counterpartyCountry("US").service(true)));
	}

	@Test
	public void testExplicitKeyAndValidity() {
		// An explicit key applies regardless of the other conditions...
		assertEquals("MP2", key(sale().category(TaxCategory.STANDARD).taxKey("MP2")));
		// ...but only within its scope: there's no sales rule IEUT25
		assertEquals("MP1", key(sale().category(TaxCategory.STANDARD).taxKey("IEUT25")));

		List<TaxRule> rules = new ArrayList<TaxRule>();
		TaxRule old = new TaxRule("OLD", "Old rate", TaxScope.SALES, TaxCategory.STANDARD, 20);
		old.setValidTo(LocalDate.of(2025, 12, 31));
		TaxRule current = new TaxRule("NEW", "New rate", TaxScope.SALES, TaxCategory.STANDARD, 25);
		current.setValidFrom(LocalDate.of(2026, 1, 1));
		rules.add(old);
		rules.add(current);
		TaxEngine e = new RuleBasedTaxEngine(rules);
		assertEquals("NEW", e.determine(sale().category(TaxCategory.STANDARD)).getRule().getKey());
		assertEquals("OLD", e.determine(sale().date(LocalDate.of(2025, 6, 1)).category(TaxCategory.STANDARD)).getRule().getKey());
	}

	@Test
	public void testMoreSpecificRuleWins() {
		List<TaxRule> rules = new SeCountryRuleSet().getDefaultTaxRules();
		TaxRule norway = new TaxRule("NO-SPECIAL", "Special for Norway", TaxScope.SALES, null, 0)
				.revenue("3106");
		norway.setCounterpartyCountry("NO");
		rules.add(norway);
		TaxEngine e = new RuleBasedTaxEngine(rules);
		assertEquals("NO-SPECIAL", e.determine(sale().category(TaxCategory.STANDARD).counterpartyCountry("NO").service(false)).getRule().getKey());
	}

	@Test
	public void testTaxLines() {
		TaxRule mp1 = engine.determine(sale().category(TaxCategory.STANDARD)).getRule();
		List<TaxLine> lines = engine.taxLines(mp1, new BigDecimal("1000"), null, true);
		assertEquals(1, lines.size());
		assertEquals("2611", lines.get(0).getAcctNo());
		assertEquals(0, new BigDecimal("-250").compareTo(lines.get(0).getAmount()));
		// The VAT charged is used as it is
		assertEquals(0, new BigDecimal("-249.99").compareTo(engine.taxLines(mp1, new BigDecimal("1000"), new BigDecimal("249.99"), true).get(0).getAmount()));

		TaxRule euServices = engine.determine(purchase().category(TaxCategory.STANDARD).counterpartyCountry("IE").service(true)).getRule();
		List<TaxLine> rc = engine.taxLines(euServices, new BigDecimal("1000"), null, false);
		assertEquals(2, rc.size());
		assertEquals("2614", rc.get(0).getAcctNo());
		assertEquals(0, new BigDecimal("-250").compareTo(rc.get(0).getAmount()));
		assertEquals("2645", rc.get(1).getAcctNo());
		assertEquals(0, new BigDecimal("250").compareTo(rc.get(1).getAmount()));

		TaxRule euGoodsSale = engine.determine(sale().category(TaxCategory.STANDARD).counterpartyCountry("DE").vatRegistered(true).service(false)).getRule();
		assertTrue(engine.taxLines(euGoodsSale, new BigDecimal("1000"), BigDecimal.ZERO, true).isEmpty());
	}

	// ------------------------------------------------------------------
	// Invoices posted through the engine
	// ------------------------------------------------------------------

	private static Invoice<Object> invoice(boolean sales, String country, String vatNo, InvoiceLine... lines) {
		Invoice<Object> inv = new Invoice<Object>();
		inv.setLines(new ArrayList<InvoiceLine>());
		inv.setInvoiceKey("1001");
		inv.setSalesTransaction(sales);
		inv.setInvoiceDate(LocalDateUtils.asDate(DATE));
		BusinessPartner<Object> bp = new BusinessPartner<Object>();
		bp.setName("Partner");
		bp.setCountryCode(country);
		bp.setVatNo(vatNo);
		inv.setBusinessPartner(bp);
		for (InvoiceLine l : lines) inv.addInvoiceLine(l);
		inv.calculateGrandTotal();
		return inv;
	}

	private static InvoiceLine line(double net, double rate, String category, Boolean service) {
		InvoiceLine il = new InvoiceLine();
		il.setQtyEntered(1);
		il.setPriceActual(net);
		il.setTaxPercent(rate);
		il.setTaxCategory(category);
		il.setService(service);
		return il;
	}

	private static Map<String, String> net(AccountingVoucher v) {
		Map<String, BigDecimal> net = new LinkedHashMap<String, BigDecimal>();
		for (AccountingVoucherLine l : v.getLines()) {
			net.merge(String.valueOf(l.getAcctNo()), l.getDebitAmount().subtract(l.getCreditAmount()), BigDecimal::add);
		}
		Map<String, String> result = new LinkedHashMap<String, String>();
		net.forEach((k, a) -> { if (a.signum() != 0) result.put(k, a.stripTrailingZeros().toPlainString()); });
		return result;
	}

	private static Map<String, String> expect(String... acctAndAmount) {
		Map<String, String> m = new LinkedHashMap<String, String>();
		for (int i = 0; i < acctAndAmount.length; i += 2) m.put(acctAndAmount[i], acctAndAmount[i + 1]);
		return m;
	}

	private InvoiceVoucherBuilder builder() {
		return new InvoiceVoucherBuilder(new SeCountryRuleSet().getDefaultPostingAccounts(), engine, "SE");
	}

	@Test
	public void testEuSaleOfGoodsAndServices() {
		Invoice<Object> inv = invoice(true, "DE", "DE123456789",
				line(1000, 0, TaxCategory.STANDARD, false), line(500, 0, TaxCategory.STANDARD, true));
		InvoiceVoucherBuilder b = builder();
		AccountingVoucher v = b.build(inv);
		assertTrue(b.getProblems().isEmpty(), b.getProblems().toString());
		assertEquals(expect("1510", "1500", "3108", "-1000", "3308", "-500"), net(v));
	}

	@Test
	public void testEuPurchaseOfServicesWithReverseCharge() {
		Invoice<Object> inv = invoice(false, "IE", "IE1234567T", line(1000, 0, TaxCategory.STANDARD, true));
		InvoiceVoucherBuilder b = builder();
		AccountingVoucher v = b.build(inv);
		assertTrue(b.getProblems().isEmpty(), b.getProblems().toString());
		assertEquals(0, v.getBalance().signum());
		assertEquals(expect("2440", "-1000", "4535", "1000", "2614", "-250", "2645", "250"), net(v));
	}

	@Test
	public void testLineOverridesProduct() {
		Product<Object> food = new Product<Object>();
		food.setTaxCategory(TaxCategory.REDUCED_1);
		food.setService(false);

		InvoiceLine fromProduct = line(100, 12, null, null);
		fromProduct.setProduct(food);
		assertEquals(TaxCategory.REDUCED_1, fromProduct.getEffectiveTaxCategory());
		assertEquals(Boolean.FALSE, fromProduct.getEffectiveService());

		InvoiceLine overridden = line(100, 25, TaxCategory.STANDARD, true);
		overridden.setProduct(food);
		assertEquals(TaxCategory.STANDARD, overridden.getEffectiveTaxCategory());
		assertEquals(Boolean.TRUE, overridden.getEffectiveService());

		AccountingVoucher v = builder().build(invoice(true, "SE", null, fromProduct, overridden));
		assertEquals(expect("1510", "237", "3002", "-100", "2621", "-12", "3001", "-100", "2611", "-25"), net(v));
	}

	@Test
	public void testUndeterminedLineIsReported() {
		// EU business customer, but goods or services unknown
		Invoice<Object> inv = invoice(true, "FI", "FI12345678", line(1000, 0, TaxCategory.STANDARD, null));
		InvoiceVoucherBuilder b = builder();
		b.build(inv);
		assertEquals(1, b.getProblems().size());
		assertTrue(b.getProblems().get(0).startsWith("Line 1"));
	}

	@Test
	public void testTradeArea() {
		assertEquals(TradeArea.DOMESTIC, TradeArea.of("SE", null));
		assertEquals(TradeArea.DOMESTIC, TradeArea.of("SE", "se"));
		assertEquals(TradeArea.EU, TradeArea.of("SE", "EL"));
		assertEquals(TradeArea.NON_EU, TradeArea.of("SE", "GB"));
		assertTrue(Arrays.asList(TradeArea.values()).contains(TradeArea.ANY));
	}

}
