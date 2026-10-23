package org.notima.generic.businessobjects;

import org.notima.generic.businessobjects.AccountElement.AccountElementType;

/**
 * Semantic classification of an account, used to group accounts in balance sheet and
 * P&amp;L reports. Stored by its {@link #name()} in {@link AccountElement#getAccountClass()}.
 *
 * <p>Which category an account has is decided by the {@link
 * org.notima.generic.businessobjects.accounting.rule.CountryRuleSet rules of its country},
 * ie from its number; everything else goes by the category, so that it works with any
 * country's chart of accounts. For the Swedish BAS chart of accounts:
 * <ul>
 *   <li>1xxx (fixed)   &rarr; ASSET_LONG</li>
 *   <li>1xxx (current) &rarr; ASSET_SHORT</li>
 *   <li>19xx (cash and bank) &rarr; ASSET_CASH</li>
 *   <li>2xxx (equity)  &rarr; EQUITY</li>
 *   <li>2xxx (untaxed) &rarr; UNTAXED_RESERVE</li>
 *   <li>2xxx (long-term debt)  &rarr; LIABILITY_LONG</li>
 *   <li>2xxx (short-term debt) &rarr; LIABILITY_SHORT, VAT_CLAIM, VAT_LIABILITY, TAX_LIABILITY</li>
 *   <li>3xxx &rarr; REVENUE</li>
 *   <li>4xxx &rarr; COGS</li>
 *   <li>5&ndash;6xxx &rarr; OPERATING_EXPENSE</li>
 *   <li>7xxx &rarr; SALARY</li>
 *   <li>8xxx &rarr; FINANCIAL_INCOME / FINANCIAL_EXPENSE / TAX_EXPENSE</li>
 * </ul>
 *
 * <p>New values can be added without breaking stored data, which refers to them by name.
 * Replaces the older, coarser string constants in {@link AccountClass}, which
 * {@link #parse(String)} accepts too.</p>
 */
public enum AccountCategory {

	ASSET_LONG,
	ASSET_SHORT,
	/** Cash and bank accounts. */
	ASSET_CASH,
	UNTAXED_RESERVE,
	LIABILITY_LONG,
	LIABILITY_SHORT,
	EQUITY,
	VAT_CLAIM,
	VAT_LIABILITY,
	TAX_LIABILITY,
	REVENUE,
	REVENUE_OTHER,
	COGS,
	OPERATING_EXPENSE,
	SALARY,
	IMPAIRMENTS,
	DEPRECIATIONS,
	OTHER_OPEX,
	FINANCIAL_INCOME,
	FINANCIAL_EXPENSE,
	TAX_EXPENSE,
	OTHER;

	/**
	 * Whether accounts of this category are balance sheet or P&amp;L accounts; {@code null} for
	 * {@link #OTHER}, which can be either.
	 */
	public AccountElementType getElementType() {
		switch (this) {
			case ASSET_LONG: case ASSET_SHORT: case ASSET_CASH: case UNTAXED_RESERVE:
			case LIABILITY_LONG: case LIABILITY_SHORT: case EQUITY:
			case VAT_CLAIM: case VAT_LIABILITY: case TAX_LIABILITY:
				return AccountElementType.BalanceAcct;
			case OTHER:
				return null;
			default:
				return AccountElementType.PLAcct;
		}
	}

	/**
	 * The category stored in an {@link AccountElement#getAccountClass()}: its {@link #name()},
	 * or one of the older {@link AccountClass} constants (ie "assets-long").
	 *
	 * @return	The category, or null if {@code s} is null, empty or unknown.
	 */
	public static AccountCategory parse(String s) {
		if (s == null || s.isEmpty()) return null;
		try {
			return valueOf(s);
		} catch (IllegalArgumentException e) {
			return fromLegacy(s);
		}
	}

	/** The category of one of the older {@link AccountClass} constants; null if none matches. */
	private static AccountCategory fromLegacy(String s) {
		switch (s) {
			case AccountClass.ASSETS_LONG:			return ASSET_LONG;
			case AccountClass.ASSETS_SHORT:			return ASSET_SHORT;
			case AccountClass.ASSETS_CASH:			return ASSET_CASH;
			case AccountClass.EQUITY:				return EQUITY;
			case AccountClass.UNTAXED_RESERVES:		return UNTAXED_RESERVE;
			case AccountClass.LIABILITIES_LONG:		return LIABILITY_LONG;
			case AccountClass.LIABILITIES_SHORT:	return LIABILITY_SHORT;
			case AccountClass.REVENUE:				return REVENUE;
			case AccountClass.COGS:					return COGS;
			case AccountClass.OTHER_EXP:			return OPERATING_EXPENSE;
			case AccountClass.STAFF:				return SALARY;
			case AccountClass.DEPRECIATIONS:		return DEPRECIATIONS;
			case AccountClass.CORPORATE_TAX:		return TAX_EXPENSE;
			default:								return null;
		}
	}

}
