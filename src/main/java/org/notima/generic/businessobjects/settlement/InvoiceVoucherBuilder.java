package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.notima.generic.businessobjects.AccountingType;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;
import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.InvoiceLine;
import org.notima.util.LocalDateUtils;

/**
 * Builds the voucher of a customer or vendor invoice, by the invoice method (the invoice
 * is posted when it's issued or received; its payment later only settles the receivable or
 * payable).
 *
 * <p>A customer invoice debits accounts receivable with its grand total and credits
 * revenue per line and output VAT per VAT rate. A vendor invoice credits accounts payable
 * and debits the cost per line and input VAT per rate. A credit note, with negative
 * amounts, gives the opposite. Whatever doesn't balance, ie öresavrundning, goes to
 * {@link AccountingType#ROUNDING}.</p>
 *
 * <p>A line's own {@link InvoiceLine#getAccountNo() account} is used if it has one, else the
 * revenue or purchase account for its VAT rate. Its cost center and project, or else the
 * invoice's, are set on its line. The line amounts ({@link InvoiceLine#getLineNet()},
 * {@link InvoiceLine#getTaxAmount()}) are used as they are, so they should be calculated
 * ({@link Invoice#calculateGrandTotal()}); an invoice without lines is posted from its net
 * and VAT totals.</p>
 *
 * <p>Amounts are in the invoice's currency, which is set as the voucher's
 * {@link AccountingVoucher#getSourceCurrency() source currency}; converting to the
 * accounting currency is up to the caller. Reverse charge VAT isn't handled yet.</p>
 */
public class InvoiceVoucherBuilder {

	private final PostingAccounts accounts;

	public InvoiceVoucherBuilder(PostingAccounts accounts) {
		this.accounts = accounts != null ? accounts : new PostingAccounts();
	}

	public AccountingVoucher build(Invoice<?> inv) {
		boolean sales = inv.isSalesTransaction();
		// Customer invoices credit revenue and VAT, vendor invoices debit cost and VAT
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

		String lineRole = sales ? AccountingType.REVENUE : AccountingType.COGS;
		String vatRole = sales ? AccountingType.LIABILITY_VAT : AccountingType.CLAIM_VAT;
		List<InvoiceLine> lines = inv.getLines();
		if (lines != null && !lines.isEmpty()) {
			// VAT per rate, keeping the tax key of the rate's first line
			Map<String, BigDecimal> vatByRate = new LinkedHashMap<String, BigDecimal>();
			Map<String, InvoiceLine> firstOfRate = new LinkedHashMap<String, InvoiceLine>();
			for (InvoiceLine il : lines) {
				String acct = VoucherLines.blankToNull(il.getAccountNo());
				if (acct == null) {
					acct = sales ? accounts.getRevenueAccount(il.getTaxPercent()) : accounts.getPurchaseAccount(il.getTaxPercent());
				}
				AccountingVoucherLine vl = VoucherLines.add(v, BigDecimal.valueOf(il.getLineNet()).multiply(sign), lineRole, acct);
				if (vl != null) {
					vl.setTaxKey(il.getTaxKey());
					vl.setCostCenter(firstNonBlank(il.getCostCenter(), inv.getCostCenter()));
					vl.setProjectCode(firstNonBlank(il.getProject(), inv.getProject()));
				}
				String rate = PostingAccounts.rateKey(il.getTaxPercent());
				vatByRate.merge(rate, BigDecimal.valueOf(il.getTaxAmount()), BigDecimal::add);
				firstOfRate.putIfAbsent(rate, il);
			}
			for (Map.Entry<String, BigDecimal> e : vatByRate.entrySet()) {
				InvoiceLine il = firstOfRate.get(e.getKey());
				String acct = sales ? accounts.getOutputVatAccount(il.getTaxPercent()) : accounts.getInputVatAccount(il.getTaxPercent());
				AccountingVoucherLine vl = VoucherLines.add(v, e.getValue().multiply(sign), vatRole, acct);
				if (vl != null) vl.setTaxKey(il.getTaxKey());
			}
		} else {
			VoucherLines.add(v, BigDecimal.valueOf(inv.getNetTotal()).multiply(sign), lineRole, accounts.get(lineRole));
			VoucherLines.add(v, BigDecimal.valueOf(inv.getVatTotal()).multiply(sign), vatRole, accounts.get(vatRole));
		}

		AccountingVoucherLine rounding = v.balanceWithLine(AccountingType.ROUNDING);
		if (rounding != null) rounding.setAcctNo(accounts.get(AccountingType.ROUNDING));
		return v;
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
