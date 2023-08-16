package org.notima.generic.ifacebusinessobjects;

import java.time.LocalDate;
import java.util.List;

import org.notima.generic.businessobjects.Tax;

/**
 * Interface to get valid tax rates. 
 * 
 * @author Daniel Tamm
 *
 */
public interface TaxRateProvider {

	public List<Tax> getValidTaxRates(String taxCountry, LocalDate taxDate);
	
	public List<Tax> getValidTaxRates(String taxCountry, String tradingCountry, LocalDate taxDate);
	
}
