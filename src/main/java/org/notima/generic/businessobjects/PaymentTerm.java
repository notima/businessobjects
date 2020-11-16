package org.notima.generic.businessobjects;

public class PaymentTerm {

	private String	key;
	private boolean useGrouping;
	private boolean useRounding;
	private boolean dynamicFactoringOption;
	private double extraReserveAmt;
	private int netDays;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isUseGrouping() {
		return (useGrouping);
	}
	
	public boolean isUseRounding() {
		return useRounding;
	}

	public double getExtraReserveAmt() {
		return extraReserveAmt;
	}

	public void setExtraReserveAmt(double extraReserveAmt) {
		this.extraReserveAmt = extraReserveAmt;
	}

	public void setUseGrouping(boolean useGrouping) {
		this.useGrouping = useGrouping;
	}

	public void setUseRounding(boolean useRounding) {
		this.useRounding = useRounding;
	}

	public boolean isDynamicFactoringOption() {
		return dynamicFactoringOption;
	}

	public void setDynamicFactoringOption(boolean dynamicFactoringOption) {
		this.dynamicFactoringOption = dynamicFactoringOption;
	}

	public int getNetDays() {
		return netDays;
	}

	public void setNetDays(int netDays) {
		this.netDays = netDays;
	}

	
}
