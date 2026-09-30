package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The result of checking a report file's payments against a channel's thresholds.
 *
 * @see PaymentBatchChannelThresholds#evaluate(List)
 */
public class ThresholdCheckResult {

	private int		paymentCount;
	private int		unmatchedCount;
	private Map<String, Double>	unmatchedAmountPerCurrency = new TreeMap<String, Double>();
	private List<String>		breaches = new ArrayList<String>();

	/**
	 * @return	True if any threshold is exceeded.
	 */
	public boolean isBreached() {
		return !breaches.isEmpty();
	}

	/**
	 * @return	A description of each exceeded threshold.
	 */
	public List<String> getBreaches() {
		return breaches;
	}

	void addBreach(String breach) {
		breaches.add(breach);
	}

	public int getPaymentCount() {
		return paymentCount;
	}

	void setPaymentCount(int paymentCount) {
		this.paymentCount = paymentCount;
	}

	public int getUnmatchedCount() {
		return unmatchedCount;
	}

	void setUnmatchedCount(int unmatchedCount) {
		this.unmatchedCount = unmatchedCount;
	}

	/**
	 * @return	Unmatched payments as a percentage of all payments. 0 if there are no payments.
	 */
	public double getUnmatchedPercent() {
		if (paymentCount==0) return 0d;
		return 100d * unmatchedCount / paymentCount;
	}

	/**
	 * @return	The sum of the unmatched payments' amounts (absolute values) per currency.
	 */
	public Map<String, Double> getUnmatchedAmountPerCurrency() {
		return unmatchedAmountPerCurrency;
	}

	void addUnmatchedAmount(String currency, double amount) {
		Double sum = unmatchedAmountPerCurrency.get(currency);
		unmatchedAmountPerCurrency.put(currency, (sum!=null ? sum : 0d) + Math.abs(amount));
	}

}
