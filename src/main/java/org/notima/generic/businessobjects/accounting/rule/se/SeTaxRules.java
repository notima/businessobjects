package org.notima.generic.businessobjects.accounting.rule.se;

import java.util.ArrayList;
import java.util.List;

import org.notima.generic.businessobjects.tax.BasicTaxRateProviderSE;
import org.notima.generic.businessobjects.tax.TaxCategory;
import org.notima.generic.businessobjects.tax.TaxRule;
import org.notima.generic.businessobjects.tax.TaxScope;
import org.notima.generic.businessobjects.tax.TradeArea;

/**
 * The usual tax rules of a Swedish company, with accounts of the BAS chart of accounts and
 * the boxes of the VAT return (momsdeklarationen) they're reported in.
 *
 * <ul>
 *   <li><b>Domestic</b> sales and purchases at 25, 12, 6 and 0 % (MP1, MP2, MP3, MF; the same
 *       keys for sales and purchases, told apart by scope). These apply to any trade area, so
 *       they're also what's left for sales to EU consumers.</li>
 *   <li><b>EU sales</b> to VAT registered buyers, goods (VTEU) and services (FTEU), and
 *       <b>export</b> outside the EU (E, ES), without VAT.</li>
 *   <li><b>Domestic reverse charge</b> (REVD, ie construction) for counterparties whose VAT
 *       treatment is {@code SEREVERSEDVAT}.</li>
 *   <li><b>EU purchases</b> of goods and services, <b>import</b> of services and goods, with
 *       reverse charge per category.</li>
 * </ul>
 */
public final class SeTaxRules {

	/** VAT treatment of a counterparty under domestic reverse charge (as in Fortnox). */
	public static final String VAT_TYPE_SE_REVERSED = "SEREVERSEDVAT";

	private SeTaxRules() {}

	public static List<TaxRule> defaults() {
		List<TaxRule> rules = new ArrayList<TaxRule>();

		// --- Domestic sales ---
		rules.add(sale("MP1", "Försäljning 25 %", TaxCategory.STANDARD, 25).outputVat("2611").revenue("3001").reporting("05", "10"));
		rules.add(sale("MP2", "Försäljning 12 %", TaxCategory.REDUCED_1, 12).outputVat("2621").revenue("3002").reporting("05", "11"));
		rules.add(sale("MP3", "Försäljning 6 %", TaxCategory.REDUCED_2, 6).outputVat("2631").revenue("3003").reporting("05", "12"));
		rules.add(sale("MF", "Momsfri försäljning", TaxCategory.EXEMPT, 0).revenue("3004").reporting("42"));

		// --- Sales abroad and with reverse charge ---
		rules.add(sale(BasicTaxRateProviderSE.KEY_EU_GOODS, "Försäljning varor till annat EU-land", null, 0)
				.tradeArea(TradeArea.EU).vatRegistered(true).goodsOnly().revenue("3108").reporting("35"));
		rules.add(sale(BasicTaxRateProviderSE.KEY_EU_SERVICES, "Försäljning tjänster till annat EU-land", null, 0)
				.tradeArea(TradeArea.EU).vatRegistered(true).servicesOnly().revenue("3308").reporting("39"));
		rules.add(sale(BasicTaxRateProviderSE.KEY_EXPORT_GOODS, "Export varor utanför EU", null, 0)
				.tradeArea(TradeArea.NON_EU).goodsOnly().revenue("3105").reporting("36"));
		rules.add(sale(BasicTaxRateProviderSE.KEY_EXPORT_SERVICES, "Försäljning tjänster utanför EU", null, 0)
				.tradeArea(TradeArea.NON_EU).servicesOnly().revenue("3305").reporting("40"));
		rules.add(sale(BasicTaxRateProviderSE.KEY_SE_REVERSE_CHARGE, "Försäljning omvänd skattskyldighet", null, 0)
				.tradeArea(TradeArea.DOMESTIC).forVatTypes(VAT_TYPE_SE_REVERSED).revenue("3231").reporting("41"));

		// --- Domestic purchases ---
		rules.add(purchase("MP1", "Inköp 25 %", TaxCategory.STANDARD, 25).inputVat("2641").reporting("48"));
		rules.add(purchase("MP2", "Inköp 12 %", TaxCategory.REDUCED_1, 12).inputVat("2641").reporting("48"));
		rules.add(purchase("MP3", "Inköp 6 %", TaxCategory.REDUCED_2, 6).inputVat("2641").reporting("48"));
		rules.add(purchase("MF", "Momsfritt inköp", TaxCategory.EXEMPT, 0));
		rules.add(purchase("IREVD", "Inköp omvänd skattskyldighet", null, 0)
				.tradeArea(TradeArea.DOMESTIC).forVatTypes(VAT_TYPE_SE_REVERSED).reverseCharge(25)
				.reverseChargeOutput("2617").inputVat("2647").expense("4425").reporting("24", "30", "48"));

		// --- EU purchases (reverse charge) ---
		rules.add(euPurchase("IEUV25", "Inköp varor från EU 25 %", TaxCategory.STANDARD, 25, true, "2614", "4515", "20", "30"));
		rules.add(euPurchase("IEUV12", "Inköp varor från EU 12 %", TaxCategory.REDUCED_1, 12, true, "2624", "4516", "20", "31"));
		rules.add(euPurchase("IEUV6", "Inköp varor från EU 6 %", TaxCategory.REDUCED_2, 6, true, "2634", "4517", "20", "32"));
		rules.add(euPurchase("IEUT25", "Inköp tjänster från EU 25 %", TaxCategory.STANDARD, 25, false, "2614", "4535", "21", "30"));
		rules.add(euPurchase("IEUT12", "Inköp tjänster från EU 12 %", TaxCategory.REDUCED_1, 12, false, "2624", "4536", "21", "31"));
		rules.add(euPurchase("IEUT6", "Inköp tjänster från EU 6 %", TaxCategory.REDUCED_2, 6, false, "2634", "4537", "21", "32"));

		// --- Import from outside the EU (reverse charge) ---
		rules.add(purchase("IIMT25", "Import tjänster utanför EU 25 %", TaxCategory.STANDARD, 0)
				.tradeArea(TradeArea.NON_EU).servicesOnly().reverseCharge(25)
				.reverseChargeOutput("2614").inputVat("2645").expense("4531").reporting("22", "30", "48"));
		rules.add(purchase("IIMV25", "Import varor 25 %", TaxCategory.STANDARD, 0)
				.tradeArea(TradeArea.NON_EU).goodsOnly().reverseCharge(25)
				.reverseChargeOutput("2615").inputVat("2645").expense("4545").reporting("50", "60", "48"));

		return rules;
	}

	private static TaxRule sale(String key, String name, String category, double rate) {
		return new TaxRule(key, name, TaxScope.SALES, category, rate);
	}

	private static TaxRule purchase(String key, String name, String category, double rate) {
		return new TaxRule(key, name, TaxScope.PURCHASE, category, rate);
	}

	private static TaxRule euPurchase(String key, String name, String category, double rcRate, boolean goods,
			String outputAcct, String expenseAcct, String baseBox, String vatBox) {
		TaxRule r = purchase(key, name, category, 0).tradeArea(TradeArea.EU).reverseCharge(rcRate)
				.reverseChargeOutput(outputAcct).inputVat("2645").expense(expenseAcct).reporting(baseBox, vatBox, "48");
		return goods ? r.goodsOnly() : r.servicesOnly();
	}

}
