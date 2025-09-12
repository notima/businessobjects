package org.notima.generic.businessobjects.tax;

import java.util.List;

import org.notima.generic.businessobjects.Tax;
import org.notima.generic.businessobjects.exception.NoMatchingTaxException;

public class TaxTool {

	/**
	 * Get first match on rate.
	 * 
	 * @param percent					The rate to match.
	 * @param allowedDeviation 			If the rate can be this higher or lower than the percent argument.
	 * @param taxes						The taxes to check.
	 * @return
	 * @throws NoMatchingTaxException
	 */
	public static Tax getFirstMatchOnRate(double percent, double allowedDeviation, List<Tax> taxes) throws NoMatchingTaxException {
		
		if (taxes==null) throw new NoMatchingTaxException(percent);

		Tax result = null;
		
		for (Tax t : taxes) {
			if (percent == t.getRate()) {
				result = t;
				break;
			}
			if (allowedDeviation!=0.0) {
				if (percent <= t.getRate() + allowedDeviation && 
					 percent >= t.getRate() - allowedDeviation) {
					result = t;
					break;
				}
			}
		}
		
		if (result==null) {
			throw new NoMatchingTaxException(percent);
		}
		
		return result;
		
	}
	
}
