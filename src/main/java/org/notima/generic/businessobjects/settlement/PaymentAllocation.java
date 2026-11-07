package org.notima.generic.businessobjects.settlement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Allocates payments to invoices, as {@code C_AllocationHdr} in iDempiere.
 *
 * <p>An allocation is a document of its own, with its own date and its own voucher, so a
 * payment can exist on its own and be allocated later, partly, to several invoices, or be
 * written off or booked to an account (see {@link PaymentAllocationLine}).</p>
 *
 * <p>A completed allocation is never changed. To allocate differently, it's
 * {@link #createReversal(LocalDate) reversed} and a new allocation is made.</p>
 */
public class PaymentAllocation {

	private String			allocationId;
	private LocalDate		allocationDate;
	private String			currency;
	private String			description;
	private DocumentStatus	status = DocumentStatus.DRAFT;
	/** The allocation this one reverses, if it's a reversal. */
	private String			reversalOfId;
	/** The id of the voucher posting this allocation, if it's posted. */
	private String			voucherId;
	private List<PaymentAllocationLine>	lines = new ArrayList<PaymentAllocationLine>();

	public PaymentAllocation() {}

	public PaymentAllocation(LocalDate allocationDate, String currency) {
		this.allocationDate = allocationDate;
		this.currency = currency;
	}

	public PaymentAllocation addLine(PaymentAllocationLine line) {
		if (lines == null) lines = new ArrayList<PaymentAllocationLine>();
		lines.add(line);
		return this;
	}

	/** @return	The stable id, generating (and setting) a random one on first call if it has none yet. */
	public String getOrCreateId() {
		if (allocationId == null) {
			allocationId = java.util.UUID.randomUUID().toString();
		}
		return allocationId;
	}

	/** @return	True if this allocation counts in balances, ie isn't a draft. */
	public boolean counts() {
		return DocumentStatus.counts(status);
	}

	public boolean isReversal() {
		return reversalOfId != null;
	}

	/**
	 * Creates the reversal of this allocation: a new allocation with all amounts negated,
	 * dated {@code date} (ie today, if this allocation's date is in a closed period). Both
	 * this allocation and the reversal become {@link DocumentStatus#REVERSED}.
	 *
	 * @param date	The date of the reversal; this allocation's date if null.
	 * @return	The reversal.
	 * @throws IllegalStateException	If this allocation isn't completed.
	 */
	public PaymentAllocation createReversal(LocalDate date) {
		if (status != DocumentStatus.COMPLETED) {
			throw new IllegalStateException("Only a completed allocation can be reversed, this one is " + status);
		}
		PaymentAllocation r = new PaymentAllocation(date != null ? date : allocationDate, currency);
		r.getOrCreateId();
		r.reversalOfId = getOrCreateId();
		r.description = description;
		if (lines != null) {
			for (PaymentAllocationLine l : lines) {
				r.addLine(l.negated());
			}
		}
		r.status = DocumentStatus.REVERSED;
		status = DocumentStatus.REVERSED;
		return r;
	}

	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public LocalDate getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(LocalDate allocationDate) {
		this.allocationDate = allocationDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DocumentStatus getStatus() {
		return status;
	}

	public void setStatus(DocumentStatus status) {
		this.status = status;
	}

	public String getReversalOfId() {
		return reversalOfId;
	}

	public void setReversalOfId(String reversalOfId) {
		this.reversalOfId = reversalOfId;
	}

	public String getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}

	public List<PaymentAllocationLine> getLines() {
		return lines;
	}

	public void setLines(List<PaymentAllocationLine> lines) {
		this.lines = lines;
	}

}
