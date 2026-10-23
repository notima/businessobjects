package org.notima.generic.businessobjects;

/**
 * One object (value) within an accounting dimension, e.g. a cost center or
 * project entry.
 *
 * <p>In the SIE format these correspond to {@code #DIM} / {@code #OBJEKT} records.
 * Standard dimension IDs: 1 = cost center, 6 = project.</p>
 */
public class AccountingObject {

	/** Standard SIE dimension ID for cost centers. */
	public static final int DIM_COST_CENTER = 1;
	/** Standard SIE dimension ID for projects. */
	public static final int DIM_PROJECT     = 6;

	/** Dimension ID this object belongs to (e.g. 1 for cost center). */
	private int    dimId;
	/** Human-readable dimension name (e.g. {@code "Kostnadsställe"}). */
	private String dimName;
	/** Object identifier within the dimension (e.g. {@code "100"}, {@code "P001"}). */
	private String objId;
	/** Human-readable object description (e.g. {@code "Marketing"}). */
	private String objName;

	public AccountingObject() {}

	public AccountingObject(int dimId, String dimName, String objId, String objName) {
		this.dimId   = dimId;
		this.dimName = dimName;
		this.objId   = objId;
		this.objName = objName;
	}

	public int getDimId() {
		return dimId;
	}
	public void setDimId(int dimId) {
		this.dimId = dimId;
	}
	public String getDimName() {
		return dimName;
	}
	public void setDimName(String dimName) {
		this.dimName = dimName;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}

	@Override
	public String toString() {
		return objId + (objName != null && !objName.isEmpty() ? " — " + objName : "");
	}
}
