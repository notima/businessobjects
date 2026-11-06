package org.notima.generic.businessobjects.accounting.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.notima.generic.businessobjects.AccountingPeriodData;
import org.notima.generic.businessobjects.accounting.AccountTotals;
import org.notima.generic.businessobjects.accounting.LedgerCalculator;
import org.notima.generic.businessobjects.accounting.LedgerSummary;

/**
 * Calculates the statements of an annual report from the current and the previous period.
 *
 * <p>Each account is placed on a line by a {@link StatementMapping}: balance sheet lines get
 * the account's ending balance, income statement lines its movement of the year. A result of
 * the year that hasn't been booked yet (no closing entry) is added to the line marked with
 * {@link StatementNode#receivesUnclosedResult()}.
 *
 * <p>Amounts are rounded per line to the unit, half to even; sums and results are the sums of
 * the rounded lines. To make the rounded statements agree:
 * <ul>
 *   <li>the rounding line of an income statement ({@link StatementTemplate#roundingLine(String)},
 *       else the line with the largest amount) is adjusted so that the result of the year equals
 *       the exact result rounded;</li>
 *   <li>the last line of the credit side of a balance sheet (equity and liabilities) is
 *       adjusted so that both sides are equal.</li>
 * </ul>
 * Every adjustment is reported as {@link StatementWarning.Code#ROUNDING_ADJUSTED}.
 *
 * <p>The periods' charts of accounts must be classified by the country's rules, as
 * {@link LedgerCalculator} requires.
 */
public final class AnnualStatementCalculator {

	private static final double CENT = 0.005;

	private AnnualStatementCalculator() {}

	/**
	 * @param templates	The statements, e.g. income statement and balance sheet.
	 * @param mapping	Places the accounts on the templates' lines.
	 * @param current	The year of the annual report.
	 * @param previous	The previous year, or null if unknown.
	 * @param unit		The unit to round to, or null to suggest one from total assets.
	 * @return	The calculated statements.
	 */
	public static AnnualStatement calculate(List<StatementTemplate> templates, StatementMapping mapping,
			AccountingPeriodData current, AccountingPeriodData previous, AmountUnit unit) {

		Map<String, StatementTemplate> lineTemplate = new HashMap<String, StatementTemplate>();
		for (StatementTemplate t : templates) {
			for (StatementNode line : t.getLines()) lineTemplate.put(line.getId(), t);
		}

		List<StatementWarning> warnings = new ArrayList<StatementWarning>();
		Column[] columns = new Column[] {
				column(current, 0, mapping, lineTemplate, warnings),
				column(previous, 1, mapping, lineTemplate, warnings) };

		// Exact amounts of every node, per column
		List<Map<String, Double>> exact = new ArrayList<Map<String, Double>>();
		for (Column c : columns) {
			Map<String, Double> values = new HashMap<String, Double>();
			for (StatementTemplate t : templates) evaluate(t, t.getNodes(), c.raw, values, true);
			exact.add(values);
		}

		checkColumns(templates, exact, previous != null, warnings);
		if (previous != null) checkOpeningBalances(columns[0], columns[1], warnings);

		if (unit == null) unit = AmountUnit.suggest(totalAssets(templates, exact.get(0)));

		// Rounded amounts of lines, adjusted so that the statements add up
		List<Map<String, Double>> rounded = new ArrayList<Map<String, Double>>();
		for (int col = 0; col < 2; col++) {
			Map<String, Double> lines = new HashMap<String, Double>();
			for (StatementTemplate t : templates) {
				for (StatementNode line : t.getLines()) {
					Double v = exact.get(col).get(line.getId());
					lines.put(line.getId(), round((v != null ? v : 0.0) / unit.getFactor()));
				}
			}
			for (StatementTemplate t : templates) {
				if (t.getType() == StatementTemplate.Type.INCOME_STATEMENT) {
					adjustResult(t, exact.get(col), lines, unit, col, warnings);
				} else if (t.getCreditSideId() != null) {
					adjustBalance(t, lines, col, warnings);
				}
			}
			Map<String, Double> values = new HashMap<String, Double>(lines);
			for (StatementTemplate t : templates) evaluate(t, t.getNodes(), lines, values, false);
			rounded.add(values);
		}

		AnnualStatement result = new AnnualStatement(
				current != null ? current.getPeriodStart() : null, current != null ? current.getPeriodEnd() : null,
				previous != null ? previous.getPeriodStart() : null, previous != null ? previous.getPeriodEnd() : null,
				previous != null, unit);
		for (StatementTemplate t : templates) {
			List<StatementRow> rows = new ArrayList<StatementRow>();
			flatten(t, t.getNodes(), 0, exact, rounded, columns, rows);
			result.addStatement(t, rows);
		}
		for (StatementWarning w : warnings) result.addWarning(w);
		return result;
	}

	// -------------------------------------------------------------------------
	// Accounts to lines
	// -------------------------------------------------------------------------

	/** The amounts of one year. */
	private static final class Column {
		/** Line id to amount, debit positive. */
		final Map<String, Double> raw = new HashMap<String, Double>();
		/** Line id to its accounts, debit positive. */
		final Map<String, List<AccountAmount>> accounts = new HashMap<String, List<AccountAmount>>();
		/** Balance sheet account to opening and ending balance. */
		final Map<String, Double> openings = new LinkedHashMap<String, Double>();
		final Map<String, Double> endings = new LinkedHashMap<String, Double>();
	}

	private static final class Group {
		final AccountPlacement placement;
		double net;
		final List<AccountAmount> members = new ArrayList<AccountAmount>();
		Group(AccountPlacement placement) {
			this.placement = placement;
		}
	}

	private static Column column(AccountingPeriodData period, int col, StatementMapping mapping,
			Map<String, StatementTemplate> lineTemplate, List<StatementWarning> warnings) {

		Column c = new Column();
		if (period == null) return c;

		LedgerSummary summary = LedgerCalculator.summarize(period, null, null, null);
		Map<String, Double> fileEndings = fileEndingBalances(period);
		Map<String, Group> groups = new LinkedHashMap<String, Group>();
		double unclosed = 0.0;

		for (AccountTotals t : summary.getAccounts()) {
			String no = t.getAccountNo();
			AccountPlacement p = mapping.place(no);
			if (p == null) {
				double amount = t.getBalanceAtTo();
				if (Math.abs(amount) >= CENT) {
					warnings.add(new StatementWarning(StatementWarning.Code.UNMAPPED_ACCOUNT, col, no, null, amount));
				}
				continue;
			}
			if (p.isClosing()) {
				unclosed += t.getNet();
				continue;
			}
			StatementTemplate debitTemplate = lineTemplate.get(p.getDebitLineId());
			StatementTemplate creditTemplate = lineTemplate.get(p.getCreditLineId());
			if (debitTemplate == null || creditTemplate == null) {
				String missing = debitTemplate == null ? p.getDebitLineId() : p.getCreditLineId();
				warnings.add(new StatementWarning(StatementWarning.Code.UNKNOWN_LINE, col, no, missing, t.getBalanceAtTo()));
				continue;
			}
			boolean balance = debitTemplate.getType() == StatementTemplate.Type.BALANCE_SHEET;
			double amount = balance ? t.getBalanceAtTo() : t.getNet();
			if (balance) {
				c.openings.put(no, t.getOpeningBalance());
				c.endings.put(no, amount);
				Double fileEnding = fileEndings.get(no);
				if (fileEnding != null && Math.abs(amount - fileEnding) >= CENT) {
					warnings.add(new StatementWarning(StatementWarning.Code.ENDING_MISMATCH, col, no, null, amount - fileEnding));
				}
			} else {
				unclosed += amount;
			}
			if (amount == 0.0) continue;
			Group g = groups.get(p.getGroup());
			if (g == null) {
				g = new Group(p);
				groups.put(p.getGroup(), g);
			}
			g.net += amount;
			g.members.add(new AccountAmount(no, t.getName(), amount));
		}

		for (Group g : groups.values()) {
			String lineId = g.placement.lineFor(g.net);
			add(c, lineId, g.net, g.members);
		}

		// A result of the year that hasn't been booked goes to the result line of the balance sheet
		if (Math.abs(unclosed) >= CENT) {
			for (StatementTemplate t : distinct(lineTemplate.values())) {
				for (StatementNode line : t.getLines()) {
					if (line.isReceivesUnclosedResult()) {
						add(c, line.getId(), unclosed, Arrays.asList(new AccountAmount(null, "", unclosed)));
					}
				}
			}
		}
		return c;
	}

	/**
	 * @return	The ending balances of the chart of accounts of a period with vouchers, if the
	 * 			file had any; empty otherwise (the balances then come from the ending balances).
	 */
	private static Map<String, Double> fileEndingBalances(AccountingPeriodData period) {
		Map<String, Double> result = new HashMap<String, Double>();
		if (period.getVouchers() == null || period.getVouchers().isEmpty() || period.getChartOfAccounts() == null) {
			return result;
		}
		boolean any = false;
		for (org.notima.generic.businessobjects.AccountElement ae : period.getChartOfAccounts()) {
			if (ae.getAccountNo() == null) continue;
			if (ae.getAccountElementType() == org.notima.generic.businessobjects.AccountElement.AccountElementType.PLAcct) continue;
			result.put(ae.getAccountNo(), ae.getEndingBalance());
			if (ae.getEndingBalance() != 0.0) any = true;
		}
		if (!any) result.clear();
		return result;
	}

	private static void add(Column c, String lineId, double amount, List<AccountAmount> members) {
		Double v = c.raw.get(lineId);
		c.raw.put(lineId, (v != null ? v : 0.0) + amount);
		List<AccountAmount> list = c.accounts.get(lineId);
		if (list == null) {
			list = new ArrayList<AccountAmount>();
			c.accounts.put(lineId, list);
		}
		list.addAll(members);
	}

	private static List<StatementTemplate> distinct(java.util.Collection<StatementTemplate> templates) {
		List<StatementTemplate> result = new ArrayList<StatementTemplate>();
		for (StatementTemplate t : templates) {
			if (!result.contains(t)) result.add(t);
		}
		return result;
	}

	// -------------------------------------------------------------------------
	// Evaluation
	// -------------------------------------------------------------------------

	/**
	 * Puts the amount of every section and result of {@code nodes} in {@code values}.
	 *
	 * @param lines		Line amounts: debit positive raw amounts if {@code signed}, else amounts
	 * 					that already have the line's sign.
	 * @return	The sum of the sections and lines of the list.
	 */
	private static double evaluate(StatementTemplate t, List<StatementNode> nodes, Map<String, Double> lines,
			Map<String, Double> values, boolean signed) {
		double running = 0.0;
		for (StatementNode n : nodes) {
			switch (n.getKind()) {
				case RESULT:
					values.put(n.getId(), running);
					break;
				case SECTION:
					double sum = evaluate(t, n.getChildren(), lines, values, signed);
					values.put(n.getId(), sum);
					running += sum;
					break;
				case LINE:
					Double v = lines.get(n.getId());
					double amount = v != null ? v : 0.0;
					if (signed && t.isCreditNormal(n.getId())) amount = -amount;
					values.put(n.getId(), amount);
					running += amount;
					break;
			}
		}
		return running;
	}

	private static double value(Map<String, Double> values, String id) {
		Double v = values.get(id);
		return v != null ? v : 0.0;
	}

	private static StatementNode finalResult(StatementTemplate t) {
		List<StatementNode> nodes = t.getNodes();
		for (int i = nodes.size() - 1; i >= 0; i--) {
			if (nodes.get(i).getKind() == StatementNode.Kind.RESULT) return nodes.get(i);
		}
		return null;
	}

	private static StatementNode unclosedResultLine(List<StatementTemplate> templates) {
		for (StatementTemplate t : templates) {
			for (StatementNode line : t.getLines()) {
				if (line.isReceivesUnclosedResult()) return line;
			}
		}
		return null;
	}

	private static double totalAssets(List<StatementTemplate> templates, Map<String, Double> values) {
		for (StatementTemplate t : templates) {
			if (t.getDebitSideId() != null) return value(values, t.getDebitSideId());
		}
		return 0.0;
	}

	// -------------------------------------------------------------------------
	// Checks
	// -------------------------------------------------------------------------

	private static void checkColumns(List<StatementTemplate> templates, List<Map<String, Double>> exact,
			boolean hasPrevious, List<StatementWarning> warnings) {
		StatementNode resultLine = unclosedResultLine(templates);
		for (int col = 0; col < (hasPrevious ? 2 : 1); col++) {
			Map<String, Double> values = exact.get(col);
			for (StatementTemplate t : templates) {
				if (t.getDebitSideId() != null && t.getCreditSideId() != null) {
					double diff = value(values, t.getDebitSideId()) - value(values, t.getCreditSideId());
					if (Math.abs(diff) >= CENT) {
						warnings.add(new StatementWarning(StatementWarning.Code.BALANCE_MISMATCH, col, null, null, diff));
					}
				}
				if (t.getType() == StatementTemplate.Type.INCOME_STATEMENT && resultLine != null) {
					StatementNode result = finalResult(t);
					if (result == null) continue;
					double diff = value(values, result.getId()) - value(values, resultLine.getId());
					if (Math.abs(diff) >= CENT) {
						warnings.add(new StatementWarning(StatementWarning.Code.RESULT_MISMATCH, col, null,
								resultLine.getId(), diff));
					}
				}
			}
		}
	}

	private static void checkOpeningBalances(Column current, Column previous, List<StatementWarning> warnings) {
		for (Map.Entry<String, Double> e : current.openings.entrySet()) {
			Double ending = previous.endings.get(e.getKey());
			double diff = e.getValue() - (ending != null ? ending : 0.0);
			if (Math.abs(diff) >= CENT) {
				warnings.add(new StatementWarning(StatementWarning.Code.OPENING_MISMATCH, 0, e.getKey(), null, diff));
			}
		}
		for (Map.Entry<String, Double> e : previous.endings.entrySet()) {
			if (current.openings.containsKey(e.getKey()) || Math.abs(e.getValue()) < CENT) continue;
			warnings.add(new StatementWarning(StatementWarning.Code.OPENING_MISMATCH, 0, e.getKey(), null, -e.getValue()));
		}
	}

	// -------------------------------------------------------------------------
	// Rounding
	// -------------------------------------------------------------------------

	private static void adjustResult(StatementTemplate t, Map<String, Double> exact, Map<String, Double> lines,
			AmountUnit unit, int col, List<StatementWarning> warnings) {
		StatementNode result = finalResult(t);
		if (result == null) return;
		Map<String, Double> values = new HashMap<String, Double>(lines);
		evaluate(t, t.getNodes(), lines, values, false);
		double diff = round(value(exact, result.getId()) / unit.getFactor()) - value(values, result.getId());
		if (diff == 0.0) return;
		StatementNode largest = t.getRoundingLineId() != null ? t.getNode(t.getRoundingLineId()) : null;
		if (largest == null) {
			for (StatementNode line : t.getLines()) {
				if (largest == null || Math.abs(value(lines, line.getId())) > Math.abs(value(lines, largest.getId()))) {
					largest = line;
				}
			}
		}
		if (largest == null) return;
		lines.put(largest.getId(), value(lines, largest.getId()) + diff);
		warnings.add(new StatementWarning(StatementWarning.Code.ROUNDING_ADJUSTED, col, null, largest.getId(), diff));
	}

	private static void adjustBalance(StatementTemplate t, Map<String, Double> lines, int col,
			List<StatementWarning> warnings) {
		Map<String, Double> values = new HashMap<String, Double>(lines);
		evaluate(t, t.getNodes(), lines, values, false);
		double diff = value(values, t.getDebitSideId()) - value(values, t.getCreditSideId());
		if (diff == 0.0) return;
		StatementNode side = t.getNode(t.getCreditSideId());
		StatementNode last = lastAdjustableLine(side, lines, null);
		if (last == null) return;
		lines.put(last.getId(), value(lines, last.getId()) + diff);
		warnings.add(new StatementWarning(StatementWarning.Code.ROUNDING_ADJUSTED, col, null, last.getId(), diff));
	}

	/**
	 * Rounds half to even. The amount is first rounded to whole cents, so that a sum like
	 * 472.4999999 rounds as 472.50.
	 */
	private static double round(double amount) {
		return Math.rint(Math.rint(amount * 100.0) / 100.0);
	}

	private static StatementNode lastAdjustableLine(StatementNode node, Map<String, Double> lines, StatementNode last) {
		if (node.getKind() == StatementNode.Kind.LINE) {
			if (!node.isReceivesUnclosedResult() && value(lines, node.getId()) != 0.0) return node;
			return last;
		}
		for (StatementNode c : node.getChildren()) last = lastAdjustableLine(c, lines, last);
		return last;
	}

	// -------------------------------------------------------------------------
	// Rows
	// -------------------------------------------------------------------------

	/** @return	True if any line in {@code nodes} has an amount. */
	private static boolean flatten(StatementTemplate t, List<StatementNode> nodes, int depth, List<Map<String, Double>> exact,
			List<Map<String, Double>> rounded, Column[] columns, List<StatementRow> rows) {
		boolean any = false;
		for (StatementNode n : nodes) {
			switch (n.getKind()) {
				case SECTION: {
					int headingIndex = rows.size();
					rows.add(null);
					boolean visible = flatten(t, n.getChildren(), depth + 1, exact, rounded, columns, rows);
					boolean shown = visible || depth == 0;
					rows.set(headingIndex, new StatementRow(n, StatementRow.Kind.HEADING, depth,
							new double[2], new long[2], null, shown));
					if (n.getSumLabel() != null) {
						rows.add(new StatementRow(n, StatementRow.Kind.SUM, depth,
								amounts(exact, n.getId()), longs(rounded, n.getId()), null, shown));
					}
					any |= visible;
					break;
				}
				case LINE: {
					long[] r = longs(rounded, n.getId());
					boolean visible = r[0] != 0 || r[1] != 0;
					List<List<AccountAmount>> accounts = new ArrayList<List<AccountAmount>>();
					for (Column c : columns) accounts.add(signed(c.accounts.get(n.getId()), t.isCreditNormal(n.getId())));
					rows.add(new StatementRow(n, StatementRow.Kind.LINE, depth, amounts(exact, n.getId()), r,
							accounts, visible));
					any |= visible;
					break;
				}
				case RESULT:
					rows.add(new StatementRow(n, StatementRow.Kind.RESULT, depth, amounts(exact, n.getId()),
							longs(rounded, n.getId()), null, true));
					break;
			}
		}
		return any;
	}

	/** The line's accounts with the line's sign. */
	private static List<AccountAmount> signed(List<AccountAmount> raw, boolean creditNormal) {
		List<AccountAmount> result = new ArrayList<AccountAmount>();
		if (raw == null) return result;
		double sign = creditNormal ? -1.0 : 1.0;
		for (AccountAmount a : raw) result.add(new AccountAmount(a.getAccountNo(), a.getName(), sign * a.getAmount()));
		return result;
	}

	private static double[] amounts(List<Map<String, Double>> values, String id) {
		return new double[] { value(values.get(0), id), value(values.get(1), id) };
	}

	private static long[] longs(List<Map<String, Double>> values, String id) {
		return new long[] { Math.round(value(values.get(0), id)), Math.round(value(values.get(1), id)) };
	}

}
