package org.notima.generic.businessobjects.tax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.notima.generic.businessobjects.AccountingType;

/**
 * A {@link TaxEngine} that chooses among a list of {@link TaxRule rules}, as iDempiere does
 * among its taxes.
 *
 * <p>A rule applies if it's valid on the date, its scope covers the trade (sales/purchase)
 * and all its conditions hold (see {@link TaxRule}). An unknown circumstance (null in the
 * {@link TaxContext}) doesn't rule anything out. When the tax category isn't known, the rules
 * with the {@link TaxContext#getRate() rate charged} are taken.</p>
 *
 * <p>Among the rules that apply, the most specific wins: a condition on the counterparty's
 * country counts most, then on its VAT treatment, the trade area, its VAT registration, the
 * tax category, and goods or services. If several rules are equally specific, none is chosen
 * and the determination says so.</p>
 *
 * <p>A rule chosen explicitly by its {@link TaxContext#getTaxKey() key} applies regardless of
 * its conditions, if it's valid and of the right scope.</p>
 */
public class RuleBasedTaxEngine implements TaxEngine {

	private final List<TaxRule> rules;

	public RuleBasedTaxEngine(Collection<TaxRule> rules) {
		this.rules = rules != null ? new ArrayList<TaxRule>(rules) : new ArrayList<TaxRule>();
	}

	public List<TaxRule> getRules() {
		return Collections.unmodifiableList(rules);
	}

	@Override
	public TaxDetermination determine(TaxContext ctx) {
		List<TaxRule> valid = new ArrayList<TaxRule>();
		for (TaxRule r : rules) {
			if (r.isValidOn(ctx.getDate()) && r.getScope().appliesTo(ctx.isSales())) valid.add(r);
		}

		if (ctx.getTaxKey() != null) {
			for (TaxRule r : valid) {
				if (ctx.getTaxKey().equals(r.getKey())) return TaxDetermination.of(r);
			}
		}

		TradeArea area = ctx.getTradeArea();
		List<TaxRule> applying = new ArrayList<TaxRule>();
		for (TaxRule r : valid) {
			if (applies(r, ctx, area)) applying.add(r);
		}
		if (applying.isEmpty()) {
			return TaxDetermination.none("No tax rule applies to a " + (ctx.isSales() ? "sale" : "purchase")
					+ (ctx.getCategoryKey() != null ? " of category " + ctx.getCategoryKey() : "")
					+ (ctx.getCategoryKey() == null && ctx.getRate() != null ? " at " + rateText(ctx.getRate()) + " %" : "")
					+ " (" + area + (ctx.getCounterpartyCountry() != null ? ", " + ctx.getCounterpartyCountry() : "") + ")");
		}

		int best = Integer.MIN_VALUE;
		List<TaxRule> top = new ArrayList<TaxRule>();
		for (TaxRule r : applying) {
			int score = specificity(r);
			if (score > best) {
				best = score;
				top.clear();
				top.add(r);
			} else if (score == best) {
				top.add(r);
			}
		}
		if (top.size() == 1) return TaxDetermination.of(top.get(0));

		StringBuilder keys = new StringBuilder();
		for (TaxRule r : top) {
			if (keys.length() > 0) keys.append(", ");
			keys.append(r.getKey());
		}
		String hint = ctx.getService() == null ? " Say whether it's goods or services." : "";
		return TaxDetermination.ambiguous(top, "Several tax rules apply equally: " + keys + "." + hint);
	}

	private static boolean applies(TaxRule r, TaxContext ctx, TradeArea area) {
		if (r.getCategoryKey() != null) {
			if (ctx.getCategoryKey() != null) {
				if (!r.getCategoryKey().equals(ctx.getCategoryKey())) return false;
			} else if (ctx.getRate() != null && !sameRate(r.getRate(), ctx.getRate())) {
				return false;
			}
		} else if (ctx.getCategoryKey() == null && ctx.getRate() != null && !sameRate(r.getRate(), ctx.getRate())) {
			return false;
		}
		if (!r.getTradeArea().matches(area)) return false;
		if (r.getCounterpartyCountry() != null
				&& !r.getCounterpartyCountry().equalsIgnoreCase(String.valueOf(ctx.getCounterpartyCountry()))) return false;
		if (Boolean.TRUE.equals(ctx.getService()) && !r.isAppliesToServices()) return false;
		if (Boolean.FALSE.equals(ctx.getService()) && !r.isAppliesToGoods()) return false;
		if (r.getVatRegisteredCounterparty() != null && ctx.getCounterpartyVatRegistered() != null
				&& !r.getVatRegisteredCounterparty().equals(ctx.getCounterpartyVatRegistered())) return false;
		if (!r.getCounterpartyVatTypes().isEmpty()
				&& (ctx.getCounterpartyVatType() == null || !r.getCounterpartyVatTypes().contains(ctx.getCounterpartyVatType()))) return false;
		return true;
	}

	/** How specific a rule's conditions are; the most specific applying rule wins. */
	static int specificity(TaxRule r) {
		int score = 0;
		if (r.getCounterpartyCountry() != null) score += 32;
		if (!r.getCounterpartyVatTypes().isEmpty()) score += 16;
		if (r.getTradeArea() != TradeArea.ANY) score += 8;
		if (r.getVatRegisteredCounterparty() != null) score += 4;
		if (r.getCategoryKey() != null) score += 2;
		if (r.isAppliesToGoods() != r.isAppliesToServices()) score += 1;
		return score;
	}

	@Override
	public List<TaxLine> taxLines(TaxRule rule, BigDecimal net, BigDecimal vat, boolean sales) {
		List<TaxLine> lines = new ArrayList<TaxLine>();
		if (rule == null || net == null) return lines;
		if (rule.isReverseCharge()) {
			// The seller charges no VAT; the buyer accounts for it, as output and input VAT
			if (sales) return lines;
			BigDecimal rc = percent(net, rule.getReverseChargeRate());
			if (rc.signum() == 0) return lines;
			lines.add(new TaxLine(AccountingType.LIABILITY_VAT, rule.getReverseChargeOutputAccount(), rc.negate(), rule));
			lines.add(new TaxLine(AccountingType.CLAIM_VAT, rule.getInputVatAccount(), rc, rule));
			return lines;
		}
		BigDecimal tax = vat != null ? vat : percent(net, rule.getRate());
		if (tax.signum() == 0) return lines;
		if (sales) {
			lines.add(new TaxLine(AccountingType.LIABILITY_VAT, rule.getOutputVatAccount(), tax.negate(), rule));
		} else {
			lines.add(new TaxLine(AccountingType.CLAIM_VAT, rule.getInputVatAccount(), tax, rule));
		}
		return lines;
	}

	private static BigDecimal percent(BigDecimal amount, double rate) {
		return amount.multiply(BigDecimal.valueOf(rate)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
	}

	private static boolean sameRate(double a, double b) {
		return BigDecimal.valueOf(a).compareTo(BigDecimal.valueOf(b)) == 0;
	}

	private static String rateText(double rate) {
		return BigDecimal.valueOf(rate).stripTrailingZeros().toPlainString();
	}

}
