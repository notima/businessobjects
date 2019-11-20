package org.notima.generic.ifacebusinessobjects;

import java.util.List;

import org.notima.generic.businessobjects.KeyValue;
import org.notima.generic.businessobjects.Translation;

public interface IProductCategory {

	public String getKey();

	public void setKey(String key);

	public String getName();

	public void setName(String name);

	public String getParentKey();

	public void setParentKey(String parentKey);

	/**
	 * When synching between different systems this reference can be 
	 * used to map the category from one system to another
	 * 
	 * @return
	 */
	public String getKeyReference();

	public void setKeyReference(String keyReference);

	/**
	 * When synching between different systems this reference can be 
	 * used to map the category from one system to another
	 * 
	 * @return
	 */
	public String getParentKeyReference();

	public void setParentKeyReference(String parentKeyReference);

	public List<Translation> getTranslations();

	public void setTranslations(List<Translation> translations);

	public List<KeyValue> getAttributes();

	public void setAttributes(List<KeyValue> attributes);
	
}
