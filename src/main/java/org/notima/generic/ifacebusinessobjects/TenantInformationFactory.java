package org.notima.generic.ifacebusinessobjects;

import java.io.IOException;

import org.notima.generic.businessobjects.TenantInformation;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;

/**
 * Factory for persistent TenantInformation entries.
 *
 * Implementations store tenant-specific configuration (e.g. default output
 * directories) keyed by TaxSubjectIdentifier.
 */
public interface TenantInformationFactory {

	/**
	 * Returns the TenantInformation for the given tenant, or null if not found.
	 */
	TenantInformation getTenantInformation(TaxSubjectIdentifier tenant);

	/**
	 * Persists (creates or updates) a TenantInformation entry.
	 *
	 * @return the persisted entry
	 */
	TenantInformation persistTenantInformation(TenantInformation ti) throws IOException;

}
