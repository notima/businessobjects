package org.notima.generic.businessobjects;

/**
 * A tenant's details for collection notices (inkassokrav) that override those from the
 * adapter, ie a separate phone number, e-mail and account for collection matters.
 * Unset (null or blank) values don't override.
 */
public class CollectionNoticeInfo {

	/** Contact phone on the notice. */
	private String phone;
	/** Contact e-mail on the notice. */
	private String email;
	/** The account to pay to, ie a bankgiro or plusgiro number. */
	private String remitToAccount;
	/** The type of {@link #remitToAccount}, BG (bankgiro) or PG (plusgiro). */
	private String remitToAccountType;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemitToAccount() {
		return remitToAccount;
	}

	public void setRemitToAccount(String remitToAccount) {
		this.remitToAccount = remitToAccount;
	}

	/**
	 * @return	BG (bankgiro) or PG (plusgiro). Can be null.
	 */
	public String getRemitToAccountType() {
		return remitToAccountType;
	}

	public void setRemitToAccountType(String remitToAccountType) {
		this.remitToAccountType = remitToAccountType;
	}

	public boolean hasPhone() {
		return hasText(phone);
	}

	public boolean hasEmail() {
		return hasText(email);
	}

	public boolean hasRemitToAccount() {
		return hasText(remitToAccount);
	}

	/**
	 * @return	True if the remit to account type is PG.
	 */
	public boolean isPlusgiro() {
		return remitToAccountType != null && "PG".equalsIgnoreCase(remitToAccountType.trim());
	}

	/**
	 * @return	True if nothing is set.
	 */
	public boolean isEmpty() {
		return !hasPhone() && !hasEmail() && !hasRemitToAccount() && !hasText(remitToAccountType);
	}

	private static boolean hasText(String s) {
		return s != null && s.trim().length() > 0;
	}

}
