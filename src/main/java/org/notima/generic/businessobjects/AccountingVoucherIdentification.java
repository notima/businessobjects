package org.notima.generic.businessobjects;

public class AccountingVoucherIdentification implements Comparable<AccountingVoucherIdentification> {

	private String	voucherSeries;
	private String	voucherNo;

	public AccountingVoucherIdentification(String series, String no) {
		voucherSeries = series;
		voucherNo = no;
	}
	
	public String getVoucherSeries() {
		return voucherSeries;
	}

	public void setVoucherSeries(String voucherSeries) {
		this.voucherSeries = voucherSeries;
	}

	public int getVoucherSeriesAsInt() {
		return Integer.valueOf(voucherSeries);
	}
	
	public int getVoucherNoAsInt() {
		return Integer.valueOf(voucherNo);
	}
	
	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	@Override
	public int compareTo(AccountingVoucherIdentification o) {
		
		if (!(o instanceof AccountingVoucherIdentification)) {
			return -1;
		}

		AccountingVoucherIdentification that = (AccountingVoucherIdentification)o;

		int voucherSeriesInt = compareVoucherSeries(that);
		
		if (voucherSeriesInt==0) {
			return compareVoucherNo(that);
		} 
		
		return voucherSeriesInt;
	}
	
	private int compareVoucherSeries(AccountingVoucherIdentification that) {
		return compareStringsOrNumbers(voucherSeries, that.getVoucherSeries());
	}
	
	private int compareVoucherNo(AccountingVoucherIdentification that) {
		return compareStringsOrNumbers(voucherNo, that.getVoucherNo());
	}

	private int compareStringsOrNumbers(String one, String two) {
		if (isInteger(one) && isInteger(two)) {
			int oneInt = Integer.parseInt(one);
			int twoInt = Integer.parseInt(two);
			if (oneInt > twoInt) return 1;
			if (oneInt == twoInt) return 0;
			return -1;
		} else {
			if (one==null) return -1;
			return one.compareTo(two);
		}
	}
	
	
	private boolean isInteger(String n) {
		try {
			Integer.parseInt(n);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
}
