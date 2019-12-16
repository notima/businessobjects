package org.notima.generic.ifacebusinessobjects;

import java.util.List;
import java.util.Map;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.DunningRun;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.Order;
import org.notima.generic.businessobjects.PaymentTerm;
import org.notima.generic.businessobjects.PriceList;
import org.notima.generic.businessobjects.Product;
import org.notima.generic.businessobjects.ProductCategory;
import org.notima.generic.businessobjects.Tax;

public interface BusinessObjectFactory<C,I,O,P,B> {

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
