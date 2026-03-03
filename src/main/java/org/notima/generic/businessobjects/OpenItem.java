package org.notima.generic.businessobjects;

import java.time.LocalDate;

/**
 * Represents a single open item — either an unpaid invoice or an unallocated
 * payment belonging to a business partner.
 * <p>
 * Open items are used for reporting purposes (e.g. aged-debt reports, ledger
 * listings). Whether an invoice open item represents a sales or a vendor
 * invoice is determined by context (i.e. which account element the containing
 * {@link OpenItemList} is associated with).
 * <p>
 * All open items inside an {@link OpenItemList} share the same snapshot date
 * and the same {@link AccountElement}.
 */
public class OpenItem {

	/**
	 * Distinguishes the kind of open item.
	 * <p>
	 * Constant names are chosen to match their JSON representation directly so
	 * that no extra serializer configuration is required.
	 */
	public enum OpenItemType {
		/** An unpaid invoice (sales or vendor — determined by context). */
		Invoice,
		/** A payment that has not yet been allocated to an invoice. */
		UnallocatedPayment
	}

	private OpenItemType	openItemType;

	/** Key / document number in the source system. */
	private String			documentKey;

	/** Date the document was issued. */
	private LocalDate		documentDate;

	/** Due date (primarily meaningful for invoices). */
	private LocalDate		dueDate;

	/** The business partner this open item belongs to. */
	private BusinessPartner<?>	businessPartner;

	/** Outstanding (open) amount — positive for receivables, negative for payables. */
	private double			amount;

	private String			currency;

	// ------------------------------------------------------------------
	// Transient references to the originating domain objects.
	// These are never serialized; they let callers avoid re-fetching the
	// underlying object when it is already in memory.
	// ------------------------------------------------------------------

	private transient Invoice<?>	invoice;
	private transient Payment<?>	payment;

	// ------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------

	public OpenItem() {}

	public OpenItem(OpenItemType type) {
		this.openItemType = type;
	}

	// ------------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------------

	public OpenItemType getOpenItemType() {
		return openItemType;
	}

	public OpenItem setOpenItemType(OpenItemType openItemType) {
		this.openItemType = openItemType;
		return this;
	}

	public String getDocumentKey() {
		return documentKey;
	}

	public OpenItem setDocumentKey(String documentKey) {
		this.documentKey = documentKey;
		return this;
	}

	public LocalDate getDocumentDate() {
		return documentDate;
	}

	public OpenItem setDocumentDate(LocalDate documentDate) {
		this.documentDate = documentDate;
		return this;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public OpenItem setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	public BusinessPartner<?> getBusinessPartner() {
		return businessPartner;
	}

	public OpenItem setBusinessPartner(BusinessPartner<?> businessPartner) {
		this.businessPartner = businessPartner;
		return this;
	}

	public double getAmount() {
		return amount;
	}

	public OpenItem setAmount(double amount) {
		this.amount = amount;
		return this;
	}

	public String getCurrency() {
		return currency;
	}

	public OpenItem setCurrency(String currency) {
		this.currency = currency;
		return this;
	}

	public Invoice<?> getInvoice() {
		return invoice;
	}

	public OpenItem setInvoice(Invoice<?> invoice) {
		this.invoice = invoice;
		return this;
	}

	public Payment<?> getPayment() {
		return payment;
	}

	public OpenItem setPayment(Payment<?> payment) {
		this.payment = payment;
		return this;
	}

	@Override
	public String toString() {
		return "[" + openItemType + "] " + documentKey
				+ " " + businessPartner
				+ " amt=" + amount + " " + currency
				+ " due=" + dueDate;
	}
}
