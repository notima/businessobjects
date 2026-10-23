package org.notima.generic.businessobjects.accounting;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.AccountingVoucherLine;

/**
 * The transactions of one account over a date range, with a running balance. Calculated by
 * {@link LedgerCalculator#accountLedger} and {@link LedgerCalculator#generalLedger}.
 */
public class AccountLedger {

	/** One voucher line on the account. */
	public static class Entry {
		private final AccountingVoucher		voucher;
		private final AccountingVoucherLine	line;
		private final double				debit;
		private final double				credit;
		private double						balance;
		private boolean						endOfDay;

		Entry(AccountingVoucher voucher, AccountingVoucherLine line) {
			this.voucher = voucher;
			this.line = line;
			this.debit = LedgerCalculator.debitOf(line);
			this.credit = LedgerCalculator.creditOf(line);
		}

		public AccountingVoucher getVoucher() {
			return voucher;
		}
		public AccountingVoucherLine getLine() {
			return line;
		}
		/** @return	The accounting date of the voucher; may be null. */
		public LocalDate getDate() {
			return voucher.getAcctDate();
		}
		/** @return	The voucher series and number, ie "A 12". */
		public String getVoucherRef() {
			String series = voucher.getVoucherSeries();
			String number = voucher.getVoucherNo();
			if (series != null && !series.isEmpty()) return series + " " + (number != null ? number : "");
			return number != null ? number : "";
		}
		public double getDebit() {
			return debit;
		}
		public double getCredit() {
			return credit;
		}
		/** @return	The balance of the account after this line. */
		public double getBalance() {
			return balance;
		}
		/** @return	True if this is the last line of its date. */
		public boolean isEndOfDay() {
			return endOfDay;
		}
	}

	private final String		accountNo;
	private final String		name;
	private final double		openingBalance;
	private final List<Entry>	entries;

	AccountLedger(String accountNo, String name, double openingBalance, List<Entry> entries) {
		this.accountNo = accountNo;
		this.name = name;
		this.openingBalance = openingBalance;
		this.entries = entries;
		double balance = openingBalance;
		for (int i = 0; i < entries.size(); i++) {
			Entry e = entries.get(i);
			balance += e.debit - e.credit;
			e.balance = balance;
			e.endOfDay = i == entries.size() - 1
					|| !java.util.Objects.equals(e.getDate(), entries.get(i + 1).getDate());
		}
	}

	public String getAccountNo() {
		return accountNo;
	}

	public String getName() {
		return name;
	}

	/** @return	The balance at the start of the range. */
	public double getOpeningBalance() {
		return openingBalance;
	}

	/** @return	The balance at the end of the range. */
	public double getClosingBalance() {
		return entries.isEmpty() ? openingBalance : entries.get(entries.size() - 1).balance;
	}

	/** @return	The lines, sorted by date (nulls last), voucher series and voucher number. */
	public List<Entry> getEntries() {
		return Collections.unmodifiableList(new ArrayList<Entry>(entries));
	}

}
