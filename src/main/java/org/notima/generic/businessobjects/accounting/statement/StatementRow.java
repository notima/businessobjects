package org.notima.generic.businessobjects.accounting.statement;

import java.util.Collections;
import java.util.List;

/**
 * A row of a calculated statement: a heading, a line, the sum of a section or a result.
 * Index 0 of the amounts is the current year and index 1 the previous year.
 */
public final class StatementRow {

	public enum Kind { HEADING, LINE, SUM, RESULT }

	private final StatementNode node;
	private final Kind kind;
	private final int depth;
	private final double[] exact;
	private final long[] rounded;
	private final List<List<AccountAmount>> accounts;
	private final boolean visible;

	StatementRow(StatementNode node, Kind kind, int depth, double[] exact, long[] rounded,
			List<List<AccountAmount>> accounts, boolean visible) {
		this.node = node;
		this.kind = kind;
		this.depth = depth;
		this.exact = exact;
		this.rounded = rounded;
		this.accounts = accounts;
		this.visible = visible;
	}

	public StatementNode getNode() {
		return node;
	}

	public String getId() {
		return node.getId();
	}

	public Kind getKind() {
		return kind;
	}

	/** @return	The label of the row: the heading, the line, the sum label or the result. */
	public String getLabel() {
		return kind == Kind.SUM ? node.getSumLabel() : node.getLabel();
	}

	/** @return	The nesting level, 0 for the top level. */
	public int getDepth() {
		return depth;
	}

	/** @return	The exact amount in kr with the row's sign; 0 for a heading. */
	public double getExact(int column) {
		return exact[column];
	}

	/** @return	The amount in the statement's unit, rounded so that the statements add up. */
	public long getRounded(int column) {
		return rounded[column];
	}

	/** @return	The accounts of a line; empty for other rows. */
	public List<AccountAmount> getAccounts(int column) {
		return accounts != null ? accounts.get(column) : Collections.<AccountAmount>emptyList();
	}

	/** @return	False for lines and sections without amounts in either year. */
	public boolean isVisible() {
		return visible;
	}

	@Override
	public String toString() {
		return kind + " " + getLabel() + " " + rounded[0] + " " + rounded[1];
	}

}
