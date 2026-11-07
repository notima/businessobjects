package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.notima.generic.businessobjects.AccountingType;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;
import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.InvoiceLine;
import org.notima.generic.businessobjects.tax.TaxContext;
import org.notima.generic.businessobjects.tax.TaxDetermination;
import org.notima.generic.businessobjects.tax.TaxEngine;
import org.notima.generic.businessobjects.tax.TaxLine;
import org.notima.generic.businessobjects.tax.TaxRule;
import org.notima.util.LocalDateUtils;

/**
 * Builds the voucher of a customer or vendor invoice, by the invoice method (the invoice
 * is posted when it's issued or received; its payment later only settles the receivable or
 * payable).
 *
 * <p>A customer invoice debits accounts receivable with its grand total; a vendor invoice
 * credits accounts payable. For every line the {@link TaxEngine} chooses the
 * {@link TaxRule tax rule} (from the line's tax category and goods/services, its product's,
 * and the counterparty), which gives the VAT postings and their accounts, also for reverse
 * charge. The line's own amount goes to its own {@link InvoiceLine#getAccountNo() account}, else
 * the rule's revenue or expense account, else the default revenue or purchase account. A
 * credit note, with negative amounts, gives the opposite. Whatever doesn't balance, ie
 * öresavrundning, goes to {@link AccountingType#ROUNDING}.</p>
 *
 * <p>The line amounts ({@link InvoiceLine#getLineNet()}, {@link InvoiceLine#getTaxAmount()})
 * are used as they are, so they should be calculated ({@link Invoice#calculateGrandTotal()});
 * an invoice without lines is posted from its net and VAT totals. Lines for which no rule
 * could be chosen are listed by {@link #getProblems()}; their VAT is left without account.</p>
 *
 * <p>Amounts are in the invoice's currency, which is set as the voucher's
 * {@link AccountingVoucher#getSourceCurrency() source currency}; converting to the
 * accounting currency is up to the caller.</p>
 */
public class InvoiceVoucherBuilder {

	private final PostingAccounts accounts;
	private final TaxEngine engine;
	private final String companyCountry;
	private final List<String> problems = new ArrayList<String>();

	/**
	 * @param accounts			Accounts per role (receivables, payables, rounding, defaults).
	 * @param engine			Chooses the tax rule of each line.
	 * @param companyCountry	The company's country, to tell domestic trade from foreign.
	 */
	public InvoiceVoucherBuilder(PostingAccounts accounts, TaxEngine engine, String companyCountry) {
		this.accounts = accounts != null ? accounts : new PostingAccounts();
		this.engine = engine;
		this.companyCountry = companyCountry;
	}

	/** @return	What went wrong in the last {@link #build(Invoice)}, in English; empty if nothing. */
	public List<String> getProblems() {
		return Collections.unmodifiableList(problems);
	}

	public AccountingVoucher build(Invoice<?> inv) {
		problems.clear();
		boolean sales = inv.isSalesTransaction();
		// Customer invoices credit revenue, vendor invoices debit cost
		BigDecimal sign = sales ? BigDecimal.ONE.negate() : BigDecimal.ONE;

		AccountingVoucher v = new AccountingVoucher();
		v.setAcctDate(LocalDateUtils.asLocalDate(inv.getInvoiceDate()));
		v.setDescription(description(inv));
		v.setSourceCurrency(inv.getCurrency());
		v.setCostCenter(inv.getCostCenter());
		v.setProjectCode(inv.getProject());

		BigDecimal grandTotal = BigDecimal.valueOf(inv.getGrandTotal());
		if (sales) {
			VoucherLines.add(v, grandTotal, AccountingType.LIQUID_ASSET_AR, accounts.get(AccountingType.LIQUID_ASSET_AR));
		} else {
			VoucherLines.add(v, grandTotal.negate(), AccountingType.LIABILITY_VENDOR, accounts.get(AccountingType.LIABILITY_VENDOR));
		}

		List<InvoiceLine> lines = inv.getLines();
		if (lines != null && !lines.isEmpty()) {
			int lineNo = 0;
			for (InvoiceLine il : lines) {
				lineNo++;
				TaxContext ctx = TaxContext.of(inv, il, companyCountry);
				addLine(v, ctx, "Line " + lineNo, BigDecimal.valueOf(il.getLineNet()), BigDecimal.valueOf(il.getTaxAmount()),
						il.getAccountNo(), firstNonBlank(il.getCostCenter(), inv.getCostCenter()),
						firstNonBlank(il.getProject(), inv.getProject()), sign, sales);
			}
		} else {
			BigDecimal net = BigDecimal.valueOf(inv.getNetTotal());
			BigDecimal vat = BigDecimal.valueOf(inv.getVatTotal());
			Double rate = net.signum() != 0
					? vat.multiply(BigDecimal.valueOf(100)).divide(net, 0, RoundingMode.HALF_UP).doubleValue() : 0.0;
			TaxContext ctx = TaxContext.of(inv, null, companyCountry).rate(rate);
			addLine(v, ctx, "The invoice", net, vat, null, inv.getCostCenter(), inv.getProject(), sign, sales);
		}

		VoucherLines.merge(v);
		AccountingVoucherLine rounding = v.balanceWithLine(AccountingType.ROUNDING);
		if (rounding != null) rounding.setAcctNo(accounts.get(AccountingType.ROUNDING));
		return v;
	}

	/** Adds the amount of one line and its VAT, as the tax rule chosen for {@code ctx} says. */
	private void addLine(AccountingVoucher v, TaxContext ctx, String what, BigDecimal net, BigDecimal vat,
			String lineAcct, String costCenter, String project, BigDecimal sign, boolean sales) {
		TaxRule rule = null;
		if (engine == null) {
			problems.add(what + ": there's no tax engine to choose its tax rule");
		} else {
			TaxDetermination det = engine.determine(ctx);
			if (det.isDetermined()) rule = det.getRule();
			else problems.add(what + ": " + det.getProblem());
		}

		String role = sales ? AccountingType.REVENUE : AccountingType.COGS;
		String acct = VoucherLines.blankToNull(lineAcct);
		if (acct == null && rule != null) acct = VoucherLines.blankToNull(sales ? rule.getRevenueAccount() : rule.getExpenseAccount());
		if (acct == null) acct = accounts.get(role);
		String taxKey = rule != null ? rule.getKey() : ctx.getTaxKey();
		dimensions(VoucherLines.add(v, net.multiply(sign), role, acct), taxKey, costCenter, project);

		if (rule != null) {
			for (TaxLine tl : engine.taxLines(rule, net, rule.isReverseCharge() ? null : vat, sales)) {
				dimensions(VoucherLines.add(v, tl.getAmount(), tl.getRole(), tl.getAcctNo()), rule.getKey(), null, null);
			}
		} else {
			// No rule: the VAT is posted without account, so the voucher can't be saved as it is
			VoucherLines.add(v, vat.multiply(sign), sales ? AccountingType.LIABILITY_VAT : AccountingType.CLAIM_VAT, null);
		}
	}

	private static void dimensions(AccountingVoucherLine l, String taxKey, String costCenter, String project) {
		if (l == null) return;
		l.setTaxKey(taxKey);
		l.setCostCenter(costCenter);
		l.setProjectCode(project);
	}

	/** "Invoice 1001 Acme AB", "Vendor invoice 55 Supplier AB", "Credit note ...". */
	static String description(Invoice<?> inv) {
		String kind;
		if (inv.getGrandTotal() < 0) {
			kind = inv.isSalesTransaction() ? "Credit note" : "Vendor credit note";
		} else {
			kind = inv.isSalesTransaction() ? "Invoice" : "Vendor invoice";
		}
		BusinessPartner<?> bp = inv.getBusinessPartner();
		return VoucherLines.join(kind, inv.getInvoiceKey(), bp != null ? bp.getName() : null);
	}

	private static String firstNonBlank(String a, String b) {
		String s = VoucherLines.blankToNull(a);
		return s != null ? s : VoucherLines.blankToNull(b);
	}

}
