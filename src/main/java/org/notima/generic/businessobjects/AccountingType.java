package org.notima.generic.businessobjects;

/**
 * Constants to classify different kinds of accounts.
 * 
 * @author Daniel Tamm
 *
 */
public class AccountingType {

	public static final String 	REVENUE = "revenue";
	public static final String	REVENUE_UNCLEAR = "revenue-unclear";
	public static final String	COGS = "cogs";
	public static final String	OTHER_EXPENSES = "other-exp";
	public static final String 	LIABILITY_VENDOR = "liability-vendor";
	public static final String	LIABILITY_VAT = "liability-vat";
	public static final String	LIABILITY_OTHER = "liability-other";
	public static final String	CLAIM_VAT = "claim-vat";
	public static final String	OTHER_EXPENSES_SALES = "other-exp-sales";
	public static final String	ASSET_DEPOSIT = "asset-deposit";
	public static final String	LIQUID_ASSET_AR ="liquid-asset-ar";
	public static final String	LIQUID_ASSET_CASH = "liquid-asset-cash";
	public static final String	UNKNOWN_BALANCE_TRX = "unknown-balance-trx";
	public static final String	INTEREST_INCOME = "interest-income";
	public static final String	INTEREST_EXPENSE = "interest-expense";
	public static final String	ROUNDING = "rounding";

	// Settlement of payments against invoices (see the settlement package)

	/** Payments received from customers that aren't allocated to an invoice yet (in Sweden ie 2420). */
	public static final String	LIABILITY_CUSTOMER_PREPAYMENT = "liability-customer-prepayment";
	/** Payments made to vendors that aren't allocated to an invoice yet (in Sweden ie 1480). */
	public static final String	ASSET_VENDOR_PREPAYMENT = "asset-vendor-prepayment";
	/** Customer invoice amounts written off as lost (in Sweden ie 6351). */
	public static final String	WRITE_OFF_CUSTOMER = "write-off-customer";
	/** Vendor invoice amounts written off, ie small differences not paid (in Sweden ie 3740). */
	public static final String	WRITE_OFF_VENDOR = "write-off-vendor";
	/** Discounts granted to customers when they pay (in Sweden ie 3731). */
	public static final String	DISCOUNT_GRANTED = "discount-granted";
	/** Discounts received from vendors when paying (in Sweden ie 4731). */
	public static final String	DISCOUNT_RECEIVED = "discount-received";


}
