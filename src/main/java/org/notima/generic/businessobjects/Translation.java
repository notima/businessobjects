package org.notima.generic.businessobjects;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * Class used to pass translations
 * 
 * @author daniel
 *
 */
@Entity
public class Translation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7516319840105955932L;
	
	protected	String	lang;
	protected	String	translation;
	
	/**
	 * ISO lang code. For instance sv_SE
	 * 
	 * @return
	 */
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	/**
	 * The actual translation
	 * 
	 * @return
	 */
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	
	

}
