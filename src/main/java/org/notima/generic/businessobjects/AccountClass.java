package org.notima.generic.businessobjects;

import java.util.Set;
import java.util.TreeSet;

/**
 * Constants to define account classes
 * 
 * @author Daniel Tamm
 *
 */
public class AccountClass {

	public static final String REVENUE = "revenue";
	public static final String COGS = "cogs";
	public static final String OTHER_EXP = "other-exp";
	public static final String STAFF = "staff";
	public static final String DEPRECIATIONS = "deprecations";
	public static final String FINANCIAL = "financial";
	public static final String CORPORATE_TAX_AJUSTMENTS = "tax-adjustments";
	public static final String CORPORATE_TAX = "tax";
	public static final String RESULT = "result";
	
	public static final Set<String> plTypes = new TreeSet<String>();
	
	public static final String ASSETS_LONG = "assets-long";
	public static final String ASSETS_SHORT = "assets-short";
	public static final String ASSETS_CASH = "assets-cash";
	public static final String EQUITY = "equity";
	public static final String UNTAXED_RESERVES = "untaxed-reserves";
	public static final String LIABILITIES_LONG = "liabilities-long";
	public static final String LIABILITIES_SHORT = "liabilities-short";
	
	public static final String UNKNOWN_CLASS = "unknown";

	public static final Set<String> balanceTypes = new TreeSet<String>();
	
	static {
		
		plTypes.add(REVENUE);
		plTypes.add(COGS);
		plTypes.add(OTHER_EXP);
		plTypes.add(STAFF);
		plTypes.add(FINANCIAL);
		plTypes.add(CORPORATE_TAX_AJUSTMENTS);
		plTypes.add(CORPORATE_TAX);
		plTypes.add(RESULT);
		
		balanceTypes.add(ASSETS_LONG);
		balanceTypes.add(ASSETS_SHORT);
		balanceTypes.add(ASSETS_CASH);
		balanceTypes.add(EQUITY);
		balanceTypes.add(UNTAXED_RESERVES);
		balanceTypes.add(LIABILITIES_LONG);
		balanceTypes.add(LIABILITIES_SHORT);
	};
	
	public static boolean isPLClass(String s) {

		if (s==null) return false;
		
		return(plTypes.contains(s.toLowerCase()));
		
	}
	
	public static boolean isBalanceClass(String s) {
		
		if (s==null) return false;
		
		return (balanceTypes.contains(s.toLowerCase()));
		
	}
	
	
}
