package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;

/**
 * Helpers for the voucher builders of this package.
 */
final class VoucherLines {

	private VoucherLines() {}

	/**
	 * Adds a line to {@code voucher}, unless {@code amount} is zero.
	 *
	 * @param amount	Debit if positive, credit if negative.
	 * @param role		One of the {@link org.notima.generic.businessobjects.AccountingType} constants.
	 */
	static AccountingVoucherLine add(AccountingVoucher voucher, BigDecimal amount, String role, String acctNo) {
		if (amount == null || amount.signum() == 0) return null;
		AccountingVoucherLine l = new AccountingVoucherLine(amount, role);
		l.setAcctNo(blankToNull(acctNo));
		voucher.addVoucherLine(l);
		return l;
	}

	/**
	 * Replaces the lines of {@code voucher} that have the same account, role, tax key, cost
	 * center and project by one line with their net amount, and drops lines that net to zero.
	 * The first line of each group decides its place.
	 */
	static void merge(AccountingVoucher voucher) {
		if (voucher.getLines() == null) return;
		Map<List<Object>, BigDecimal> net = new LinkedHashMap<List<Object>, BigDecimal>();
		Map<List<Object>, AccountingVoucherLine> first = new LinkedHashMap<List<Object>, AccountingVoucherLine>();
		for (AccountingVoucherLine l : voucher.getLines()) {
			List<Object> key = new ArrayList<Object>();
			key.add(l.getAcctNo());
			key.add(l.getAcctType());
			key.add(l.getTaxKey());
			key.add(l.getCostCenter());
			key.add(l.getProjectCode());
			net.merge(key, nvl(l.getDebitAmount()).subtract(nvl(l.getCreditAmount())), BigDecimal::add);
			first.putIfAbsent(key, l);
		}
		List<AccountingVoucherLine> lines = new ArrayList<AccountingVoucherLine>();
		for (Map.Entry<List<Object>, BigDecimal> e : net.entrySet()) {
			if (e.getValue().signum() == 0) continue;
			AccountingVoucherLine src = first.get(e.getKey());
			AccountingVoucherLine l = new AccountingVoucherLine(e.getValue(), src.getAcctType());
			l.setAcctNo(src.getAcctNo());
			l.setTaxKey(src.getTaxKey());
			l.setCostCenter(src.getCostCenter());
			l.setProjectCode(src.getProjectCode());
			lines.add(l);
		}
		voucher.setLines(new ArrayList<AccountingVoucherLine>());
		for (AccountingVoucherLine l : lines) voucher.addVoucherLine(l);
	}

	static BigDecimal nvl(BigDecimal v) {
		return v != null ? v : BigDecimal.ZERO;
	}

	static String blankToNull(String s) {
		return s == null || s.trim().isEmpty() ? null : s.trim();
	}

	static String join(String... parts) {
		StringBuilder sb = new StringBuilder();
		for (String p : parts) {
			if (p == null || p.trim().isEmpty()) continue;
			if (sb.length() > 0) sb.append(' ');
			sb.append(p.trim());
		}
		return sb.toString();
	}

}
