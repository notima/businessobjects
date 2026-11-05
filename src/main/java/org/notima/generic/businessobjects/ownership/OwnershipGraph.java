package org.notima.generic.businessobjects.ownership;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Who owns whom: the parties of a set of {@link OwnershipStake}s and the stakes between
 * them.
 *
 * <p>Parties that {@link EntityRef#matches(EntityRef) match} are merged into one node, so a
 * company that is an owner in one share register and the company of another register is
 * the same node. The nodes are copies; the stakes the graph returns refer to them.</p>
 *
 * <p>A company's owners should come from one source (its share register). If stakes in the
 * same company arrive from more than one source, the first source is used and the others
 * are listed by {@link #getConflictingSources()}.</p>
 *
 * <p>Cross ownership (cycles) is allowed; the calculations follow each path until it
 * would return to a party already on it.</p>
 */
public class OwnershipGraph {

	private static final BigDecimal HALF = new BigDecimal("0.5");

	private final List<EntityRef>	nodes = new ArrayList<>();
	private final Map<EntityRef, List<OwnershipStake>>	owners = new IdentityHashMap<>();
	private final Map<EntityRef, List<OwnershipStake>>	holdings = new IdentityHashMap<>();
	private final Map<EntityRef, String>	companySource = new IdentityHashMap<>();
	private final List<String>		conflictingSources = new ArrayList<>();

	public OwnershipGraph() {}

	public static OwnershipGraph of(Collection<OwnershipStake> stakes) {
		OwnershipGraph g = new OwnershipGraph();
		g.addAll(stakes);
		return g;
	}

	public void addAll(Collection<OwnershipStake> stakes) {
		if (stakes == null) return;
		for (OwnershipStake s : stakes) add(s);
	}

	/**
	 * Adds a stake. The stake is copied, with owner and company replaced by the graph's
	 * nodes.
	 *
	 * @return	The added copy, or null if the stake was ignored (no owner or company, or
	 * 			the company's owners come from another source).
	 */
	public OwnershipStake add(OwnershipStake stake) {
		if (stake == null || stake.getOwner() == null || stake.getCompany() == null) return null;
		EntityRef company = node(stake.getCompany());
		String source = stake.getSource() != null ? stake.getSource() : "";
		String known = companySource.putIfAbsent(company, source);
		if (known != null && !known.equals(source)) {
			if (!conflictingSources.contains(source)) conflictingSources.add(source);
			return null;
		}
		EntityRef owner = node(stake.getOwner());

		OwnershipStake s = new OwnershipStake(owner, company, stake.getAsOf(), stake.getShares(), stake.getTotalShares());
		s.setVotes(stake.getVotes());
		s.setTotalVotes(stake.getTotalVotes());
		s.setClassHoldings(stake.getClassHoldings());
		s.setSource(stake.getSource());
		owners.computeIfAbsent(company, k -> new ArrayList<>()).add(s);
		holdings.computeIfAbsent(owner, k -> new ArrayList<>()).add(s);
		return s;
	}

	/** Adds a party without stakes, e.g. a company whose share register has no owners yet. */
	public EntityRef addEntity(EntityRef ref) {
		return ref != null ? node(ref) : null;
	}

	/** The node matching {@code ref}, adding a copy of it if there is none. */
	private EntityRef node(EntityRef ref) {
		EntityRef n = find(ref);
		if (n != null) {
			n.mergeFrom(ref);
			return n;
		}
		n = ref.copy();
		nodes.add(n);
		return n;
	}

	/** @return	The node that {@link EntityRef#matches(EntityRef) matches} {@code ref}, or null. */
	public EntityRef find(EntityRef ref) {
		if (ref == null) return null;
		for (EntityRef n : nodes) {
			if (n == ref) return n;
		}
		for (EntityRef n : nodes) {
			if (n.matches(ref)) return n;
		}
		return null;
	}

	public List<EntityRef> getNodes() {
		return Collections.unmodifiableList(nodes);
	}

	/** @return	The sources whose stakes were ignored because another source covered the company. */
	public List<String> getConflictingSources() {
		return Collections.unmodifiableList(conflictingSources);
	}

	/** @return	The direct owners of {@code company}. */
	public List<OwnershipStake> getOwners(EntityRef company) {
		EntityRef n = find(company);
		List<OwnershipStake> l = n != null ? owners.get(n) : null;
		return l != null ? Collections.unmodifiableList(l) : Collections.emptyList();
	}

	/** @return	The direct holdings of {@code owner}. */
	public List<OwnershipStake> getHoldings(EntityRef owner) {
		EntityRef n = find(owner);
		List<OwnershipStake> l = n != null ? holdings.get(n) : null;
		return l != null ? Collections.unmodifiableList(l) : Collections.emptyList();
	}

	/** @return	True if the owners of {@code company} are known, i.e. its share register is in the graph. */
	public boolean hasOwners(EntityRef company) {
		return !getOwners(company).isEmpty();
	}

	/**
	 * The companies not owned by another company in the graph: the tops of the group
	 * structures. They may be owned by persons. Persons are not included.
	 */
	public List<EntityRef> getTopCompanies() {
		List<EntityRef> result = new ArrayList<>();
		for (EntityRef n : nodes) {
			if (n.isPerson()) continue;
			List<OwnershipStake> o = owners.get(n);
			boolean ownedByOther = false;
			if (o != null) {
				for (OwnershipStake s : o) {
					if (s.getOwner() != n && !s.getOwner().isPerson()) { ownedByOther = true; break; }
				}
			}
			if (!ownedByOther) result.add(n);
		}
		return result;
	}

	/**
	 * The share {@code owner} has of {@code company}, directly and through other companies:
	 * the sum over every path from owner to company of the product of the fractions along
	 * it.
	 */
	public OwnershipShare getIndirectShare(EntityRef owner, EntityRef company) {
		EntityRef from = find(owner);
		EntityRef to = find(company);
		if (from == null || to == null) return OwnershipShare.NONE;
		if (from == to) return OwnershipShare.ALL;
		return indirect(from, to, OwnershipShare.ALL, Collections.newSetFromMap(new IdentityHashMap<>()));
	}

	private OwnershipShare indirect(EntityRef from, EntityRef to, OwnershipShare acc, Set<EntityRef> path) {
		path.add(from);
		OwnershipShare sum = OwnershipShare.NONE;
		for (OwnershipStake s : holdings.getOrDefault(from, Collections.emptyList())) {
			EntityRef next = s.getCompany();
			if (path.contains(next)) continue;
			OwnershipShare share = acc.times(OwnershipShare.of(s));
			if (next == to) sum = sum.plus(share);
			else sum = sum.plus(indirect(next, to, share, path));
		}
		path.remove(from);
		return sum;
	}

	/**
	 * The ultimate owners of {@code company}: the parties without owners of their own in
	 * the graph (persons, and companies whose share registers are not included) reached by
	 * following the owners upwards, each with its indirect share.
	 */
	public Map<EntityRef, OwnershipShare> getUltimateOwners(EntityRef company) {
		Map<EntityRef, OwnershipShare> result = new LinkedHashMap<>();
		EntityRef n = find(company);
		if (n != null) ultimate(n, OwnershipShare.ALL, Collections.newSetFromMap(new IdentityHashMap<>()), result);
		return result;
	}

	private void ultimate(EntityRef company, OwnershipShare acc, Set<EntityRef> path,
			Map<EntityRef, OwnershipShare> result) {
		path.add(company);
		for (OwnershipStake s : owners.getOrDefault(company, Collections.emptyList())) {
			EntityRef owner = s.getOwner();
			if (path.contains(owner)) continue;
			OwnershipShare share = acc.times(OwnershipShare.of(s));
			if (owners.getOrDefault(owner, Collections.emptyList()).isEmpty()) {
				result.merge(owner, share, OwnershipShare::plus);
			} else {
				ultimate(owner, share, path, result);
			}
		}
		path.remove(company);
	}

	/** The tree of owners of {@code company}, upwards. Null if the company is not in the graph. */
	public OwnershipTreeNode getOwnerTree(EntityRef company) {
		EntityRef n = find(company);
		if (n == null) return null;
		OwnershipTreeNode root = new OwnershipTreeNode(n, null, OwnershipShare.ALL, false);
		tree(root, true, Collections.newSetFromMap(new IdentityHashMap<>()));
		return root;
	}

	/** The tree of the holdings of {@code owner}, downwards. Null if the owner is not in the graph. */
	public OwnershipTreeNode getHoldingTree(EntityRef owner) {
		EntityRef n = find(owner);
		if (n == null) return null;
		OwnershipTreeNode root = new OwnershipTreeNode(n, null, OwnershipShare.ALL, false);
		tree(root, false, Collections.newSetFromMap(new IdentityHashMap<>()));
		return root;
	}

	private void tree(OwnershipTreeNode node, boolean upwards, Set<EntityRef> path) {
		EntityRef e = node.getEntity();
		path.add(e);
		List<OwnershipStake> edges = (upwards ? owners : holdings).getOrDefault(e, Collections.emptyList());
		for (OwnershipStake s : edges) {
			EntityRef next = upwards ? s.getOwner() : s.getCompany();
			boolean cycle = path.contains(next);
			OwnershipTreeNode child = new OwnershipTreeNode(next, s,
					node.getShare().times(OwnershipShare.of(s)), cycle);
			node.getChildren().add(child);
			if (!cycle) tree(child, upwards, path);
		}
		path.remove(e);
	}

	/**
	 * The subsidiaries (dotterföretag) of {@code parent}: the companies where the parent,
	 * alone or together with its other subsidiaries, holds more than half of the votes
	 * (aktiebolagslagen 1 kap. 11 &sect;, the voting criterion only).
	 */
	public List<EntityRef> getSubsidiaries(EntityRef parent) {
		EntityRef p = find(parent);
		List<EntityRef> result = new ArrayList<>();
		if (p == null) return result;
		Set<EntityRef> controllers = Collections.newSetFromMap(new IdentityHashMap<>());
		controllers.add(p);
		boolean changed = true;
		while (changed) {
			changed = false;
			// Candidate companies: those held by the parent or a subsidiary
			Set<EntityRef> candidates = Collections.newSetFromMap(new IdentityHashMap<>());
			for (EntityRef c : controllers) {
				for (OwnershipStake s : holdings.getOrDefault(c, Collections.emptyList())) {
					candidates.add(s.getCompany());
				}
			}
			for (EntityRef company : candidates) {
				if (controllers.contains(company)) continue;
				BigDecimal votes = BigDecimal.ZERO;
				for (OwnershipStake s : owners.getOrDefault(company, Collections.emptyList())) {
					if (controllers.contains(s.getOwner())) votes = votes.add(s.getVoteFraction());
				}
				if (votes.compareTo(HALF) > 0) {
					controllers.add(company);
					result.add(company);
					changed = true;
				}
			}
		}
		return result;
	}

	/**
	 * The party that ultimately controls {@code company}: the one that, alone or through
	 * its subsidiaries, holds more than half of the votes and is not itself controlled by
	 * another party in the graph. It is the parent company (moderföretag) of the group, or
	 * a person controlling it. Null if no party controls the company.
	 */
	public EntityRef getUltimateController(EntityRef company) {
		EntityRef c = find(company);
		if (c == null) return null;
		EntityRef best = null;
		int bestSize = -1;
		for (EntityRef n : nodes) {
			if (n == c) continue;
			List<EntityRef> subs = getSubsidiaries(n);
			if (subs.contains(c) && subs.size() > bestSize) {
				// The top parent controls the largest group
				best = n;
				bestSize = subs.size();
			}
		}
		return best;
	}

}
