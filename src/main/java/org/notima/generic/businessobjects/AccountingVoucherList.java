package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
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
