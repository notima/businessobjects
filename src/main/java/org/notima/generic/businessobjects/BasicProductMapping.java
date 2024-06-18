package org.notima.generic.businessobjects;

import org.notima.generic.ifacebusinessobjects.ProductMapping;

public class BasicProductMapping implements ProductMapping {

	private String sourceProductId;
	private String sourceName;
	private String destinationProductId;
	private String destinationName;
	private String sourceSystem;
	private String destinationSystem;
	
	@Override
	public String getSourceProductId() {
		return sourceProductId;
	}

	@Override
	public String getSourceName() {
		return sourceName;
	}

	@Override
	public String getDestinationProductId() {
		return destinationProductId;
	}

	@Override
	public String getDestinationName() {
		return destinationName;
	}

	@Override
	public String getSourceSystem() {
		return sourceSystem;
	}

	@Override
	public String getDestinationSystem() {
		return destinationSystem;
	}

	public void setSourceProductId(String sourceProductId) {
		this.sourceProductId = sourceProductId;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public void setDestinationProductId(String destinationProductId) {
		this.destinationProductId = destinationProductId;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public void setDestinationSystem(String destinationSystem) {
		this.destinationSystem = destinationSystem;
	}
	
	

}
