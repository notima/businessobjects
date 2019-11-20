package org.notima.generic.businessobjects;

public class KeyValue {

	private	String		key;
	private String		value;
	private Object		object;
	
	public KeyValue() {
		
	}
	
	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
		this.object = value;
	}
	
	public KeyValue(String key, Object object) {
		this.key = key;
		this.object = object;
		if (object!=null)
			this.value = object.toString();
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	

}
