package org.notima.factoring;

/**
 * Utility class for operations on detail rows.
 * 
 * @author Daniel Tamm
 *
 */
public class DetailRowUtility {

	/**
	 * Calculates total amount for detail rows.
	 * 
	 * The highest precision/rounding decimal for a single detail row is used to round the total.
	 * 
	 * @param rows
	 * @return
	 */
	public static double calculateTotalAmountWithTax(DetailRow[] rows) {

		int maxDecimals = 0;
		
		double result = 0.0;
		for (int i=0; i<rows.length; i++) {
			result += rows[i].getLineTotalWithTax();
			maxDecimals = Math.max(maxDecimals, rows[i].getRoundingDecimals());
		}

		long integervalue = Math.round(result * Math.pow(10, maxDecimals));
		result = (double)integervalue/Math.pow(10, maxDecimals);

		
		return result;
		
	}
	
	/**
	 * Calculates total amount for detail rows
	 * 
	 * @param rows
	 * @param roundingDecimals, may be different for different factoring providers.
	 * @return
	 */
	public static double calculateTotalAmountWithTax(DetailRow[] rows, int roundingDecimals) {

		double result = 0.0;
		for (int i=0; i<rows.length; i++) {
			rows[i].setRoundingDecimals(roundingDecimals);
			result += rows[i].getLineTotalWithTax();
		}
		
		long integervalue = Math.round(result * Math.pow(10, roundingDecimals));
		result = (double)integervalue/Math.pow(10, roundingDecimals);
		
		return result;
		
	}
	
	
}
