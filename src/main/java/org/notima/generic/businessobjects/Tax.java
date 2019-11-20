package org.notima.generic.businessobjects;

import javax.persistence.Entity;

@Entity
public class Tax {

	private String key;
	private double rate;
	
	public String getKey() {
		return key;
	}
	
	public double getRate() {
		return rate;
	}
	
}
