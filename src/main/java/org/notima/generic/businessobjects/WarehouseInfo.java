package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Warehouse information
 * 
 * @author Daniel Tamm
 *
 */
@XmlRootElement(name="WarehouseInfo")
public class WarehouseInfo {

	private String	warehouseReference;
	private String	warehouseName;
	private List<WarehouseLocator> warehouseLocator	 = new ArrayList<WarehouseLocator>();
	
	public String getWarehouseReference() {
		return warehouseReference;
	}
	public void setWarehouseReference(String warehouseReference) {
		this.warehouseReference = warehouseReference;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public List<WarehouseLocator> getWarehouseLocator() {
		return warehouseLocator;
	}
	public void setWarehouseLocator(List<WarehouseLocator> warehouseLocator) {
		this.warehouseLocator = warehouseLocator;
	}
	
	
}
