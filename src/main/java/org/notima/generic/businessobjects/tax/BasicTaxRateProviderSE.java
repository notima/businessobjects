package org.notima.generic.businessobjects.tax;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.notima.generic.businessobjects.Tax;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;
import org.notima.generic.businessobjects.exception.NoSuchTenantException;
import org.notima.generic.businessobjects.exception.TaxRatesNotAvailableException;
import org.notima.generic.businessobjects.util.CoreConstants;
import org.notima.generic.ifacebusinessobjects.TaxRateProvider;

/**
 * A simple tax rate provider for sales from Sweden.
 * 
 * <p>Domestic rates 25 % (MP1), 12 % (MP2), 6 % (MP3) and 0 % (MF), followed by the
 * zero-rated sales that are reported separately in the VAT return:</p>
 * <ul>
 *   <li>{@value #KEY_SE_REVERSE_CHARGE}: domestic sales where the buyer is liable for the VAT
 *       (omv&auml;nd skattskyldighet, e.g. construction services)</li>
 *   <li>{@value #KEY_EU_GOODS}: sales of goods to a VAT-registered buyer in another EU country</li>
 *   <li>{@value #KEY_EU_SERVICES}: sales of services to a VAT-registered buyer in another EU country</li>
 *   <li>{@value #KEY_EXPORT_GOODS}: export of goods outside the EU</li>
 *   <li>{@value #KEY_EXPORT_SERVICES}: sales of services outside the EU</li>
 * </ul>
 * <p>The 0 % rates come after MF, so a lookup on rate alone
 * ({@link TaxTool#getFirstMatchOnRate}) still finds MF.</p>
 * 
 */
public class BasicTaxRateProviderSE implements TaxRateProvider {

	/** Domestic sales, reverse charge (omvänd skattskyldighet). */
	public static final String KEY_SE_REVERSE_CHARGE = "REVD";
	/** Sales of goods to another EU country. */
	public static final String KEY_EU_GOODS = "VTEU";
	/** Sales of services to another EU country. */
	public static final String KEY_EU_SERVICES = "FTEU";
	/** Export of goods outside the EU. */
	public static final String KEY_EXPORT_GOODS = "E";
	/** Sales of services outside the EU. */
	public static final String KEY_EXPORT_SERVICES = "ES";

	private static final String EU = "EU";
	
	private Locale thisLocale = Locale.forLanguageTag("sv-SE");
	private Set<String> domiciles;
	
	protected List<Tax> taxList;
	
	public BasicTaxRateProviderSE() {
		createSETaxList();
		domiciles = new TreeSet<String>();
		domiciles.add(this.thisLocale.getCountry());
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
	public Set<String> getTaxDomiciles() {
		return domiciles;
	}
	
	protected void createSETaxList() {
		
		taxList = new ArrayList<Tax>();
		addNormalTax();
		addMediumTax();
		addLowTax();
		addZeroTax();
		addZeroRatedTax(KEY_SE_REVERSE_CHARGE, true, null, true, true);
		addZeroRatedTax(KEY_EU_GOODS, true, EU, true, false);
		addZeroRatedTax(KEY_EU_SERVICES, true, EU, false, true);
		addZeroRatedTax(KEY_EXPORT_GOODS, false, null, true, false);
		addZeroRatedTax(KEY_EXPORT_SERVICES, false, null, false, true);
		
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

	/**
	 * Adds a 0 % rate that is reported as its own kind of sales.
	 * 
	 * @param key					Tax key, also used as the revenue VAT code.
	 * @param reverseCharge			True if the buyer accounts for the VAT.
	 * @param otherCountryGroup		Country group of the buyer, e.g. EU, or null.
	 * @param goods					True if it applies to goods.
	 * @param services				True if it applies to services.
	 */
	private void addZeroRatedTax(String key, boolean reverseCharge, String otherCountryGroup, boolean goods, boolean services) {
		Tax tax = new Tax(key, thisLocale.getCountry(), 0, key, null, null);
		tax.setReverseCharge(reverseCharge);
		tax.setOtherCountryGroup(otherCountryGroup);
		tax.setAppliesToGoods(goods);
		tax.setAppliesToServices(services);
		taxList.add(tax);
	}

}
