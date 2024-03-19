package org.notima.generic.ifacebusinessobjects;

import java.time.LocalDate;
import java.util.List;

import org.notima.generic.businessobjects.Tax;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;
import org.notima.generic.businessobjects.exception.NoSuchTenantException;
import org.notima.generic.businessobjects.exception.TaxRatesNotAvailableException;

/**
 * Interface to get valid tax rates. 
 * 
 * @author Daniel Tamm
 *
 */
public interface TaxRateProvider {

	public String getSystemName();
	
	public List<Tax> getValidTaxRates(TaxSubjectIdentifier tsi, LocalDate taxDate) throws NoSuchTenantException, TaxRatesNotAvailableException;
	
	public List<Tax> getValidTaxRates(TaxSubjectIdentifier tsi, String tradingCountry, LocalDate taxDate) throws NoSuchTenantException, TaxRatesNotAvailableException;
	
}
