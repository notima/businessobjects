package org.notima.generic.businessobjects.tax;

import java.util.List;

import org.notima.generic.businessobjects.Tax;
import org.notima.generic.businessobjects.exception.NoMatchingTaxException;

public class TaxTool {

	/**
	 * Get first match on rate.
	 * 
	 * @param percent
	 * @param taxes
	 * @return
	 * @throws NoMatchingTaxException
	 */
	public static Tax getFirstMatchOnRate(double percent, List<Tax> taxes) throws NoMatchingTaxException {
		
		if (taxes==null) throw new NoMatchingTaxException(percent);

		Tax result = null;
		
		for (Tax t : taxes) {
			if (percent == t.getRate()) {
				result = t;
				break;
			}
		}
		
		return result;
		
	}
	
}
