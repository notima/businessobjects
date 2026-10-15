package org.notima.generic.businessobjects;

import java.time.LocalDateTime;

/**
 * Records a change to a voucher's header after it was registered, e.g. a corrected
 * accounting date or description. Changes to lines are recorded on the lines themselves
 * (see {@link AccountingVoucherLine#getDeletedBy()} / {@link AccountingVoucherLine#getAddedBy()}).
 *
 * <p>Values are stored as text so any header field can be logged the same way; dates
 * are written as ISO dates (yyyy-MM-dd).
 */
public class AccountingVoucherChange {

	/** The accounting date ({@link AccountingVoucher#getAcctDate()}). */
	public static final String FIELD_ACCT_DATE = "acctDate";
	/** The voucher text ({@link AccountingVoucher#getDescription()}). */
	public static final String FIELD_DESCRIPTION = "description";

	/** Which header field was changed, one of the {@code FIELD_} constants. */
	private String field;
	/** The value before the change; {@code null} if it was empty. */
	private String oldValue;
	/** The value after the change; {@code null} if it is empty. */
	private String newValue;
	/** Who made the change. */
	private String changedBy;
	/** When the change was made. */
	private LocalDateTime changedDate;

	public AccountingVoucherChange() {}

	public AccountingVoucherChange(String field, String oldValue, String newValue, String changedBy, LocalDateTime changedDate) {
		this.field = field;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.changedBy = changedBy;
		this.changedDate = changedDate;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public LocalDateTime getChangedDate() {
		return changedDate;
	}

	public void setChangedDate(LocalDateTime changedDate) {
		this.changedDate = changedDate;
	}

}
