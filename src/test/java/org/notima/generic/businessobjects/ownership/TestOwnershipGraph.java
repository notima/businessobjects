package org.notima.generic.businessobjects.ownership;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class TestOwnershipGraph {

	private static final LocalDate DATE = LocalDate.of(2026, 1, 1);

	private final EntityRef person = EntityRef.person("p1", "19700101-1234", "Anna");
	private final EntityRef holding = EntityRef.organization("o1", "556000-0001", "Holding AB");
	private final EntityRef sub = EntityRef.organization("o2", "556000-0002", "Dotter AB");
	private final EntityRef grandchild = EntityRef.organization("o3", "556000-0003", "Barnbarn AB");
	private final EntityRef other = EntityRef.organization("o4", "556000-0004", "Annan AB");

	private static OwnershipStake stake(EntityRef owner, EntityRef company, long shares, long total, String source) {
		OwnershipStake s = new OwnershipStake(owner, company, DATE, shares, total);
		s.setSource(source);
		return s;
	}

	private static void assertFraction(String expected, BigDecimal actual) {
		assertEquals(0, new BigDecimal(expected).compareTo(actual), "expected " + expected + " but was " + actual);
	}

	/** Anna owns Holding 100 %; Holding owns 60 % of Dotter; Dotter 50 % and Annan 50 % of Barnbarn. */
	private OwnershipGraph group() {
		return OwnershipGraph.of(Arrays.asList(
				stake(person, holding, 100, 100, "holding.shl"),
				stake(holding, sub, 600, 1000, "sub.shl"),
				stake(other, sub, 400, 1000, "sub.shl"),
				stake(sub, grandchild, 50, 100, "grandchild.shl"),
				stake(other, grandchild, 50, 100, "grandchild.shl")));
	}

	@Test
	public void matchesOnRegistryIdOrCanonicalTaxId() {
		EntityRef byTaxId = EntityRef.organization("other-register-id", "5560000002", "Dotter");
		assertTrue(sub.matches(byTaxId));
		assertTrue(sub.matches(EntityRef.organization(null, "16556000-0002", null)));
		assertFalse(sub.matches(holding));
		assertFalse(sub.matches(EntityRef.person(null, "556000-0002", null)));
	}

	@Test
	public void mergesMatchingParties() {
		OwnershipGraph g = group();
		assertEquals(5, g.getNodes().size());
		EntityRef found = g.find(EntityRef.organization(null, "5560000002", null));
		assertNotNull(found);
		assertEquals("Dotter AB", found.getName());
		assertEquals(2, g.getOwners(found).size());
		assertEquals(1, g.getHoldings(found).size());
	}

	@Test
	public void indirectShareMultipliesAlongPaths() {
		OwnershipGraph g = group();
		assertFraction("0.3", g.getIndirectShare(person, grandchild).getCapital());
		assertFraction("0.6", g.getIndirectShare(holding, sub).getCapital());
		// Annan: 50 % directly + 40 % × 50 % via Dotter
		assertFraction("0.7", g.getIndirectShare(other, grandchild).getCapital());
		assertFraction("0", g.getIndirectShare(grandchild, person).getCapital());
	}

	@Test
	public void ultimateOwners() {
		Map<EntityRef, OwnershipShare> u = group().getUltimateOwners(grandchild);
		assertEquals(2, u.size());
		for (Map.Entry<EntityRef, OwnershipShare> e : u.entrySet()) {
			if (e.getKey().matches(person)) assertFraction("0.3", e.getValue().getCapital());
			else if (e.getKey().matches(other)) assertFraction("0.7", e.getValue().getCapital());
			else fail("unexpected ultimate owner " + e.getKey());
		}
	}

	@Test
	public void subsidiariesAndController() {
		OwnershipGraph g = group();
		// Holding controls Dotter (60 %) but Dotter only has 50 % of Barnbarn
		List<EntityRef> subs = g.getSubsidiaries(holding);
		assertEquals(1, subs.size());
		assertTrue(subs.get(0).matches(sub));
		assertTrue(g.getUltimateController(sub).matches(person));
		assertNull(g.getUltimateController(grandchild));
	}

	@Test
	public void controlThroughSubsidiariesCounts() {
		OwnershipGraph g = OwnershipGraph.of(Arrays.asList(
				stake(holding, sub, 100, 100, "sub.shl"),
				stake(holding, grandchild, 30, 100, "gc.shl"),
				stake(sub, grandchild, 30, 100, "gc.shl"),
				stake(other, grandchild, 40, 100, "gc.shl")));
		assertEquals(2, g.getSubsidiaries(holding).size());
	}

	@Test
	public void votesDecideControl() {
		OwnershipStake a = stake(holding, sub, 100, 1000, "sub.shl");
		a.setVotes(new BigDecimal("1000"));
		a.setTotalVotes(new BigDecimal("1900"));
		OwnershipStake b = stake(other, sub, 900, 1000, "sub.shl");
		b.setVotes(new BigDecimal("900"));
		b.setTotalVotes(new BigDecimal("1900"));
		OwnershipGraph g = OwnershipGraph.of(Arrays.asList(a, b));
		assertFraction("0.1", g.getIndirectShare(holding, sub).getCapital());
		assertEquals(1, g.getSubsidiaries(holding).size());
		assertTrue(g.getSubsidiaries(other).isEmpty());
	}

	@Test
	public void crossOwnershipTerminates() {
		OwnershipGraph g = OwnershipGraph.of(Arrays.asList(
				stake(holding, sub, 50, 100, "sub.shl"),
				stake(sub, holding, 10, 100, "holding.shl"),
				stake(person, holding, 90, 100, "holding.shl")));
		OwnershipTreeNode tree = g.getOwnerTree(sub);
		assertEquals(1, tree.getChildren().size());
		OwnershipTreeNode h = tree.getChildren().get(0);
		assertEquals(2, h.getChildren().size());
		assertTrue(h.getChildren().stream().anyMatch(OwnershipTreeNode::isCycle));
		assertFraction("0.45", g.getUltimateOwners(sub).values().iterator().next().getCapital());
	}

	@Test
	public void holdingTree() {
		OwnershipTreeNode tree = group().getHoldingTree(person);
		OwnershipTreeNode h = tree.getChildren().get(0);
		OwnershipTreeNode d = h.getChildren().get(0);
		OwnershipTreeNode gc = d.getChildren().get(0);
		assertTrue(gc.getEntity().matches(grandchild));
		assertFraction("0.3", gc.getShare().getCapital());
	}

	@Test
	public void secondSourceForSameCompanyIsIgnored() {
		OwnershipGraph g = OwnershipGraph.of(Arrays.asList(
				stake(holding, sub, 100, 100, "sub.shl"),
				stake(other, sub, 100, 100, "copy-of-sub.shl")));
		assertEquals(1, g.getOwners(sub).size());
		assertEquals(List.of("copy-of-sub.shl"), g.getConflictingSources());
	}

	@Test
	public void topCompanies() {
		List<EntityRef> top = group().getTopCompanies();
		assertEquals(2, top.size());
		assertTrue(top.get(0).matches(holding));
		assertTrue(top.get(1).matches(other));
	}

}
