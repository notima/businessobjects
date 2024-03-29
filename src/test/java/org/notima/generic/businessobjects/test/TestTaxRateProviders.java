package org.notima.generic.businessobjects.test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.Tax;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;
import org.notima.generic.businessobjects.exception.NoSuchTenantException;
import org.notima.generic.businessobjects.exception.TaxRatesNotAvailableException;
import org.notima.generic.businessobjects.tax.BasicTaxRateProviderFI;
import org.notima.generic.businessobjects.tax.BasicTaxRateProviderSE;
import org.notima.generic.ifacebusinessobjects.TaxRateProvider;

/**
 * Basic test class for a tax rate provider.
 * 
 */
public class TestTaxRateProviders {

	private List<TaxRateProvider> providers;
	private TaxRateProvider testedProvider;
	private List<Tax>		taxRates;
	private TaxSubjectIdentifier tsi;
	private LocalDate		taxDate;
	
	@Before
	public void setUp() throws Exception {
		initProviders();
		taxDate = LocalDate.now();
		tsi = new TaxSubjectIdentifier("556745-6941","SE");
	}

	private void initProviders() {
		providers = new ArrayList<TaxRateProvider>();
		
		// Add all known providers
		TaxRateProvider basicSE = new BasicTaxRateProviderSE();
		providers.add(basicSE);
		TaxRateProvider basicFI = new BasicTaxRateProviderFI();
		providers.add(basicFI);
	}
	
	@Test
	public void testAllProviders() {
		for (TaxRateProvider trp : providers) {
			testedProvider = trp;
			try {
				testProvider();
			} catch (NoSuchTenantException ne) {
				fail("No such tenant: " + ne.getMessage());				
			} catch (TaxRatesNotAvailableException ta) {
				fail("Tax rates not available");
			}
		}

	}
	
	private void testProvider() throws NoSuchTenantException, TaxRatesNotAvailableException {

		taxRates = testedProvider.getValidTaxRates(tsi, taxDate);
		org.junit.Assert.assertEquals(true, hasTaxRates());
		
	}
	
	private boolean hasTaxRates() {

		if (taxRates==null || taxRates.size()==0)
			return false;
		return true;
		
	}
	
	
}
