package org.notima.generic.businessobjects.tax;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.notima.generic.businessobjects.Tax;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;
import org.notima.generic.businessobjects.exception.NoSuchTenantException;
import org.notima.generic.businessobjects.exception.TaxRatesNotAvailableException;
import org.notima.generic.businessobjects.util.CoreConstants;
import org.notima.generic.ifacebusinessobjects.TaxRateProvider;

/**
 * A simple tax rate provider for domestic sales in Sweden.
 * 
 */
public class BasicTaxRateProviderSE implements TaxRateProvider {

	private Locale thisLocale = new Locale("","SE");
	
	protected List<Tax> taxList;
	
	public BasicTaxRateProviderSE() {
		createSETaxList();
	}
	
	@Override
	public String getSystemName() {
		return CoreConstants.CORE_SYSTEM_NAME + "_" + thisLocale.getCountry();
	}

	@Override
	public List<Tax> getValidTaxRates(TaxSubjectIdentifier tsi, LocalDate taxDate)
			throws NoSuchTenantException, TaxRatesNotAvailableException {
		
		if (tsi==null) throw new NoSuchTenantException("null");
		
		return taxList;
	}

	@Override
	public List<Tax> getValidTaxRates(TaxSubjectIdentifier tsi, String tradingCountry, LocalDate taxDate)
			throws NoSuchTenantException, TaxRatesNotAvailableException {

		if (tradingCountry!=null && !tradingCountry.toUpperCase().equals(thisLocale.getCountry()))
			throw new TaxRatesNotAvailableException(tradingCountry);
		
		return getValidTaxRates(tsi, taxDate);
	}
	
	@Override
	public String getTaxDomicile() {
		return thisLocale.getCountry();
	}
	
	protected void createSETaxList() {
		
		taxList = new ArrayList<Tax>();
		addNormalTax();
		addMediumTax();
		addLowTax();
		addZeroTax();
		
	}
	
	private void addNormalTax() {
		Tax tax = new Tax("MP1", thisLocale.getCountry(), 25, "MP1","U1","I");
		taxList.add(tax);
	}

	private void addMediumTax() {
		Tax tax = new Tax("MP2", thisLocale.getCountry(), 12, "MP2","U2","I");
		taxList.add(tax);
	}
	
	private void addLowTax() {
		Tax tax = new Tax("MP3", thisLocale.getCountry(), 6, "MP3","U3","I");
		taxList.add(tax);
	}
	
	private void addZeroTax() {
		Tax tax = new Tax("MF", thisLocale.getCountry(), 0, "MF",null,"I");
		taxList.add(tax);
	}


}
