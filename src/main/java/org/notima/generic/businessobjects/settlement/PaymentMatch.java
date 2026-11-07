package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;

import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.Payment;

/**
 * A suggestion from a {@link PaymentMatcher} that a payment is for an invoice.
 */
public class PaymentMatch {

	/** How sure the match is. */
	public enum Confidence {
		/** A reference of the payment is the invoice's OCR or invoice number. */
		HIGH,
		/** The payer is the invoice's business partner and the amount is the invoice's open amount. */
		MEDIUM,
		/** Only the amount is the same, and no other open invoice has it. */
		LOW
	}

	private final Payment<?>	payment;
	private final Invoice<?>	invoice;
	private final Confidence	confidence;
	private final String		reason;
	private final BigDecimal	openAmount;

	public PaymentMatch(Payment<?> payment, Invoice<?> invoice, Confidence confidence, String reason, BigDecimal openAmount) {
		this.payment = payment;
		this.invoice = invoice;
		this.confidence = confidence;
		this.reason = reason;
		this.openAmount = openAmount;
	}

	public Payment<?> getPayment() {
		return payment;
	}

	public Invoice<?> getInvoice() {
		return invoice;
	}

	public Confidence getConfidence() {
		return confidence;
	}

	/** @return	What matched, ie {@code "OCR 1234"}, in English. */
	public String getReason() {
		return reason;
	}

	/** @return	The invoice's open amount when matched. */
	public BigDecimal getOpenAmount() {
		return openAmount;
	}

	/** @return	True if the payment's amount is exactly the invoice's open amount. */
	public boolean isExactAmount() {
		return AllocationLedger.paymentAmount(payment).compareTo(openAmount) == 0;
	}

	@Override
	public String toString() {
		return confidence + " " + invoice.getInvoiceKey() + " (" + reason + ")";
	}

}
