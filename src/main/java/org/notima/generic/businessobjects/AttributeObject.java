package org.notima.generic.businessobjects;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.notima.generic.businessobjects.exception.KeyNotFoundException;


/**
 * 
 * Derive from this class to add custom attribute to a class.
 * 
 * @author Daniel Tamm
 *
 */
public class AttributeObject {

	private Map<String, KeyValue>	attributes;

	public List<KeyValue> getAttributes() {
		if (attributes==null) return new ArrayList<KeyValue>();
		return new ArrayList<KeyValue>(attributes.values());
	}

	public void addAttribute(String key, String value) {
		if (key==null) return;
		if (attributes==null) {
			attributes = new TreeMap<String, KeyValue>();
		}
		attributes.put(key, new KeyValue(key, value));
	}
	
	@Transient
	public String getAttribute(String key) throws KeyNotFoundException {
		if (key==null) throw new KeyNotFoundException("Null key not allowed");
		if (attributes==null) throw new KeyNotFoundException(key);
		KeyValue kv = attributes.get(key);
		if (kv==null)
			throw new KeyNotFoundException(key);
		return kv.getValue();
		
	}
	
	
}
