package org.notima.generic.businessobjects.ownership;

import java.util.ArrayList;
import java.util.List;

/**
 * A node of an owner tree (who owns the root, upwards) or a holding tree (what the root
 * owns, downwards), built by {@link OwnershipGraph}.
 */
public class OwnershipTreeNode {

	private final EntityRef			entity;
	private final OwnershipStake	stake;
	private final OwnershipShare	share;
	private final boolean			cycle;
	private final List<OwnershipTreeNode>	children = new ArrayList<>();

	OwnershipTreeNode(EntityRef entity, OwnershipStake stake, OwnershipShare share, boolean cycle) {
		this.entity = entity;
		this.stake = stake;
		this.share = share;
		this.cycle = cycle;
	}

	public EntityRef getEntity() {
		return entity;
	}

	/**
	 * @return	The stake between this node and its parent: in an owner tree this node's
	 * 			stake in the parent, in a holding tree the parent's stake in this node.
	 * 			Null for the root.
	 */
	public OwnershipStake getStake() {
		return stake;
	}

	/**
	 * @return	The share along the path from the root: in an owner tree this node's
	 * 			indirect share of the root, in a holding tree the root's indirect share of
	 * 			this node. {@link OwnershipShare#ALL} for the root.
	 */
	public OwnershipShare getShare() {
		return share;
	}

	/**
	 * @return	True if this entity already appears higher up on the path (cross
	 * 			ownership); the node then has no children.
	 */
	public boolean isCycle() {
		return cycle;
	}

	public List<OwnershipTreeNode> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		return entity + (cycle ? " (cycle)" : "");
	}

}
