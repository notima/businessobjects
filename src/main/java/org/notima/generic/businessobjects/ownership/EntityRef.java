package org.notima.generic.businessobjects.ownership;

import java.util.Locale;

/**
 * A reference to a party &mdash; a person or an organization &mdash; that owns shares, is
 * owned, or is a party to a loan.
 *
 * <p>A party is identified by its register id (e.g. the UUID of the entry in a site's
 * company or person register) and/or its tax id (organisationsnummer / personnummer).
 * The register id identifies a party within one register; the tax id identifies it across
 * registers and systems. See {@link #matches(EntityRef)}.</p>
 */
public class EntityRef {

	private EntityKind	kind;
	private String		registryId;
	private String		taxId;
	private String		taxIdCountry;
	private String		name;

	public EntityRef() {}

	public EntityRef(EntityKind kind, String registryId, String taxId, String taxIdCountry, String name) {
		this.kind = kind;
		this.registryId = registryId;
		this.taxId = taxId;
		this.taxIdCountry = taxIdCountry;
		this.name = name;
	}

	public static EntityRef organization(String registryId, String taxId, String name) {
		return new EntityRef(EntityKind.ORGANIZATION, registryId, taxId, null, name);
	}

	public static EntityRef person(String registryId, String taxId, String name) {
		return new EntityRef(EntityKind.PERSON, registryId, taxId, null, name);
	}

	/**
	 * True if this and {@code other} refer to the same party: their register ids are
	 * equal, or, when either lacks a register id or the ids differ, their tax ids are
	 * equal after {@link #canonicalTaxId(String) canonicalization} (and their tax id
	 * countries don't contradict each other). Parties of different {@link EntityKind}s
	 * never match when both kinds are known.
	 *
	 * <p>Register ids that differ do not rule out a match: the same company can have
	 * different ids in different registers, and the tax id then decides.</p>
	 */
	public boolean matches(EntityRef other) {
		if (other == null) return false;
		if (other == this) return true;
		if (kind != null && other.kind != null && kind != other.kind) return false;
		if (registryId != null && registryId.equals(other.registryId)) return true;
		String a = canonicalTaxId(taxId);
		String b = canonicalTaxId(other.taxId);
		if (a.isEmpty() || !a.equals(b)) return false;
		return taxIdCountry == null || other.taxIdCountry == null
				|| taxIdCountry.equalsIgnoreCase(other.taxIdCountry);
	}

	/**
	 * Fills in the fields this reference lacks from {@code other}, which should
	 * {@link #matches(EntityRef) match} it.
	 */
	public void mergeFrom(EntityRef other) {
		if (other == null) return;
		if (kind == null) kind = other.kind;
		if (isBlank(registryId)) registryId = other.registryId;
		if (isBlank(taxId)) taxId = other.taxId;
		if (isBlank(taxIdCountry)) taxIdCountry = other.taxIdCountry;
		if (isBlank(name)) name = other.name;
	}

	/** A copy of this reference. */
	public EntityRef copy() {
		return new EntityRef(kind, registryId, taxId, taxIdCountry, name);
	}

	/**
	 * A tax id stripped to its letters and digits, uppercased, for comparison only:
	 * {@code "556123-4567"} becomes {@code "5561234567"}. Empty for {@code null}. A
	 * Swedish twelve digit organisationsnummer with the "16" prefix is shortened to ten
	 * digits.
	 */
	public static String canonicalTaxId(String taxId) {
		if (taxId == null) return "";
		String s = taxId.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
		if (s.length() == 12 && s.startsWith("16")) s = s.substring(2);
		return s;
	}

	public boolean isOrganization() {
		return kind == EntityKind.ORGANIZATION;
	}

	public boolean isPerson() {
		return kind == EntityKind.PERSON;
	}

	public EntityKind getKind() {
		return kind;
	}

	public void setKind(EntityKind kind) {
		this.kind = kind;
	}

	/** @return	The id of the party's entry in a register; may be null. */
	public String getRegistryId() {
		return registryId;
	}

	public void setRegistryId(String registryId) {
		this.registryId = registryId;
	}

	/** @return	Organisationsnummer / personnummer or the foreign equivalent; may be null. */
	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	/** @return	ISO 3166 country of the tax id, e.g. "SE"; may be null. */
	public String getTaxIdCountry() {
		return taxIdCountry;
	}

	public void setTaxIdCountry(String taxIdCountry) {
		this.taxIdCountry = taxIdCountry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name != null ? name : "?");
		if (!isBlank(taxId)) sb.append(" (").append(taxId).append(')');
		return sb.toString();
	}

}
