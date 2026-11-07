package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;

/**
 * One line of a {@link PaymentAllocation}, as {@code C_AllocationLine} in iDempiere.
 *
 * <p>A line refers to a payment, an invoice, or both, and what it does depends on which:</p>
 * <ul>
 *   <li><b>Payment and invoice</b>: {@link #getAmount() amount} of the payment is applied to
 *       the invoice. It reduces both the payment's unallocated amount and the invoice's open
 *       amount.</li>
 *   <li><b>Invoice only</b>: the {@link #getAmount() amount} is offset against other
 *       invoice-only lines of the same allocation, ie a credit note against an invoice. The
 *       amounts of such lines must sum to zero.</li>
 *   <li><b>Payment and {@link #getChargeAccountNo() charge account}</b>: the amount of the
 *       payment is booked to that account instead of an invoice, ie a bank fee or a payment
 *       that wasn't for an invoice at all.</li>
 *   <li><b>Payment only</b>: offset against other payment-only lines, ie a refund against a
 *       payment. The amounts of such lines must sum to zero.</li>
 * </ul>
 *
 * <p>{@link #getDiscountAmount() Discount} and {@link #getWriteOffAmount() write-off} reduce
 * the invoice's open amount without any money being paid, so they need an invoice.</p>
 *
 * <p>Amounts are in the document's own sign: a customer invoice and a payment from the
 * customer are both positive; a credit note and a refund to the customer are negative. The
 * same goes for vendor invoices and payments to the vendor. All amounts are in the
 * allocation's currency.</p>
 */
public class PaymentAllocationLine {

	private String		paymentId;
	/** The {@link org.notima.generic.businessobjects.Invoice#getInvoiceKey() invoice key}. */
	private String		invoiceKey;
	/** True if the invoice is a customer invoice, false if a vendor invoice. */
	private boolean		salesTransaction = true;
	private String		businessPartnerKey;
	private BigDecimal	amount = BigDecimal.ZERO;
	private BigDecimal	discountAmount = BigDecimal.ZERO;
	private BigDecimal	writeOffAmount = BigDecimal.ZERO;
	/** Account for the write-off, if other than the default for write-offs. */
	private String		writeOffAccountNo;
	private String		chargeAccountNo;
	private String		description;

	public PaymentAllocationLine() {}

	/**
	 * A line applying {@code amount} of a payment to an invoice.
	 */
	public static PaymentAllocationLine paymentToInvoice(String paymentId, String invoiceKey, boolean salesTransaction, BigDecimal amount) {
		PaymentAllocationLine l = new PaymentAllocationLine();
		l.setPaymentId(paymentId);
		l.setInvoiceKey(invoiceKey);
		l.setSalesTransaction(salesTransaction);
		l.setAmount(amount);
		return l;
	}

	/**
	 * A line booking {@code amount} of a payment to an account instead of an invoice.
	 */
	public static PaymentAllocationLine paymentToAccount(String paymentId, String chargeAccountNo, BigDecimal amount) {
		PaymentAllocationLine l = new PaymentAllocationLine();
		l.setPaymentId(paymentId);
		l.setChargeAccountNo(chargeAccountNo);
		l.setAmount(amount);
		return l;
	}

	/**
	 * A line writing off {@code writeOffAmount} of an invoice, without a payment.
	 */
	public static PaymentAllocationLine writeOff(String invoiceKey, boolean salesTransaction, BigDecimal writeOffAmount, String writeOffAccountNo) {
		PaymentAllocationLine l = new PaymentAllocationLine();
		l.setInvoiceKey(invoiceKey);
		l.setSalesTransaction(salesTransaction);
		l.setWriteOffAmount(writeOffAmount);
		l.setWriteOffAccountNo(writeOffAccountNo);
		return l;
	}

	public boolean hasPayment() {
		return paymentId != null;
	}

	public boolean hasInvoice() {
		return invoiceKey != null;
	}

	public boolean hasChargeAccount() {
		return chargeAccountNo != null && !chargeAccountNo.trim().isEmpty();
	}

	/** @return	How much the invoice's open amount is reduced: amount, discount and write-off. */
	public BigDecimal getInvoiceReduction() {
		return nvl(amount).add(nvl(discountAmount)).add(nvl(writeOffAmount));
	}

	/** @return	A copy of this line with all amounts negated, for a reversal. */
	public PaymentAllocationLine negated() {
		PaymentAllocationLine l = new PaymentAllocationLine();
		l.paymentId = paymentId;
		l.invoiceKey = invoiceKey;
		l.salesTransaction = salesTransaction;
		l.businessPartnerKey = businessPartnerKey;
		l.amount = nvl(amount).negate();
		l.discountAmount = nvl(discountAmount).negate();
		l.writeOffAmount = nvl(writeOffAmount).negate();
		l.writeOffAccountNo = writeOffAccountNo;
		l.chargeAccountNo = chargeAccountNo;
		l.description = description;
		return l;
	}

	static BigDecimal nvl(BigDecimal v) {
		return v != null ? v : BigDecimal.ZERO;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getInvoiceKey() {
		return invoiceKey;
	}

	public void setInvoiceKey(String invoiceKey) {
		this.invoiceKey = invoiceKey;
	}

	public boolean isSalesTransaction() {
		return salesTransaction;
	}

	public void setSalesTransaction(boolean salesTransaction) {
		this.salesTransaction = salesTransaction;
	}

	public String getBusinessPartnerKey() {
		return businessPartnerKey;
	}

	public void setBusinessPartnerKey(String businessPartnerKey) {
		this.businessPartnerKey = businessPartnerKey;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = nvl(amount);
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = nvl(discountAmount);
	}

	public BigDecimal getWriteOffAmount() {
		return writeOffAmount;
	}

	public void setWriteOffAmount(BigDecimal writeOffAmount) {
		this.writeOffAmount = nvl(writeOffAmount);
	}

	public String getWriteOffAccountNo() {
		return writeOffAccountNo;
	}

	public void setWriteOffAccountNo(String writeOffAccountNo) {
		this.writeOffAccountNo = writeOffAccountNo;
	}

	public String getChargeAccountNo() {
		return chargeAccountNo;
	}

	public void setChargeAccountNo(String chargeAccountNo) {
		this.chargeAccountNo = chargeAccountNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
