package org.notima.generic.businessobjects;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import org.notima.generic.ifacebusinessobjects.IProductCategory;

/**
 * Product category.
 * 
 * @author daniel
 *
 */
@Entity
@XmlType(name="ProductCategory")
@XmlRootElement(name = "ProductCategory")
public class ProductCategory implements Serializable, IProductCategory {

	
	@Id
	@GeneratedValue
	private Integer		id;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2712782417399635960L;
	
	protected String key;
	protected String name;
	protected String parentKey;
	
	protected String keyReference;
	protected String parentKeyReference;
	
	protected List<Translation> translations;
	protected List<KeyValue>	attributes;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	/**
	 * When synching between different systems this reference can be 
	 * used to map the category from one system to another
	 * 
	 * @return
	 */
	public String getKeyReference() {
		return keyReference;
	}

	public void setKeyReference(String keyReference) {
		this.keyReference = keyReference;
	}

	/**
	 * When synching between different systems this reference can be 
	 * used to map the category from one system to another
	 * 
	 * @return
	 */
	public String getParentKeyReference() {
		return parentKeyReference;
	}

	public void setParentKeyReference(String parentKeyReference) {
		this.parentKeyReference = parentKeyReference;
	}

	public List<Translation> getTranslations() {
		return translations;
	}

	public void setTranslations(List<Translation> translations) {
		this.translations = translations;
	}

	public List<KeyValue> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<KeyValue> attributes) {
		this.attributes = attributes;
	}
	
	
	

}
