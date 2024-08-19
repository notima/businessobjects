package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.BusinessPartner;

/**
 * Converts vouchers from one format to another
 * 
 */
public interface AccountingVoucherConverter<F> {

	public String getSystemName();
	
	public void readSource(F src);
	
	public void setTenant(BusinessPartner<?> tenant);
	
	public BusinessPartner<?> getTenant();
	
	List<AccountingVoucher> getAccountingVouchers();
	
}
