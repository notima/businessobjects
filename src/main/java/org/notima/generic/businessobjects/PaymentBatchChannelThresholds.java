package org.notima.generic.businessobjects;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Limits for when a payment channel's report file may be processed. A file whose payments
 * exceed any limit should not be processed.
 *
 * A payment is unmatched if no invoice was found for it, or if the invoice found is already
 * fully paid.
 *
 * All limits are optional. A null limit means no limit.
 */
public class PaymentBatchChannelThresholds {

	private static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ROOT));

	private Integer		maxUnmatchedCount;
	private Double		maxUnmatchedPercent;
	private String		maxUnmatchedAmount;

	/**
	 * @return	The max number of unmatched payments in a report file (all currencies).
	 */
	public Integer getMaxUnmatchedCount() {
		return maxUnmatchedCount;
	}

	public void setMaxUnmatchedCount(Integer maxUnmatchedCount) {
		this.maxUnmatchedCount = maxUnmatchedCount;
	}

	/**
	 * @return	The max share of unmatched payments in a report file, in percent.
	 */
	public Double getMaxUnmatchedPercent() {
		return maxUnmatchedPercent;
	}

	public void setMaxUnmatchedPercent(Double maxUnmatchedPercent) {
		this.maxUnmatchedPercent = maxUnmatchedPercent;
	}

	/**
	 * @return	The max unmatched amount per currency, ie "5000" (all currencies) or
	 * 			"5000,{500:EUR}" (500 for EUR, 5000 for other currencies).
	 */
	public String getMaxUnmatchedAmount() {
		return maxUnmatchedAmount;
	}

	/**
	 * @param maxUnmatchedAmount	The max unmatched amount per currency, ie "5000" or "5000,{500:EUR}".
	 * 								Null or blank for no limit.
	 * @throws IllegalArgumentException	If the definition is malformed or contains a value that isn't a number.
	 */
	public void setMaxUnmatchedAmount(String maxUnmatchedAmount) {
		CurrencyAccountMap limits = CurrencyAccountMap.parse(maxUnmatchedAmount);
		if (limits.getDefaultAccount()!=null) {
			parseAmount(limits.getDefaultAccount(), maxUnmatchedAmount);
		}
		for (String c : limits.getCurrencies()) {
			parseAmount(limits.getCurrencyAccount(c), maxUnmatchedAmount);
		}
		this.maxUnmatchedAmount = limits.isEmpty() ? null : maxUnmatchedAmount;
	}

	/**
	 * @return	True if any limit is set.
	 */
	public boolean hasLimits() {
		return maxUnmatchedCount!=null || maxUnmatchedPercent!=null || maxUnmatchedAmount!=null;
	}

	/**
	 * Checks whether a payment is unmatched, ie no invoice is found or the invoice is already paid.
	 * 
	 * A matched payment without a known open amount also counts as unmatched, since 
	 * {@link Payment#getMatchedInvoiceOpenAmount()} returns 0 when it isn't set. This happens if the
	 * invoice couldn't be looked up, which should also stop processing.
	 */
	public static boolean isUnmatched(Payment<?> p) {
		if (!p.hasMatchedInvoiceNo()) return true;
		return p.getMatchedInvoiceOpenAmount().doubleValue()==0d;
	}

	/**
	 * Checks the payments of one report file against the limits. The payments are expected
	 * to have been matched (matched invoice number and open amount set).
	 *
	 * @param fileBatches	The batches created from one report file (ie one per currency).
	 * @return	The result of the check.
	 */
	public ThresholdCheckResult evaluate(List<PaymentBatch> fileBatches) {

		ThresholdCheckResult result = new ThresholdCheckResult();
		int paymentCount = 0;
		int unmatchedCount = 0;

		if (fileBatches!=null) {
			for (PaymentBatch pb : fileBatches) {
				if (pb==null || !pb.hasPayments()) continue;
				String batchCurrency = pb.getBankAccount()!=null ? pb.getBankAccount().getCurrency() : null;
				for (Payment<?> p : pb.getPayments()) {
					paymentCount++;
					if (isUnmatched(p)) {
						unmatchedCount++;
						String currency = p.getCurrency()!=null ? p.getCurrency() : batchCurrency;
						result.addUnmatchedAmount(currency!=null ? currency.trim().toUpperCase() : "",
								p.getAmount()!=null ? p.getAmount() : 0d);
					}
				}
			}
		}
		result.setPaymentCount(paymentCount);
		result.setUnmatchedCount(unmatchedCount);

		if (maxUnmatchedCount!=null && unmatchedCount > maxUnmatchedCount) {
			result.addBreach(unmatchedCount + " unmatched payments, limit " + maxUnmatchedCount);
		}

		if (maxUnmatchedPercent!=null && result.getUnmatchedPercent() > maxUnmatchedPercent) {
			result.addBreach(AMOUNT_FORMAT.format(result.getUnmatchedPercent()) + " % unmatched payments ("
					+ unmatchedCount + " of " + paymentCount + "), limit " + AMOUNT_FORMAT.format(maxUnmatchedPercent) + " %");
		}

		if (maxUnmatchedAmount!=null) {
			CurrencyAccountMap limits = CurrencyAccountMap.parse(maxUnmatchedAmount);
			for (Map.Entry<String, Double> e : result.getUnmatchedAmountPerCurrency().entrySet()) {
				String currency = e.getKey();
				String limitStr = limits.getCurrencyAccount(currency);
				if (limitStr==null) {
					limitStr = limits.getDefaultAccount();
				}
				String label = currency.length()>0 ? currency : "(no currency)";
				if (limitStr==null) {
					result.addBreach("Unmatched " + label + " " + AMOUNT_FORMAT.format(e.getValue())
						+ ", no limit defined for " + label);
					continue;
				}
				double limit = parseAmount(limitStr, maxUnmatchedAmount);
				if (e.getValue() > limit) {
					result.addBreach("Unmatched " + label + " " + AMOUNT_FORMAT.format(e.getValue())
							+ ", limit " + AMOUNT_FORMAT.format(limit));
				}
			}
		}

		return result;
	}

	private static double parseAmount(String value, String definition) {
		try {
			return Double.parseDouble(value.trim());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Amount " + value + " is not a number in [" + definition + "]");
		}
	}

}
