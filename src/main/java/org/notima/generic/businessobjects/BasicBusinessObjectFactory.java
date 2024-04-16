package org.notima.generic.businessobjects;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.bind.JAXB;

import org.notima.generic.businessobjects.exception.NoSuchTenantException;
import org.notima.generic.ifacebusinessobjects.BusinessObjectFactory;

/*
 *    Copyright 2020 Notima System Integration AB

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */


/**
 * Boiler plate of a business object factory. This abstract class contains a settings mechanism.
 * 
 * @author Daniel Tamm
 * @param <O>
 *
 */
public abstract class BasicBusinessObjectFactory<C,I,O,P,B,T> implements BusinessObjectFactory<C,I,O,P,B,T> {

	protected Map<String,String> settingsMap;
	
	protected Map<String, BusinessPartner<T>> tenantMap = new TreeMap<String,BusinessPartner<T>>();
	
	protected BusinessPartner<T> currentTenant = null;
	
	protected boolean enrichDataByDefault = false;
	
	@Override
	public List<Invoice<I>> lookupInvoiceWithReference(TransactionReference reference) throws Exception {

		List<Invoice<I>> invoices = new ArrayList<Invoice<I>>();
		if (reference==null) return invoices;
		Invoice<I> invoice = lookupInvoice(reference.getInvoiceNo());
		if (invoice!=null)
			invoices.add(invoice);
		return invoices;
		
	}
	
	
	/**
	 * Some systems can't lookup quickly on all fields. To flush the cache that might be present, call this
	 * method.
	 * 
	 * @throws Exception
	 */
	@Override
	public void flushInvoiceCache() throws Exception {
		
	}
	
	public boolean isEnrichDataByDefault() {
		return enrichDataByDefault;
	}

	public void setEnrichDataByDefault(boolean enrichDataByDefault) {
		this.enrichDataByDefault = enrichDataByDefault;
	}


	/**
	 * Adds a tenant to given business object factory.
	 * Override this method to add real tenants to a business object factory.
	 *  
	 * @param orgNo				The orgNo
	 * @param countryCode		Country Code
	 * @param name				Tenant name
	 * @param props				Properties used for initialization.
	 * @return
	 */
	public BusinessPartner<T> addTenant(String orgNo, String countryCode, String name, Properties props) {
	
		BusinessPartner<T> tenant = tenantMap.get(orgNo);
		if (tenant==null) {
			tenant = new BusinessPartner<T>();
			tenantMap.put(orgNo, tenant);
		}
		tenant.setTaxId(orgNo);
		tenant.setCountryCode(countryCode);
		tenant.setName(name);
		
		if (currentTenant==null) {
			currentTenant = tenant;
		}
		
		return tenant;
		
	}
	
	/**
	 * Removes a tenant from a given business object factory.
	 * 
	 * @param orgNo				The orgNo
	 * @param countryCode		The country code
	 * @return	True if the tenant was removed. False if the tenant didn't exist.
	 * @throws Exception if something goes wrong.
	 */
	public boolean removeTenant(String orgNo, String countryCode) throws Exception {

		BusinessPartner<T> tenant = tenantMap.get(orgNo);
		if (tenant==null) return false;
		
		tenantMap.remove(orgNo);
		return true;
	}
	
	
	@Override
	public void setTenant(String orgNo, String countryCode) throws NoSuchTenantException {

		BusinessPartner<T> tenant = tenantMap.get(orgNo);
		if (tenant==null) {
			throw new NoSuchTenantException(orgNo);
		} else if (countryCode!=null && tenant.getCountryCode()!=null && !countryCode.equals(tenant.getCountryCode())){
			throw new NoSuchTenantException("Tenant has different countrycode: " + tenant.getCountryCode() + " : " + orgNo);
		}
		currentTenant = tenant;
		
	}

	@Override
	public BusinessPartner<T> getCurrentTenant() {
		return currentTenant;
	}

	public void refreshTenantMap() {
		tenantMap.clear();
		BusinessPartnerList<T> tenants = this.listTenants();
		for (BusinessPartner<T> bp : tenants.getBusinessPartner()) {
			tenantMap.put(bp.getTaxId(), bp);
		}
		
	}
	

	/**
	 * Returns given setting.
	 * 
	 * @param settingKey	The key of the setting.
	 * @return				Null if the settings doesn't exist. Otherwise a non-null value.
	 */
	public String getSetting(String settingKey) {
		
		if (settingsMap==null)
			return null;
		
		String result = settingsMap.get(settingKey);
		
		return result;
		
	}

	/**
	 * Writes a given setting.
	 * 
	 * @param settingKey	The setting key
	 * @param value			If null and the setting exist, it's remove.
	 */
	public void setSetting(String settingKey, String value) {
		
		if (settingsMap==null) {
			settingsMap = new TreeMap<String, String>();
		}
		
		if (value!=null) {
			settingsMap.put(settingKey, value);
		} else {
			if (settingsMap.get(settingKey)!=null) {
				settingsMap.remove(settingKey);
			}
		}
		
	}

	/**
	 * Appends settings from a settings map
	 * 
	 * @param	settings	An existing settings map. This is copied to the internal settings map.
	 */
	public void appendSettings(Map<String, String> settings) {
		
		if (settings==null) return;
		
		if (settingsMap==null) {
			settingsMap = new TreeMap<String, String>();
		}
		
		for (String s : settings.keySet()) {
			settingsMap.put(s, settings.get(s));
		}
		
	}

	@Override
	public List<BusinessPartner<B>> lookupAllActiveCustomers() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<AccountingVoucher> writeVouchers(List<AccountingVoucher> vouchers) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public InvoiceOperationResult writeInvoices(List<Invoice<?>> canonicalInvoices, Date invoiceDate, Date dueDate,
			boolean createBp, int createLimit, boolean updateExisting) throws Exception {

		InvoiceList listToWrite = new InvoiceList();
		listToWrite.setInvoiceList(canonicalInvoices);
		
		String fileName = System.getProperty("user.home") + File.separator + "invoicelist.xml";
		
		JAXB.marshal(listToWrite, new File(fileName));
		InvoiceOperationResult result = new InvoiceOperationResult();
		result.setAffectedInvoices(listToWrite);
		
		return result;
	}
	
	@Override
	public InvoiceOperationResult readInvoices(Date fromDate, Date untilDate, int readLimit) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String attachFileToVoucher(AccountingVoucher voucher, String fileName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
