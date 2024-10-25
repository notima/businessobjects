package org.notima.generic.businessobjects;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A custom object. The purpose is to be able to use it if no other existing fits.
 *  
 * @author daniel.tamm
 *
 */
@XmlRootElement(name = "customobject")
public class CustomObject {

	private String	customType;
	private String	key;
	private String	value;
	
	private List<KeyValue>	attributes;

	public String getCustomType() {
		return customType;
	}

	public void setCustomType(String customType) {
		this.customType = customType;
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

	public List<KeyValue> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<KeyValue> attributes) {
		this.attributes = attributes;
	}
	
}
