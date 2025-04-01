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
	protected String					destinationReference;
	
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
	public String getGeneralLedgerBankAccount() {
		return generalLedgerBankAccount;
	}
	public void setGeneralLedgerBankAccount(String generalLedgerBankAccount) {
		this.generalLedgerBankAccount = generalLedgerBankAccount;
	}
	public String getGeneralLedgerInTransitAccount() {
		return generalLedgerInTransitAccount;
	}
	public void setGeneralLedgerInTransitAccount(String generalLedgerInTransitAccount) {
		this.generalLedgerInTransitAccount = generalLedgerInTransitAccount;
	}
	public String getGeneralLedgerReconciliationAccount() {
		return generalLedgerReconciliationAccount;
	}
	public void setGeneralLedgerReconciliationAccount(String generalLedgerReconciliationAccount) {
		this.generalLedgerReconciliationAccount = generalLedgerReconciliationAccount;
	}
	public String getGeneralLedgerFeeAccount() {
		return generalLedgerFeeAccount;
	}
	public void setGeneralLedgerFeeAccount(String generalLedgerFeeAccount) {
		this.generalLedgerFeeAccount = generalLedgerFeeAccount;
	}
	public String getGeneralLedgerUnknownTrxAccount() {
		return generalLedgerUnknownTrxAccount;
	}
	public void setGeneralLedgerUnknownTrxAccount(String generalLedgerUnknownTrxAccount) {
		this.generalLedgerUnknownTrxAccount = generalLedgerUnknownTrxAccount;
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
	
}
