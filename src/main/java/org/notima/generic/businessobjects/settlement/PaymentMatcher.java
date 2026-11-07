package org.notima.generic.businessobjects.settlement;

import java.util.List;

import org.notima.generic.businessobjects.Payment;

/**
 * Finds the invoices a payment may be for, among the open invoices of an
 * {@link AllocationLedger}. For systems that keep their invoices locally; a system that
 * matches on its own side (ie Fortnox, through its
 * {@link org.notima.generic.ifacebusinessobjects.PaymentBatchProcessor}) doesn't need one.
 */
public interface PaymentMatcher {

	/**
	 * @return	The candidate invoices, best first; empty if there are none.
	 */
	List<PaymentMatch> match(Payment<?> payment, AllocationLedger ledger);

}
