package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.Payment;

/**
 * Open amounts of invoices and unallocated amounts of payments, worked out from the
 * {@link PaymentAllocation allocations} between them: an invoice's open amount is its
 * opening amount less what's allocated to it, a payment's unallocated amount is its amount
 * less what's allocated of it. Neither is stored anywhere; this is the one place they're
 * calculated.
 *
 * <p>A snapshot: built from the invoices, payments and allocations it's given, and not
 * updated when they change. Only allocations that {@link PaymentAllocation#counts() count}
 * are included. Invoices are identified by {@link Invoice#getInvoiceKey() invoice key} and
 * {@link Invoice#isSalesTransaction() sales transaction}, payments by
 * {@link Payment#getPaymentId() payment id}.</p>
 *
 * <p>Amounts are in the documents' own sign (see {@link PaymentAllocationLine}). A payment's
 * amount is its {@link Payment#getOriginalAmount() original amount}, what the payer paid,
 * before any fees were deducted.</p>
 */
public class AllocationLedger {

	/** How far an invoice is paid. */
	public enum SettlementState {
		/** Nothing allocated. */
		OPEN,
		/** Partly allocated. */
		PARTIAL,
		/** Fully allocated. */
		PAID,
		/** More allocated than the invoice's amount. */
		OVERPAID
	}

	/** Opening amount of an invoice: its grand total, for invoices whose every payment is allocated here. */
	public static final Function<Invoice<?>, BigDecimal> GRAND_TOTAL = inv -> BigDecimal.valueOf(inv.getGrandTotal());

	/**
	 * Opening amount of an invoice: its open amount, for invoices imported with their open
	 * amount as of some date, where only payments after that date are allocated here.
	 */
	public static final Function<Invoice<?>, BigDecimal> OPEN_AMOUNT = inv -> BigDecimal.valueOf(inv.getOpenAmt());

	private final Function<Invoice<?>, BigDecimal> openingAmount;
	private final Map<String, Invoice<?>> invoices = new LinkedHashMap<String, Invoice<?>>();
	private final Map<String, Payment<?>> payments = new LinkedHashMap<String, Payment<?>>();
	private final List<PaymentAllocation> allocations = new ArrayList<PaymentAllocation>();

	/** Reduction of the open amount per invoice key, from counting allocations. */
	private final Map<String, BigDecimal> invoiceReductions = new HashMap<String, BigDecimal>();
	/** Allocated amount per payment id, from counting allocations. */
	private final Map<String, BigDecimal> paymentAllocated = new HashMap<String, BigDecimal>();

	/**
	 * A ledger where every invoice's opening amount is its grand total.
	 */
	public AllocationLedger(Collection<? extends Invoice<?>> invoices, Collection<? extends Payment<?>> payments,
			Collection<PaymentAllocation> allocations) {
		this(invoices, payments, allocations, GRAND_TOTAL);
	}

	/**
	 * @param openingAmount	The opening amount of an invoice, ie {@link #GRAND_TOTAL} or {@link #OPEN_AMOUNT}.
	 */
	public AllocationLedger(Collection<? extends Invoice<?>> invoices, Collection<? extends Payment<?>> payments,
			Collection<PaymentAllocation> allocations, Function<Invoice<?>, BigDecimal> openingAmount) {
		this.openingAmount = openingAmount != null ? openingAmount : GRAND_TOTAL;
		if (invoices != null) {
			for (Invoice<?> inv : invoices) {
				if (inv != null && inv.getInvoiceKey() != null) {
					this.invoices.put(invoiceId(inv.getInvoiceKey(), inv.isSalesTransaction()), inv);
				}
			}
		}
		if (payments != null) {
			for (Payment<?> p : payments) {
				if (p != null && p.getPaymentId() != null) {
					this.payments.put(p.getPaymentId(), p);
				}
			}
		}
		if (allocations != null) {
			for (PaymentAllocation a : allocations) {
				if (a != null && a.counts()) {
					this.allocations.add(a);
					add(a, BigDecimal.ONE);
				}
			}
		}
	}

	/** Adds (factor 1) or removes (factor -1) the effect of {@code a}. */
	private void add(PaymentAllocation a, BigDecimal factor) {
		if (a.getLines() == null) return;
		for (PaymentAllocationLine l : a.getLines()) {
			if (l.hasInvoice()) {
				invoiceReductions.merge(invoiceId(l.getInvoiceKey(), l.isSalesTransaction()),
						l.getInvoiceReduction().multiply(factor), BigDecimal::add);
			}
			if (l.hasPayment()) {
				paymentAllocated.merge(l.getPaymentId(), PaymentAllocationLine.nvl(l.getAmount()).multiply(factor), BigDecimal::add);
			}
		}
	}

	private static String invoiceId(String invoiceKey, boolean salesTransaction) {
		return (salesTransaction ? "S:" : "P:") + invoiceKey;
	}

	/** @return	The amount of a payment that can be allocated: what the payer paid. */
	public static BigDecimal paymentAmount(Payment<?> p) {
		Double amt = p != null ? p.getOriginalAmount() : null;
		return amt != null ? BigDecimal.valueOf(amt) : BigDecimal.ZERO;
	}

	// ------------------------------------------------------------------
	// Invoices
	// ------------------------------------------------------------------

	public Invoice<?> getInvoice(String invoiceKey, boolean salesTransaction) {
		return invoices.get(invoiceId(invoiceKey, salesTransaction));
	}

	/** @return	What's allocated to the invoice: payments, discounts and write-offs. */
	public BigDecimal getAllocatedAmount(String invoiceKey, boolean salesTransaction) {
		return invoiceReductions.getOrDefault(invoiceId(invoiceKey, salesTransaction), BigDecimal.ZERO);
	}

	/** @return	The invoice's open amount, or null if it isn't in this ledger. */
	public BigDecimal getOpenAmount(String invoiceKey, boolean salesTransaction) {
		Invoice<?> inv = getInvoice(invoiceKey, salesTransaction);
		if (inv == null) return null;
		return openingAmount.apply(inv).subtract(getAllocatedAmount(invoiceKey, salesTransaction));
	}

	/** @return	The invoice's open amount. */
	public BigDecimal getOpenAmount(Invoice<?> inv) {
		return openingAmount.apply(inv).subtract(getAllocatedAmount(inv.getInvoiceKey(), inv.isSalesTransaction()));
	}

	/** @return	How far the invoice is paid, taking the sign of credit notes into account. */
	public SettlementState getState(Invoice<?> inv) {
		BigDecimal opening = openingAmount.apply(inv);
		BigDecimal allocated = getAllocatedAmount(inv.getInvoiceKey(), inv.isSalesTransaction());
		if (opening.signum() < 0) {
			opening = opening.negate();
			allocated = allocated.negate();
		}
		if (allocated.signum() == 0) return opening.signum() == 0 ? SettlementState.PAID : SettlementState.OPEN;
		int cmp = allocated.compareTo(opening);
		if (cmp < 0) return SettlementState.PARTIAL;
		return cmp == 0 ? SettlementState.PAID : SettlementState.OVERPAID;
	}

	/** @return	Invoices of the given kind with an open amount, in the order given. */
	public List<Invoice<?>> getOpenInvoices(boolean salesTransaction) {
		List<Invoice<?>> result = new ArrayList<Invoice<?>>();
		for (Invoice<?> inv : invoices.values()) {
			if (inv.isSalesTransaction() == salesTransaction && getOpenAmount(inv).signum() != 0) {
				result.add(inv);
			}
		}
		return result;
	}

	// ------------------------------------------------------------------
	// Payments
	// ------------------------------------------------------------------

	public Payment<?> getPayment(String paymentId) {
		return payments.get(paymentId);
	}

	/** @return	What's allocated of the payment. */
	public BigDecimal getAllocatedAmount(String paymentId) {
		return paymentAllocated.getOrDefault(paymentId, BigDecimal.ZERO);
	}

	/** @return	The payment's unallocated amount; its whole amount if it has no id. */
	public BigDecimal getUnallocatedAmount(Payment<?> p) {
		BigDecimal amt = paymentAmount(p);
		return p.getPaymentId() != null ? amt.subtract(getAllocatedAmount(p.getPaymentId())) : amt;
	}

	/** @return	Payments with an unallocated amount, in the order given. */
	public List<Payment<?>> getUnallocatedPayments() {
		List<Payment<?>> result = new ArrayList<Payment<?>>();
		for (Payment<?> p : payments.values()) {
			if (DocumentStatus.counts(p.getStatus()) && getUnallocatedAmount(p).signum() != 0) {
				result.add(p);
			}
		}
		return result;
	}

	// ------------------------------------------------------------------
	// Allocations
	// ------------------------------------------------------------------

	/** @return	The counting allocations with a line for the invoice. */
	public List<PaymentAllocation> getAllocationsFor(Invoice<?> inv) {
		List<PaymentAllocation> result = new ArrayList<PaymentAllocation>();
		for (PaymentAllocation a : allocations) {
			for (PaymentAllocationLine l : lines(a)) {
				if (l.hasInvoice() && l.isSalesTransaction() == inv.isSalesTransaction()
						&& l.getInvoiceKey().equals(inv.getInvoiceKey())) {
					result.add(a);
					break;
				}
			}
		}
		return result;
	}

	/** @return	The counting allocations with a line for the payment. */
	public List<PaymentAllocation> getAllocationsFor(Payment<?> p) {
		List<PaymentAllocation> result = new ArrayList<PaymentAllocation>();
		if (p.getPaymentId() == null) return result;
		for (PaymentAllocation a : allocations) {
			for (PaymentAllocationLine l : lines(a)) {
				if (p.getPaymentId().equals(l.getPaymentId())) {
					result.add(a);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Suggests a draft allocation of {@code p} to {@code inv}: as much as both have, ie the
	 * whole invoice if the payment covers it, else the whole payment. What remains is left
	 * open or unallocated, for the user to allocate further or write off.
	 *
	 * @return	The suggestion, or null if nothing can be allocated (one of them is settled,
	 * 			or they're in opposite directions).
	 */
	public PaymentAllocation suggestAllocation(Payment<?> p, Invoice<?> inv, java.time.LocalDate date) {
		BigDecimal unallocated = getUnallocatedAmount(p);
		BigDecimal open = getOpenAmount(inv);
		if (unallocated.signum() == 0 || unallocated.signum() != open.signum()) return null;
		BigDecimal amount = unallocated.abs().compareTo(open.abs()) <= 0 ? unallocated : open;
		PaymentAllocation a = new PaymentAllocation(date, p.getCurrency() != null ? p.getCurrency() : inv.getCurrency());
		PaymentAllocationLine l = PaymentAllocationLine.paymentToInvoice(p.getOrCreatePaymentId(), inv.getInvoiceKey(),
				inv.isSalesTransaction(), amount);
		l.setBusinessPartnerKey(p.getBusinessPartnerKey());
		a.addLine(l);
		return a;
	}

	private static List<PaymentAllocationLine> lines(PaymentAllocation a) {
		return a.getLines() != null ? a.getLines() : Collections.<PaymentAllocationLine>emptyList();
	}

	/**
	 * Checks that {@code a} can be completed against this ledger: that what it refers to
	 * exists and is of the right kind and currency, that its offsetting lines sum to zero, and
	 * that it doesn't allocate more than is open of an invoice or unallocated of a payment.
	 * If {@code a} is already in this ledger, it's checked as if it weren't.
	 *
	 * @return	The problems found, empty if there are none.
	 */
	public List<String> validate(PaymentAllocation a) {
		List<String> problems = new ArrayList<String>();
		if (a.getLines() == null || a.getLines().isEmpty()) {
			problems.add("The allocation has no lines");
			return problems;
		}
		if (a.getAllocationDate() == null) {
			problems.add("The allocation has no date");
		}

		boolean included = allocations.contains(a);
		if (included) add(a, BigDecimal.ONE.negate());
		try {
			BigDecimal invoiceOnly = BigDecimal.ZERO;
			BigDecimal paymentOnly = BigDecimal.ZERO;
			Map<String, BigDecimal> invoiceReduction = new LinkedHashMap<String, BigDecimal>();
			Map<String, BigDecimal> paymentAmount = new LinkedHashMap<String, BigDecimal>();

			int lineNo = 0;
			for (PaymentAllocationLine l : a.getLines()) {
				lineNo++;
				String at = "Line " + lineNo + ": ";
				if (!l.hasPayment() && !l.hasInvoice()) {
					problems.add(at + "refers to neither a payment nor an invoice");
					continue;
				}
				if (l.hasInvoice() && l.hasChargeAccount()) {
					problems.add(at + "has both an invoice and a charge account");
				}
				if (!l.hasInvoice() && (PaymentAllocationLine.nvl(l.getDiscountAmount()).signum() != 0
						|| PaymentAllocationLine.nvl(l.getWriteOffAmount()).signum() != 0)) {
					problems.add(at + "has a discount or write-off but no invoice");
				}

				Invoice<?> inv = null;
				if (l.hasInvoice()) {
					inv = getInvoice(l.getInvoiceKey(), l.isSalesTransaction());
					if (inv == null) {
						problems.add(at + "unknown " + (l.isSalesTransaction() ? "customer" : "vendor") + " invoice " + l.getInvoiceKey());
					} else {
						checkCurrency(problems, at, a.getCurrency(), inv.getCurrency(), "invoice " + l.getInvoiceKey());
						invoiceReduction.merge(invoiceId(l.getInvoiceKey(), l.isSalesTransaction()), l.getInvoiceReduction(), BigDecimal::add);
					}
				}
				Payment<?> p = null;
				if (l.hasPayment()) {
					p = getPayment(l.getPaymentId());
					if (p == null) {
						problems.add(at + "unknown payment " + l.getPaymentId());
					} else {
						checkCurrency(problems, at, a.getCurrency(), p.getCurrency(), "payment " + l.getPaymentId());
						if (p.getStatus() == DocumentStatus.DRAFT) {
							problems.add(at + "payment " + l.getPaymentId() + " is a draft");
						}
						paymentAmount.merge(l.getPaymentId(), PaymentAllocationLine.nvl(l.getAmount()), BigDecimal::add);
					}
				}
				if (inv != null && p != null && inv.isSalesTransaction() != p.isCustomerPayment()) {
					problems.add(at + (inv.isSalesTransaction()
							? "a customer invoice can't be paid by a payment to a vendor"
							: "a vendor invoice can't be paid by a payment from a customer"));
				}
				if (l.hasInvoice() && !l.hasPayment()) {
					invoiceOnly = invoiceOnly.add(PaymentAllocationLine.nvl(l.getAmount()));
				}
				if (l.hasPayment() && !l.hasInvoice() && !l.hasChargeAccount()) {
					paymentOnly = paymentOnly.add(PaymentAllocationLine.nvl(l.getAmount()));
				}
			}

			if (invoiceOnly.signum() != 0) {
				problems.add("The amounts of the lines with only an invoice don't sum to zero (" + invoiceOnly.toPlainString() + ")");
			}
			if (paymentOnly.signum() != 0) {
				problems.add("The amounts of the lines with only a payment don't sum to zero (" + paymentOnly.toPlainString() + ")");
			}

			for (Map.Entry<String, BigDecimal> e : invoiceReduction.entrySet()) {
				Invoice<?> inv = invoices.get(e.getKey());
				BigDecimal open = getOpenAmount(inv);
				if (exceeds(e.getValue(), open)) {
					problems.add("More is allocated to invoice " + inv.getInvoiceKey() + " (" + e.getValue().toPlainString()
							+ ") than is open (" + open.toPlainString() + ")");
				}
			}
			for (Map.Entry<String, BigDecimal> e : paymentAmount.entrySet()) {
				Payment<?> p = payments.get(e.getKey());
				BigDecimal unallocated = getUnallocatedAmount(p);
				if (exceeds(e.getValue(), unallocated)) {
					problems.add("More is allocated of payment " + e.getKey() + " (" + e.getValue().toPlainString()
							+ ") than is unallocated (" + unallocated.toPlainString() + ")");
				}
			}
		} finally {
			if (included) add(a, BigDecimal.ONE);
		}
		return problems;
	}

	/**
	 * @return	True if allocating {@code amount} of {@code available} isn't possible: it's
	 * 			more than is available (120 of 100), or in the opposite direction (-10 of 100,
	 * 			anything of 0), which would increase what's open. Only a reversal may do that,
	 * 			and reversals aren't validated: they undo an allocation that was valid.
	 */
	private static boolean exceeds(BigDecimal amount, BigDecimal available) {
		if (amount.signum() == 0) return false;
		return amount.signum() != available.signum() || amount.abs().compareTo(available.abs()) > 0;
	}

	private static void checkCurrency(List<String> problems, String at, String allocationCurrency, String docCurrency, String what) {
		if (allocationCurrency != null && docCurrency != null && !allocationCurrency.equalsIgnoreCase(docCurrency)) {
			problems.add(at + what + " is in " + docCurrency + ", the allocation in " + allocationCurrency);
		}
	}

}
