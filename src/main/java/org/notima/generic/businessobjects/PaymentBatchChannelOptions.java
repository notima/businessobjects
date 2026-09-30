package org.notima.generic.businessobjects;

import java.io.File;
import java.util.Properties;

public class PaymentBatchChannelOptions {

	public static final String DIRECTORY = "directory";
	public static final String FILE_FILTER = "file-filter";
	
	private Properties 	destinationProperties;
	private Properties	sourceProperties;
	
	protected TaxSubjectIdentifier 		taxIdentifier;
	protected String					directory;
	protected transient File			directoryFile;
	protected String					defaultCurrency;
	protected String					generalLedgerBankAccount;
	protected String					generalLedgerInTransitAccount;
	protected String					generalLedgerReconciliationAccount;
	protected String					generalLedgerFeeAccount;
	protected String					generalLedgerUnknownTrxAccount;
	protected String					voucherSeries;
	protected String					sourceReference;
	protected String					sourceReferenceRegex;
	protected String					destinationReference;
	protected String					destinationReferenceRegex;
	protected PaymentBatchChannelThresholds	thresholds;
	
	public Properties getDestinationProperties() {
		return destinationProperties;
	}
	public void setDestinationProperties(Properties destinationProperties) {
		this.destinationProperties = destinationProperties;
	}
	public Properties getSourceProperties() {
		return sourceProperties;
	}
	public void setSourceProperties(Properties sourceProperties) {
		this.sourceProperties = sourceProperties;
	}

	public boolean hasDestinationProperties() {
		return destinationProperties!=null && !destinationProperties.isEmpty();
	}
	
	public boolean hasSourceProperties() {
		return sourceProperties!=null && !sourceProperties.isEmpty();
	}
	
	public String getSourceDirectory() {
		if (!hasSourceProperties()) return null;
		
		return sourceProperties.getProperty(DIRECTORY);
	}
	
	public void setSourceDirectory(String directory) {
		setSourceProperty(DIRECTORY, directory);
	}
	
	public String getSourceFileFilter() {
		if (!hasSourceProperties()) return null;
		
		return sourceProperties.getProperty(FILE_FILTER);
	}
	
	public void setSourceFileFilter(String filter) {
		setSourceProperty(FILE_FILTER, filter);
	}
	
	public void setSourceProperty(String key, String value) {
		if (key==null) return;
		if (sourceProperties==null) {
			sourceProperties = new Properties();
		}
		sourceProperties.setProperty(key, value);
	}
	
	public TaxSubjectIdentifier getTaxIdentifier() {
		return taxIdentifier;
	}
	public void setTaxIdentifier(TaxSubjectIdentifier taxIdentifier) {
		this.taxIdentifier = taxIdentifier;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public File getDirectoryFile() {
		return directoryFile;
	}
	public void setDirectoryFile(File directoryFile) {
		this.directoryFile = directoryFile;
	}
	public String getDefaultCurrency() {
		return defaultCurrency;
	}
	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}
	/**
	 * @return	The generalLedgerBankAccount for the default currency.
	 */
	public String getGeneralLedgerBankAccount() {
		return getGeneralLedgerBankAccount(null);
	}
	/**
	 * @param currency	The currency. If null, the default currency is assumed.
	 * @return	The generalLedgerBankAccount for given currency, or null if not defined at all.
	 * @throws UnknownCurrencyAccountException	If defined per currency, but not for given currency.
	 */
	public String getGeneralLedgerBankAccount(String currency) {
		return resolve("generalLedgerBankAccount", generalLedgerBankAccount, currency);
	}
	/**
	 * @return	The raw definition, ie "1991,{1992:EUR}".
	 */
	public String getGeneralLedgerBankAccountDefinition() {
		return generalLedgerBankAccount;
	}
	/**
	 * @param generalLedgerBankAccount	An account (used for all currencies) or an account definition, ie "1991,{1992:EUR}"
	 * @throws IllegalArgumentException	If the definition is malformed.
	 */
	public void setGeneralLedgerBankAccount(String generalLedgerBankAccount) {
		CurrencyAccountMap.parse(generalLedgerBankAccount);
		this.generalLedgerBankAccount = generalLedgerBankAccount;
	}
	/**
	 * @return	The generalLedgerInTransitAccount for the default currency.
	 */
	public String getGeneralLedgerInTransitAccount() {
		return getGeneralLedgerInTransitAccount(null);
	}
	/**
	 * @param currency	The currency. If null, the default currency is assumed.
	 * @return	The generalLedgerInTransitAccount for given currency, or null if not defined at all.
	 * @throws UnknownCurrencyAccountException	If defined per currency, but not for given currency.
	 */
	public String getGeneralLedgerInTransitAccount(String currency) {
		return resolve("generalLedgerInTransitAccount", generalLedgerInTransitAccount, currency);
	}
	/**
	 * @return	The raw definition, ie "1991,{1992:EUR}".
	 */
	public String getGeneralLedgerInTransitAccountDefinition() {
		return generalLedgerInTransitAccount;
	}
	/**
	 * @param generalLedgerInTransitAccount	An account (used for all currencies) or an account definition, ie "1991,{1992:EUR}"
	 * @throws IllegalArgumentException	If the definition is malformed.
	 */
	public void setGeneralLedgerInTransitAccount(String generalLedgerInTransitAccount) {
		CurrencyAccountMap.parse(generalLedgerInTransitAccount);
		this.generalLedgerInTransitAccount = generalLedgerInTransitAccount;
	}
	/**
	 * @return	The generalLedgerReconciliationAccount for the default currency.
	 */
	public String getGeneralLedgerReconciliationAccount() {
		return getGeneralLedgerReconciliationAccount(null);
	}
	/**
	 * @param currency	The currency. If null, the default currency is assumed.
	 * @return	The generalLedgerReconciliationAccount for given currency, or null if not defined at all.
	 * @throws UnknownCurrencyAccountException	If defined per currency, but not for given currency.
	 */
	public String getGeneralLedgerReconciliationAccount(String currency) {
		return resolve("generalLedgerReconciliationAccount", generalLedgerReconciliationAccount, currency);
	}
	/**
	 * @return	The raw definition, ie "1991,{1992:EUR}".
	 */
	public String getGeneralLedgerReconciliationAccountDefinition() {
		return generalLedgerReconciliationAccount;
	}
	/**
	 * @param generalLedgerReconciliationAccount	An account (used for all currencies) or an account definition, ie "1991,{1992:EUR}"
	 * @throws IllegalArgumentException	If the definition is malformed.
	 */
	public void setGeneralLedgerReconciliationAccount(String generalLedgerReconciliationAccount) {
		CurrencyAccountMap.parse(generalLedgerReconciliationAccount);
		this.generalLedgerReconciliationAccount = generalLedgerReconciliationAccount;
	}
	/**
	 * @return	The generalLedgerFeeAccount for the default currency.
	 */
	public String getGeneralLedgerFeeAccount() {
		return getGeneralLedgerFeeAccount(null);
	}
	/**
	 * @param currency	The currency. If null, the default currency is assumed.
	 * @return	The generalLedgerFeeAccount for given currency, or null if not defined at all.
	 * @throws UnknownCurrencyAccountException	If defined per currency, but not for given currency.
	 */
	public String getGeneralLedgerFeeAccount(String currency) {
		return resolve("generalLedgerFeeAccount", generalLedgerFeeAccount, currency);
	}
	/**
	 * @return	The raw definition, ie "1991,{1992:EUR}".
	 */
	public String getGeneralLedgerFeeAccountDefinition() {
		return generalLedgerFeeAccount;
	}
	/**
	 * @param generalLedgerFeeAccount	An account (used for all currencies) or an account definition, ie "1991,{1992:EUR}"
	 * @throws IllegalArgumentException	If the definition is malformed.
	 */
	public void setGeneralLedgerFeeAccount(String generalLedgerFeeAccount) {
		CurrencyAccountMap.parse(generalLedgerFeeAccount);
		this.generalLedgerFeeAccount = generalLedgerFeeAccount;
	}
	/**
	 * @return	The generalLedgerUnknownTrxAccount for the default currency.
	 */
	public String getGeneralLedgerUnknownTrxAccount() {
		return getGeneralLedgerUnknownTrxAccount(null);
	}
	/**
	 * @param currency	The currency. If null, the default currency is assumed.
	 * @return	The generalLedgerUnknownTrxAccount for given currency, or null if not defined at all.
	 * @throws UnknownCurrencyAccountException	If defined per currency, but not for given currency.
	 */
	public String getGeneralLedgerUnknownTrxAccount(String currency) {
		return resolve("generalLedgerUnknownTrxAccount", generalLedgerUnknownTrxAccount, currency);
	}
	/**
	 * @return	The raw definition, ie "1991,{1992:EUR}".
	 */
	public String getGeneralLedgerUnknownTrxAccountDefinition() {
		return generalLedgerUnknownTrxAccount;
	}
	/**
	 * @param generalLedgerUnknownTrxAccount	An account (used for all currencies) or an account definition, ie "1991,{1992:EUR}"
	 * @throws IllegalArgumentException	If the definition is malformed.
	 */
	public void setGeneralLedgerUnknownTrxAccount(String generalLedgerUnknownTrxAccount) {
		CurrencyAccountMap.parse(generalLedgerUnknownTrxAccount);
		this.generalLedgerUnknownTrxAccount = generalLedgerUnknownTrxAccount;
	}
	private String resolve(String optionName, String definition, String currency) {
		try {
			return CurrencyAccountMap.parse(definition).getAccount(currency, defaultCurrency);
		} catch (UnknownCurrencyAccountException e) {
			throw new UnknownCurrencyAccountException(optionName, e.getCurrency());
		}
	}
	
	public String getVoucherSeries() {
		return voucherSeries;
	}
	public void setVoucherSeries(String voucherSeries) {
		this.voucherSeries = voucherSeries;
	}
	
	/** 
	 * The name of the source's reference fields. If more than one field, this is a comma separated list.
	 * @return
	 */
	public String getSourceReference() {
		return sourceReference;
	}
	public void setSourceReference(String sourceReference) {
		this.sourceReference = sourceReference;
	}
	
	public boolean hasSourceReference() {
		return sourceReference!=null && sourceReference.trim().length()>0;
	}

	/**
	 * Regex applied to the source reference (the payment provider's own reference, e.g. merchant/order
	 * reference) before matching against the destination system.
	 * @return
	 */
	public String getSourceReferenceRegex() {
		return sourceReferenceRegex;
	}
	public void setSourceReferenceRegex(String sourceReferenceRegex) {
		this.sourceReferenceRegex = sourceReferenceRegex;
	}

	public boolean hasSourceReferenceRegex() {
		return sourceReferenceRegex!=null && sourceReferenceRegex.trim().length()>0;
	}

	/**
	 * The name of the destination's reference fields. If more than one field, this is a comma separated list.
	 * @return
	 */
	public String getDestinationReference() {
		return destinationReference;
	}
	public void setDestinationReference(String destinationReference) {
		this.destinationReference = destinationReference;
	}

	public boolean hasDestinationReference() {
		return destinationReference!=null && destinationReference.trim().length()>0;
	}

	/**
	 * Regex applied to the destination reference field when matching against the destination system.
	 * @return
	 */
	public String getDestinationReferenceRegex() {
		return destinationReferenceRegex;
	}
	public void setDestinationReferenceRegex(String destinationReferenceRegex) {
		this.destinationReferenceRegex = destinationReferenceRegex;
	}

	public boolean hasDestinationReferenceRegex() {
		return destinationReferenceRegex!=null && destinationReferenceRegex.trim().length()>0;
	}

	/**
	 * @return	Limits for when a report file may be processed. Can be null (no limits).
	 */
	public PaymentBatchChannelThresholds getThresholds() {
		return thresholds;
	}
	public void setThresholds(PaymentBatchChannelThresholds thresholds) {
		this.thresholds = thresholds;
	}

	public boolean hasThresholds() {
		return thresholds!=null && thresholds.hasLimits();
	}

}
