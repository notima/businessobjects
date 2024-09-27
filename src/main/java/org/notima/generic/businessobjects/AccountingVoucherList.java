package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AccountingVoucherList")
public class AccountingVoucherList {
	
	private List<AccountingVoucher> accountingVouchers = new ArrayList<AccountingVoucher>();
	
	public List<AccountingVoucher> getAccountingVouchers() {
		return accountingVouchers;
	}

	public void setAccountingVouchers(List<AccountingVoucher> accountingVouchers) {
		this.accountingVouchers = accountingVouchers;
	}
	
}
