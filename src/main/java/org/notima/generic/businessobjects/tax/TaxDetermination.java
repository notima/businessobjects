package org.notima.generic.businessobjects.tax;

import java.util.Collections;
import java.util.List;

/**
 * The {@link TaxRule} the {@link TaxEngine} chose for a {@link TaxContext}, or why it couldn't
 * choose one: no rule applies, or several apply equally (ie because it isn't known whether
 * the line is goods or services).
 */
public class TaxDetermination {

	private final TaxRule		rule;
	private final List<TaxRule>	candidates;
	private final String		problem;

	private TaxDetermination(TaxRule rule, List<TaxRule> candidates, String problem) {
		this.rule = rule;
		this.candidates = candidates != null ? candidates : Collections.<TaxRule>emptyList();
		this.problem = problem;
	}

	public static TaxDetermination of(TaxRule rule) {
		return new TaxDetermination(rule, Collections.singletonList(rule), null);
	}

	public static TaxDetermination none(String problem) {
		return new TaxDetermination(null, null, problem);
	}

	public static TaxDetermination ambiguous(List<TaxRule> candidates, String problem) {
		return new TaxDetermination(null, candidates, problem);
	}

	public boolean isDetermined() { return rule != null; }

	/** @return	The chosen rule, or null. */
	public TaxRule getRule() { return rule; }

	/** @return	The rules that applied equally when none could be chosen; the chosen one otherwise. */
	public List<TaxRule> getCandidates() { return candidates; }

	/** @return	Why no rule was chosen, in English; null if one was. */
	public String getProblem() { return problem; }

}
