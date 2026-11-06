package org.notima.generic.businessobjects.accounting.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A statement layout (uppställningsform), e.g. the income statement or the balance sheet
 * of a K2 annual report.
 */
public final class StatementTemplate {

	public enum Type { INCOME_STATEMENT, BALANCE_SHEET }

	private final String id;
	private final String name;
	private final Type type;
	private final boolean creditNormal;
	private final List<StatementNode> nodes;
	private final Map<String, StatementNode> byId = new LinkedHashMap<String, StatementNode>();
	private final Map<String, Boolean> creditNormalById = new LinkedHashMap<String, Boolean>();
	private String debitSideId;
	private String creditSideId;
	private String roundingLineId;

	/**
	 * @param id			Unique id, e.g. "SE-K2-RR".
	 * @param name			Heading, e.g. "Resultaträkning".
	 * @param type			Income statement or balance sheet.
	 * @param creditNormal	Default for nodes without a setting of their own.
	 * @param nodes			The top level nodes.
	 */
	public StatementTemplate(String id, String name, Type type, boolean creditNormal, StatementNode... nodes) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.creditNormal = creditNormal;
		this.nodes = Collections.unmodifiableList(new ArrayList<StatementNode>(Arrays.asList(nodes)));
		for (StatementNode n : this.nodes) index(n, creditNormal);
	}

	private void index(StatementNode n, boolean inherited) {
		if (byId.containsKey(n.getId())) {
			throw new IllegalArgumentException("Duplicate node id " + n.getId() + " in " + id);
		}
		boolean cn = n.getCreditNormal() != null ? n.getCreditNormal() : inherited;
		byId.put(n.getId(), n);
		creditNormalById.put(n.getId(), cn);
		for (StatementNode c : n.getChildren()) index(c, cn);
	}

	/**
	 * Declares the two sides of a balance sheet, which must be equal. Rounding differences
	 * are put on the credit side (equity and liabilities).
	 */
	public StatementTemplate sides(String debitSideId, String creditSideId) {
		this.debitSideId = debitSideId;
		this.creditSideId = creditSideId;
		return this;
	}

	/**
	 * Declares the line of an income statement that takes the rounding difference of the
	 * result. Without one, the line with the largest amount takes it.
	 */
	public StatementTemplate roundingLine(String lineId) {
		this.roundingLineId = lineId;
		return this;
	}

	public String getRoundingLineId() {
		return roundingLineId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public boolean isCreditNormal() {
		return creditNormal;
	}

	public List<StatementNode> getNodes() {
		return nodes;
	}

	public StatementNode getNode(String nodeId) {
		return byId.get(nodeId);
	}

	public boolean hasNode(String nodeId) {
		return byId.containsKey(nodeId);
	}

	/** @return	True if the node's amounts are shown credit positive. */
	public boolean isCreditNormal(String nodeId) {
		Boolean cn = creditNormalById.get(nodeId);
		return cn != null ? cn : creditNormal;
	}

	public String getDebitSideId() {
		return debitSideId;
	}

	public String getCreditSideId() {
		return creditSideId;
	}

	/** @return	All lines (accounts are placed on lines), in layout order. */
	public List<StatementNode> getLines() {
		List<StatementNode> result = new ArrayList<StatementNode>();
		for (StatementNode n : byId.values()) {
			if (n.getKind() == StatementNode.Kind.LINE) result.add(n);
		}
		return result;
	}

}
