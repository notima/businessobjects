package org.notima.generic.businessobjects.tax;

import java.math.BigDecimal;
import java.util.List;

/**
 * Works out how a supply is taxed and posted, as iDempiere's tax determination
 * ({@code Tax.get}): which {@link TaxRule} applies in a {@link TaxContext}, and the VAT
 * postings it gives. The accounts come from the rules, not from the chart of accounts.
 */
public interface TaxEngine {

	/** Chooses the rule for {@code context}. */
	TaxDetermination determine(TaxContext context);

	/**
	 * The VAT postings of a line under {@code rule}.
	 *
	 * @param net		The line's amount excluding VAT, in the document's own sign (positive
	 * 					for an invoice, negative for a credit note).
	 * @param vat		The VAT charged on the line, if known (it's then used as it is); null
	 * 					to calculate it from the rule's rate.
	 * @param sales		True for a sale, false for a purchase.
	 * @return	Debit positive, credit negative; empty if there's no VAT.
	 */
	List<TaxLine> taxLines(TaxRule rule, BigDecimal net, BigDecimal vat, boolean sales);

}
