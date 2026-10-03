package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;

/**
 * SPI interface for plugins that can read business partners (customers and/or suppliers)
 * directly from a connected system, for example through an accounting system's API.
 * <p>
 * Unlike a {@link BusinessPartnerFileImporter}, a provider usually needs credentials
 * and possibly a licence for the source system. The plugin is responsible for its own
 * configuration; {@link #isAvailable()} tells the caller whether it's ready to use.
 * <p>
 * A provider may give access to several companies (tenants). The caller picks one from
 * {@link #listTenants()} and asks for its business partners.
 *
 * @see BusinessPartnerFileImporter	for reading business partners from an exported file.
 */
public interface BusinessPartnerProvider {

	/**
	 * @return Identifier of the source system, e.g. {@code "Fortnox"}.
	 */
	String getSystemName();

	/**
	 * @return True if the provider is configured and able to connect, e.g. credentials
	 * 			are present. Returning true doesn't guarantee that calls will succeed.
	 */
	boolean isAvailable();

	/**
	 * @return The companies (tenants) business partners can be read for. Each entry has
	 * 			at least a name and a tax id. Never null.
	 * @throws Exception	If the source system can't be reached.
	 */
	List<BusinessPartner<?>> listTenants() throws Exception;

	/**
	 * Reads the business partners of a tenant.
	 *
	 * @param tenant		The tenant, identified by tax id and country code.
	 * @param customers		True to include customers.
	 * @param suppliers		True to include suppliers.
	 * @return				The business partners, with customer / vendor roles set. Never null.
	 * @throws Exception	If the tenant doesn't exist or the source system can't be reached.
	 */
	List<BusinessPartner<?>> lookupBusinessPartners(TaxSubjectIdentifier tenant,
			boolean customers, boolean suppliers) throws Exception;

}
