package org.notima.generic.ifacebusinessobjects;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.notima.generic.businessobjects.Tax;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;
import org.notima.generic.businessobjects.exception.NoSuchTenantException;
import org.notima.generic.businessobjects.exception.TaxRatesNotAvailableException;

/**
 * Interface to get valid tax rates.
 * 
 * The tax rates are all usable tax rates for a system / tenant. It's up to the caller to
 * then decide what tax rate to use for the given situation.
 * 
 * @author Daniel Tamm
 *
 */
public interface TaxRateProvider {

	/**
	 * 	 * 
	 * @return 	The system name of this tax rate provider.
	 */
	public String getSystemName();
	
	/**
	 * The tax domiciles of this tax rate provider.
	 * 
	 * @return
	 */
	public Set<String> getTaxDomiciles();
	
	/**
	 * 
	 * @param tsi			Tax subject identifier. This is the tenant / client of the system. Tax domicile of tsi is assumed.
	 * @param taxDate		The date for which the taxes should be valid.
	 * @return				A list of valid taxes that can be used for this tenant.
	 * @throws NoSuchTenantException			If the tenant doesn't exist.
	 * @throws TaxRatesNotAvailableException	If there are no tax rates available for this client / system / date.
	 */
	public List<Tax> getValidTaxRates(TaxSubjectIdentifier tsi, LocalDate taxDate) throws NoSuchTenantException, TaxRatesNotAvailableException;

	/**
	 * 
	 * @param tsi			Tax subject identifier. This is the tenant / client of the system.
	 * @param taxDomicile	Query for specific tax domicile (if it differs from tsi).
	 * @param taxDate		The date for which the taxes should be valid.
	 * @return				A list of valid taxes that can be used for this tenant.
	 * @throws NoSuchTenantException			If the tenant doesn't exist.
	 * @throws TaxRatesNotAvailableException	If there are no tax rates available for this client / system / date.
	 */
	public List<Tax> getValidTaxRates(TaxSubjectIdentifier tsi, String taxDomicile, LocalDate taxDate) throws NoSuchTenantException, TaxRatesNotAvailableException;
	
}
