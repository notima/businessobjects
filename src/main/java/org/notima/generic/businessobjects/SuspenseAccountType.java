package org.notima.generic.businessobjects;

/**
 * The kinds of suspense accounts: accounts where amounts are booked temporarily, that should
 * normally be zero. Which accounts they are varies between companies, so they're set per
 * tenant in {@link TenantInformation#getSuspenseAccounts()}.
 */
public enum SuspenseAccountType {

	/** We don't know what it is (Swedish "OBS-konto", ie 1799 or 2999). */
	UNIDENTIFIED,
	/** We believe we know what it is but it isn't settled yet, ie a double payment (in Sweden ie 1684). */
	IDENTIFIED,
	/** Settlement with the owner (in Sweden ie 2893). */
	OWNER_SETTLEMENT;

	/** @return	The type named {@code s}, ignoring case; null if none is. */
	public static SuspenseAccountType parse(String s) {
		if (s == null) return null;
		for (SuspenseAccountType t : values()) {
			if (t.name().equalsIgnoreCase(s.trim())) return t;
		}
		return null;
	}

}
