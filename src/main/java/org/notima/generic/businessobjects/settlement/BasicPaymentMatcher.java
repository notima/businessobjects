package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.Payment;
import org.notima.generic.businessobjects.TransactionReference;
import org.notima.generic.businessobjects.settlement.PaymentMatch.Confidence;

/**
 * Matches a payment to open invoices of the same kind (a customer's payment to customer
 * invoices, a payment to a vendor to vendor invoices) and currency:
 * <ol>
 *   <li>{@link Confidence#HIGH HIGH}: a reference of the payment is the invoice's OCR (digits
 *       only, leading zeros ignored) or invoice number. The references are the payment's
 *       {@link Payment#getMatchedInvoiceNo() matched invoice number},
 *       {@link Payment#getInvoiceNo() invoice number},
 *       {@link Payment#getDestinationSystemReference() destination system reference} (also
 *       what {@link Payment#getDestinationSystemReferenceRegex() its regex} finds in it,
 *       group 1 if it has one) and its transaction reference's invoice number, and each of
 *       their parts (of at least three characters) separated by spaces, commas, semicolons or
 *       colons, so "107193 5000.00" or "107208," find invoice 107193 or 107208.</li>
 *   <li>{@link Confidence#MEDIUM MEDIUM}: the payer is the invoice's business partner (by
 *       {@link Payment#getBusinessPartnerKey() key}, partner number or tax id) and the amount
 *       is the invoice's open amount.</li>
 *   <li>{@link Confidence#LOW LOW}: the amount is the open amount of only this invoice.</li>
 * </ol>
 * Candidates are sorted by confidence, then exact amount first, then earliest due date.
 */
public class BasicPaymentMatcher implements PaymentMatcher {

	@Override
	public List<PaymentMatch> match(Payment<?> payment, AllocationLedger ledger) {
		Set<String> refs = references(payment);
		BigDecimal amount = AllocationLedger.paymentAmount(payment);

		List<Invoice<?>> candidates = new ArrayList<Invoice<?>>();
		for (Invoice<?> inv : ledger.getOpenInvoices(payment.isCustomerPayment())) {
			if (payment.getCurrency() == null || inv.getCurrency() == null
					|| payment.getCurrency().equalsIgnoreCase(inv.getCurrency())) {
				candidates.add(inv);
			}
		}

		List<PaymentMatch> matches = new ArrayList<PaymentMatch>();
		List<Invoice<?>> sameAmount = new ArrayList<Invoice<?>>();
		for (Invoice<?> inv : candidates) {
			BigDecimal open = ledger.getOpenAmount(inv);
			String ocr = digits(inv.getOcr());
			String hit = null;
			if (ocr != null) {
				for (String r : refs) {
					if (ocr.equals(digits(r))) { hit = "OCR " + inv.getOcr(); break; }
				}
			}
			if (hit == null && inv.getInvoiceKey() != null) {
				for (String r : refs) {
					if (normalize(inv.getInvoiceKey()).equalsIgnoreCase(normalize(r))) { hit = "invoice number " + inv.getInvoiceKey(); break; }
				}
			}
			boolean exact = open.compareTo(amount) == 0;
			if (hit != null) {
				matches.add(new PaymentMatch(payment, inv, Confidence.HIGH, hit, open));
			} else if (exact && samePartner(payment, inv.getBusinessPartner())) {
				matches.add(new PaymentMatch(payment, inv, Confidence.MEDIUM, "business partner and amount", open));
			} else if (exact) {
				sameAmount.add(inv);
			}
		}
		if (matches.isEmpty() && sameAmount.size() == 1) {
			Invoice<?> inv = sameAmount.get(0);
			matches.add(new PaymentMatch(payment, inv, Confidence.LOW, "amount", ledger.getOpenAmount(inv)));
		}

		matches.sort(Comparator.comparing(PaymentMatch::getConfidence)
				.thenComparing(m -> !m.isExactAmount())
				.thenComparing(m -> m.getInvoice().getDueDate(), Comparator.nullsLast(Comparator.<Date>naturalOrder())));
		return matches;
	}

	/** The payment's references, trimmed, each once. */
	static Set<String> references(Payment<?> p) {
		Set<String> refs = new LinkedHashSet<String>();
		addRef(refs, p.getMatchedInvoiceNo());
		addRef(refs, p.getInvoiceNo());
		String dest = p.getDestinationSystemReference();
		addRef(refs, dest);
		if (dest != null && p.hasDestinationSystemReferenceRegex()) {
			try {
				Matcher m = Pattern.compile(p.getDestinationSystemReferenceRegex()).matcher(dest);
				if (m.find()) addRef(refs, m.groupCount() >= 1 ? m.group(1) : m.group());
			} catch (RuntimeException e) {
				// An invalid regex only means no extra reference
			}
		}
		TransactionReference tr = p.getTransactionReference();
		if (tr != null) addRef(refs, tr.getInvoiceNo());
		// Each part of a reference that has several, ie "107193 5000.00"; short parts would
		// match short invoice numbers by chance
		for (String ref : new java.util.ArrayList<String>(refs)) {
			for (String part : ref.split("[\\s,;:]+")) {
				if (part.length() >= 3) addRef(refs, part);
			}
		}
		return refs;
	}

	private static void addRef(Set<String> refs, String ref) {
		if (ref != null && !ref.trim().isEmpty()) refs.add(ref.trim());
	}

	/** The digits of {@code s} without leading zeros, or null if it has none. */
	static String digits(String s) {
		if (s == null) return null;
		String d = s.replaceAll("\\D", "").replaceFirst("^0+(?=.)", "");
		return d.isEmpty() ? null : d;
	}

	private static String normalize(String s) {
		return s.replaceAll("\\s", "");
	}

	private static boolean samePartner(Payment<?> p, BusinessPartner<?> bp) {
		if (bp == null) return false;
		Set<String> keys = new LinkedHashSet<String>();
		addRef(keys, p.getBusinessPartnerKey());
		BusinessPartner<?> payer = p.getBusinessPartner();
		if (payer != null) {
			addRef(keys, payer.getIdentityNo());
			addRef(keys, payer.getTaxId());
		}
		for (String k : keys) {
			if (k.equals(trim(bp.getIdentityNo())) || k.equals(trim(bp.getTaxId()))) return true;
		}
		return false;
	}

	private static String trim(String s) {
		return s != null ? s.trim() : null;
	}

}
