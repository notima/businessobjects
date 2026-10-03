package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * An accounting period together with its chart of accounts and vouchers.
 * <p>
 * Used as the format-neutral exchange object between accounting file
 * importers / exporters ({@link org.notima.generic.ifacebusinessobjects.AccountingFileImporter},
 * {@link org.notima.generic.ifacebusinessobjects.AccountingFileExporter}) and the
 * applications using them.
 * <p>
 * Opening and ending balances are carried on each {@link AccountElement}
 * in the chart of accounts.
 */
public class AccountingPeriodData extends AccountingPeriod {

	private List<AccountElement>	chartOfAccounts = new ArrayList<AccountElement>();
	private List<AccountingVoucher>	vouchers = new ArrayList<AccountingVoucher>();

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

}
