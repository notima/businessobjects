package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An accounting period together with its chart of accounts and vouchers.
 * <p>
 * Used as the format-neutral exchange object between accounting file
 * importers / exporters ({@link org.notima.generic.ifacebusinessobjects.AccountingFileImporter},
 * {@link org.notima.generic.ifacebusinessobjects.AccountingFileExporter}) and the
 * applications using them.
 * <p>
 * Opening and ending balances are carried on each {@link AccountElement}
 * in the chart of accounts. Balances and reports are calculated with
 * {@link org.notima.generic.businessobjects.accounting.LedgerCalculator}.
 */
public class AccountingPeriodData extends AccountingPeriod {

	private List<AccountElement>	chartOfAccounts = new ArrayList<AccountElement>();
	private List<AccountingVoucher>	vouchers = new ArrayList<AccountingVoucher>();
	private List<OpenItemList>		openItemLists = new ArrayList<OpenItemList>();
	/** Accounting (base) currency for this period, e.g. "SEK". */
	private String					accountingCurrency;
	/** Voucher series definitions for this period, with their last used numbers. */
	private List<VoucherSeries>		voucherSeries = new ArrayList<VoucherSeries>();
	/** Dimension objects (cost centers, projects, etc.) defined for this period. */
	private List<AccountingObject>	accountingObjects = new ArrayList<AccountingObject>();

	public List<AccountElement> getChartOfAccounts() {
		return chartOfAccounts;
	}

	public void setChartOfAccounts(List<AccountElement> chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts != null ? chartOfAccounts : new ArrayList<AccountElement>();
	}

	public List<AccountingVoucher> getVouchers() {
		return vouchers;
	}

	public void setVouchers(List<AccountingVoucher> vouchers) {
		this.vouchers = vouchers != null ? vouchers : new ArrayList<AccountingVoucher>();
	}

	public List<OpenItemList> getOpenItemLists() {
		return openItemLists;
	}

	public void setOpenItemLists(List<OpenItemList> openItemLists) {
		this.openItemLists = openItemLists != null ? openItemLists : new ArrayList<OpenItemList>();
	}

	public String getAccountingCurrency() {
		return accountingCurrency;
	}

	public void setAccountingCurrency(String accountingCurrency) {
		this.accountingCurrency = accountingCurrency;
	}

	public List<VoucherSeries> getVoucherSeries() {
		return voucherSeries;
	}

	public void setVoucherSeries(List<VoucherSeries> voucherSeries) {
		this.voucherSeries = voucherSeries != null ? voucherSeries : new ArrayList<VoucherSeries>();
	}

	public List<AccountingObject> getAccountingObjects() {
		return accountingObjects;
	}

	public void setAccountingObjects(List<AccountingObject> accountingObjects) {
		this.accountingObjects = accountingObjects != null ? accountingObjects : new ArrayList<AccountingObject>();
	}

	/**
	 * Derives voucher series from the vouchers in this period.
	 *
	 * <p>Each distinct {@code voucherSeries} value found in the voucher list is
	 * represented; the last (highest) voucher number per series is computed and
	 * stored.  Existing explicit entries in {@link #getVoucherSeries()} are replaced.</p>
	 */
	public void rebuildVoucherSeriesFromVouchers() {
		if (vouchers == null || vouchers.isEmpty()) return;
		Map<String, Integer> lastNoMap = new TreeMap<String, Integer>();
		for (AccountingVoucher v : vouchers) {
			String series = v.getVoucherSeries();
			if (series == null || series.isEmpty()) series = "";
			int no = 0;
			try { no = Integer.parseInt(v.getVoucherNo()); } catch (Exception ignored) {}
			lastNoMap.merge(series, no, Math::max);
		}
		List<VoucherSeries> result = new ArrayList<VoucherSeries>();
		for (Map.Entry<String, Integer> e : lastNoMap.entrySet()) {
			result.add(new VoucherSeries(e.getKey(), e.getValue()));
		}
		this.voucherSeries = result;
	}

}
