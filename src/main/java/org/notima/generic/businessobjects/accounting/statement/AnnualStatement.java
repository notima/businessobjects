package org.notima.generic.businessobjects.accounting.statement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The calculated statements of an annual report (income statement and balance sheet) for
 * the current and the previous year, with the warnings found while calculating them.
 */
public final class AnnualStatement {

	private final LocalDate[] periodStart = new LocalDate[2];
	private final LocalDate[] periodEnd = new LocalDate[2];
	private final boolean hasPrevious;
	private final AmountUnit unit;
	private final Map<String, List<StatementRow>> rowsByTemplate = new LinkedHashMap<String, List<StatementRow>>();
	private final Map<String, StatementTemplate> templates = new LinkedHashMap<String, StatementTemplate>();
	private final List<StatementWarning> warnings = new ArrayList<StatementWarning>();

	AnnualStatement(LocalDate currentStart, LocalDate currentEnd, LocalDate previousStart, LocalDate previousEnd,
			boolean hasPrevious, AmountUnit unit) {
		periodStart[0] = currentStart;
		periodEnd[0] = currentEnd;
		periodStart[1] = previousStart;
		periodEnd[1] = previousEnd;
		this.hasPrevious = hasPrevious;
		this.unit = unit;
	}

	void addStatement(StatementTemplate template, List<StatementRow> rows) {
		templates.put(template.getId(), template);
		rowsByTemplate.put(template.getId(), Collections.unmodifiableList(rows));
	}

	void addWarning(StatementWarning w) {
		warnings.add(w);
	}

	public LocalDate getPeriodStart(int column) {
		return periodStart[column];
	}

	public LocalDate getPeriodEnd(int column) {
		return periodEnd[column];
	}

	/** @return	True if the previous year's figures are known. */
	public boolean hasPrevious() {
		return hasPrevious;
	}

	public AmountUnit getUnit() {
		return unit;
	}

	public List<StatementTemplate> getTemplates() {
		return new ArrayList<StatementTemplate>(templates.values());
	}

	/** @return	All rows of a template, visible or not. */
	public List<StatementRow> getRows(String templateId) {
		List<StatementRow> rows = rowsByTemplate.get(templateId);
		return rows != null ? rows : Collections.<StatementRow>emptyList();
	}

	/** @return	The rows to show. */
	public List<StatementRow> getVisibleRows(String templateId) {
		List<StatementRow> result = new ArrayList<StatementRow>();
		for (StatementRow r : getRows(templateId)) {
			if (r.isVisible()) result.add(r);
		}
		return result;
	}

	/**
	 * @param nodeId	A line, result or section id.
	 * @return	The row of a line or result, or the sum row of a section; null if none.
	 */
	public StatementRow getRow(String nodeId) {
		StatementRow heading = null;
		for (List<StatementRow> rows : rowsByTemplate.values()) {
			for (StatementRow r : rows) {
				if (!r.getId().equals(nodeId)) continue;
				if (r.getKind() != StatementRow.Kind.HEADING) return r;
				heading = r;
			}
		}
		return heading;
	}

	public List<StatementWarning> getWarnings() {
		return Collections.unmodifiableList(warnings);
	}

	/** @return	True if there is a warning of severity ERROR. */
	public boolean hasErrors() {
		for (StatementWarning w : warnings) {
			if (w.getSeverity() == StatementWarning.Severity.ERROR) return true;
		}
		return false;
	}

}
