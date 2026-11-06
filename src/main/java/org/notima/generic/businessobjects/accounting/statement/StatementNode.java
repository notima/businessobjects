package org.notima.generic.businessobjects.accounting.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A node of a statement layout (uppställningsform): a section with a heading, lines and
 * an optional sum, a line that accounts are placed on, or a result row.
 *
 * <ul>
 *   <li>{@link Kind#SECTION}: a heading, its children and, if {@code sumLabel} is set, a sum
 *       row of the children.</li>
 *   <li>{@link Kind#LINE}: a line that accounts are placed on by a {@link StatementMapping}.</li>
 *   <li>{@link Kind#RESULT}: the sum of all sections and lines before it in the same list,
 *       e.g. Rörelseresultat or Årets resultat.</li>
 * </ul>
 *
 * <p>Amounts are shown credit positive in a credit normal node (the income statement, equity
 * and liabilities) and debit positive otherwise (assets). A node without a setting of its own
 * inherits it from its parent.
 */
public final class StatementNode {

	public enum Kind { SECTION, LINE, RESULT }

	private final Kind kind;
	private final String id;
	private final String label;
	private String sumLabel;
	private Boolean creditNormal;
	private boolean receivesUnclosedResult;
	private boolean alwaysShown;
	private final List<StatementNode> children;

	private StatementNode(Kind kind, String id, String label, List<StatementNode> children) {
		this.kind = kind;
		this.id = id;
		this.label = label;
		this.children = children;
	}

	/**
	 * @param id		Unique id within the template.
	 * @param label		The heading.
	 * @param sumLabel	The label of the sum row; null for no sum row.
	 * @param children	Sections, lines and results of the section.
	 */
	public static StatementNode section(String id, String label, String sumLabel, StatementNode... children) {
		StatementNode n = new StatementNode(Kind.SECTION, id, label,
				Collections.unmodifiableList(new ArrayList<StatementNode>(Arrays.asList(children))));
		n.sumLabel = sumLabel;
		return n;
	}

	public static StatementNode line(String id, String label) {
		return new StatementNode(Kind.LINE, id, label, Collections.<StatementNode>emptyList());
	}

	public static StatementNode result(String id, String label) {
		return new StatementNode(Kind.RESULT, id, label, Collections.<StatementNode>emptyList());
	}

	/** Sets how amounts of this node and its children are shown. */
	public StatementNode creditNormal(boolean creditNormal) {
		this.creditNormal = creditNormal;
		return this;
	}

	/**
	 * Marks the line that gets the result of the year when it hasn't been booked yet
	 * (Årets resultat in the balance sheet).
	 */
	public StatementNode receivesUnclosedResult() {
		this.receivesUnclosedResult = true;
		return this;
	}

	/** Shows a section even when none of its lines has an amount. */
	public StatementNode alwaysShown() {
		this.alwaysShown = true;
		return this;
	}

	public boolean isAlwaysShown() {
		return alwaysShown;
	}

	public Kind getKind() {
		return kind;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getSumLabel() {
		return sumLabel;
	}

	/** @return	The own setting, or null to inherit the parent's. */
	public Boolean getCreditNormal() {
		return creditNormal;
	}

	public boolean isReceivesUnclosedResult() {
		return receivesUnclosedResult;
	}

	public List<StatementNode> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		return kind + " " + id;
	}

}
