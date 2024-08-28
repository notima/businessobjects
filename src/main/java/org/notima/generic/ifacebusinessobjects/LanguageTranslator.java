package org.notima.generic.ifacebusinessobjects;

public interface LanguageTranslator {

	/**
	 * Returns a translation of the given label (key).
	 * 
	 * @param label		The label to translate
	 * @param lang		The language to translate to in ISO3 format (e.g. 'eng')
	 * @return			A translation. If no translation is available the label itself is returned.
	 */
	String getTranslation(String label, String lang);
	
	/**
	 * Returns true if there exists a translation for this label (key).
	 * 
	 * @param label
	 * @param lang
	 * @return
	 */
	boolean hasTranslation(String label, String lang);

	/**
	 * Returns a translation of the given label (key) and capitalizes the first letter.
	 * 
	 * @param label		The label to translate
	 * @param lang		The language to translate to in ISO3 format (e.g. 'eng')
	 * 
	 * @return			A translation. If no translation is available the label itself is returned.
	 */
	String getTranslationAndCapitalizeFirstLetter(String label, String lang);
	
	/**
	 * Short hand for 
	 * getTranslationAndCapitalizeFirstLetter
	 * Returns a translation of the given label (key) and capitalizes the first letter.
	 * 
	 * @param label		The label to translate
	 * @param lang		The language to translate to in ISO3 format (e.g. 'eng')
	 * 
	 * @return			A translation. If no translation is available the label itself is returned.
	 */
	String getTranslationAFL(String label, String lang);
	
	
}
