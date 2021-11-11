package org.notima.generic.ifacebusinessobjects;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.BusinessPartnerList;
import org.notima.generic.businessobjects.DunningRun;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.Order;
import org.notima.generic.businessobjects.PaymentTerm;
import org.notima.generic.businessobjects.PriceList;
import org.notima.generic.businessobjects.Product;
import org.notima.generic.businessobjects.ProductCategory;
import org.notima.generic.businessobjects.Tax;
import org.notima.generic.businessobjects.TransactionReference;
import org.notima.generic.businessobjects.exception.NoSuchTenantException;

public interface BusinessObjectFactory<C,I,O,P,B> {

	/**
	 * 
	 * @return	The system name of this adapter.
	 */
	public String getSystemName();
	
	/**
	 * List tenants for this business object factory.
	 * 
	 * A tenant is the "owner" of an ERP. The one to which all customer / invoices etc
	 * belongs to.
	 * 
	 * @return A business partner list
	 */
	public BusinessPartnerList<B> listTenants();
	
	/**
	 * Sets current tenant
	 * @param orgNo
	 * @param countryCode
	 */
	public void setTenant(String orgNo, String countryCode) throws NoSuchTenantException;

	/**
	 * Adds a tenant to given business object factory.
	 *  
	 * @param orgNo				The orgNo
	 * @param countryCode		Country Code
	 * @param name				The name of the tenant.
	 * @param props				Properties used for initialization.
	 * @return		A business partner representing the tenant.
	 */
	public BusinessPartner<B> addTenant(String orgNo, String countryCode, String name, Properties props);
	
	/**
	 * Removes a tenant from a given business object factory.
	 * 
	 * @param orgNo				The orgNo
	 * @param countryCode		The country code
	 * @return	True if the tenant was removed. False if the tenant didn't exist.
	 * @throws Exception if something goes wrong.
	 */
	public boolean removeTenant(String orgNo, String countryCode) throws Exception ;
	
	/**
	 * 
	 * @return	The current tenant.
	 */
	public BusinessPartner<B> getCurrentTenant();
	
	/**
	 * Returns given setting.
	 * 
	 * @param settingKey	The key of the setting.
	 * @return				Null if the settings doesn't exist. Otherwise a non-null value.
	 */
	public String getSetting(String settingKey);	
	
	/**
	 * Writes a given setting.
	 * 
	 * @param settingKey	The setting key
	 * @param value			If null and the setting exist, it's remove.
	 */
	public void setSetting(String settingKey, String value);
	
	/**
	 * Appends settings from a settings map
	 * 
	 * @param	settings	An existing settings map. This is copied to the internal settings map.
	 */
	public void appendSettings(Map<String, String> settings);
	
	/**
	 * Lookup business partner (vendor / customer) using supplied key
	 * 
	 * @param key			The business partner key used in the system
	 * @return				A business partner
	 * @throws Exception	If something goes wrong.
	 */
	public BusinessPartner<B> lookupBusinessPartner(String key) throws Exception;
	
	/**
	 * Lookup all business partners
	 * 
	 * @return		A list of all business partners
	 * @throws Exception
	 */
	public List<BusinessPartner<B>> lookupAllBusinessPartners() throws Exception;	

	/**
	 * Looks up business partners
	 * 
	 * @param maxCount		The maximum number of business partners to return.
	 * @param customers		If true, include customers
	 * @param suppliers		If true, include suppliers.
	 * @return				A list of business partners
	 * @throws Exception
	 */
	public List<BusinessPartner<B>> lookupBusinessPartners(int maxCount, boolean customers, boolean suppliers) throws Exception;
	
	/**
	 * Returns information about this legal entity. This means not the customers or 
	 * the vendors, but this legal entity.
	 * 
	 * @return
	 * @throws Exception
	 */
	public BusinessPartner<?> lookupThisCompanyInformation() throws Exception;
	
	/**
	 * Lookup a specified dunning run.
	 * 
	 * @param key			Dunning run identifier
	 * @return				A dunning run.
	 * @throws Exception	If something goes wrong
	 */
	public DunningRun<?,?> lookupDunningRun(String key) throws Exception;

	/**
	 * Returns native client
	 * 
	 * @return
	 */
	public C getClient();
	
	/**
	 * Looks up the native invoice object without doing any conversion.
	 * The object must then be casted to relevant object.
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public I lookupNativeInvoice(String key) throws Exception;
	
	/**
	 * Persists native invoice.
	 * 
	 * @param invoice
	 * @return
	 * @throws Exception
	 */
	public I persistNativeInvoice(I invoice) throws Exception;
	
	public O lookupNativeOrder(String key) throws Exception;
	
	public O persistNativeOrder(O order) throws Exception;
	
	public Invoice<I> lookupInvoice(String key) throws Exception;

	/**
	 * Looks up invoices with a given reference
	 * 
	 * @param reference			A transaction reference
	 * @return	A list of invoices matchning the reference.
	 * @see #flushInvoiceCache()
	 */
	public List<Invoice<I>> lookupInvoiceWithReference(TransactionReference reference) throws Exception;

	/**
	 * Some systems can't lookup quickly on all fields. To flush the cache that might be present, call this
	 * method.
	 * 
	 * @throws Exception
	 */
	public void flushInvoiceCache() throws Exception;
	
	public Order<O> lookupOrder(String key) throws Exception;
	
	public Product<P> lookupProduct(String key) throws Exception;
	
	public Product<P> lookupProductByEan(String ean) throws Exception;
	
	public List<Product<P>> lookupProductByName(String name) throws Exception;
	
	public PriceList lookupPriceForProduct(String productKey, String currency, Boolean salesPriceList) throws Exception;
	
	public List<ProductCategory> lookupProductCategory(String key) throws Exception; 

	/**
	 * Looks up an arbitrary map. What maps that are available depends on 
	 * the implementation.
	 * @param listName
	 * @return	A map.
	 * @throws Exception
	 */
	public Map<Object, Object> lookupList(String listName) throws Exception;
	
	public Product<P> lookupRoundingProduct() throws Exception;
	
	public Tax lookupTax(String key) throws Exception;
	
	public PaymentTerm lookupPaymentTerm(String key) throws Exception;
	
	public FactoringReservation lookupFactoringReservation(String key) throws Exception;
	
	public List<FactoringReservation> lookupFactoringReservationForOrder(String orderKey) throws Exception;
	
	public List<FactoringReservation> lookupFactoringReservationForInvoice(String invoiceKey) throws Exception;

	/**
	 * Write accounting vouchers to underlaying system
	 * 
	 * @param vouchers		The vouchers to be written.
	 * @return				The vouchers written.
	 * @throws Exception	If something goes wrong.
	 */
	public List<AccountingVoucher> writeVouchers(List<AccountingVoucher> vouchers) throws Exception;
	
	/**
	 * Attach file to voucher in underlaying system.
	 * 
	 * @param 	voucher 	The voucher to attach to. Normally it should already exist in the target system.
	 * @param 	fileName 	The filename of the file to attach.
	 */
	public String attachFileToVoucher(AccountingVoucher voucher, String fileName) throws Exception;
	
	/**
	 * Persists a business object
	 * 
	 * @param o
	 * @return
	 */
	public Object persist(Object o) throws Exception;
	
	/**
	 * Check is the factory is connected / online / available 
	 * @return
	 */
	public boolean isConnected() throws Exception;
	
	/**
	 * Destroys and cleans up the factory. 
	 */
	public void destroy() throws Exception;
	
}
