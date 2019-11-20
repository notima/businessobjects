package org.notima.generic.businessobjects.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InterestCalculator {

	public static DateFormat dfmt = new SimpleDateFormat("YYYY-MM-dd");
	
	public static double calculateFixedRate(String fromDate, String untilDate, double amount, double rate, int decimals) throws ParseException {
		return calculateFixedRate(dfmt.parse(fromDate), dfmt.parse(untilDate), amount, rate, decimals);
	}
	
	public static double calculateFixedRate(java.util.Date fromDate, java.util.Date untilDate, double amount, double rate, int decimals) {
		
		if (rate==0 || amount==0) return 0;
		Calendar fromCal = Calendar.getInstance();
		fromCal.setTime(fromDate);
		fromCal.set(Calendar.HOUR_OF_DAY, 0);
		fromCal.set(Calendar.MINUTE,0);
		fromCal.set(Calendar.SECOND, 0);
		fromCal.set(Calendar.MILLISECOND, 0);

		Calendar untilCal = Calendar.getInstance();
		untilCal.setTime(untilDate);
		untilCal.set(Calendar.HOUR_OF_DAY, 23);
		untilCal.set(Calendar.MINUTE,59);
		untilCal.set(Calendar.SECOND, 0);
		untilCal.set(Calendar.MILLISECOND, 0);
		
		long seconds = ((untilCal.getTimeInMillis() - fromCal.getTimeInMillis()) / 1000);
		
		double days = (double)Math.round(((double)seconds / (double)(3600*24)));
		
		double interest = amount * rate * (days / 365D);
		
		return roundToDecimals(interest, decimals);
		
	}
	
	public static double roundToDecimals(double amount, int decimals) {
		
		double multiplier = Math.pow(10, decimals);
		
		double result = (Math.round(amount*multiplier))/multiplier;
		
		return result;
		
	}
	
}
