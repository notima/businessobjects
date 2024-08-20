package org.notima.generic.businessobjects.util;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.notima.generic.ifacebusinessobjects.LanguageTranslator;

/**
 * Class used to translate different business object's terms and other labels / etc.
 * 
 * @author Daniel Tamm
 *
 */
public class Translator implements LanguageTranslator {
	
	public static final String BO_TRANSLATIONS_LABEL = "BOTranslations";
	
	public static Locale[] supportedLocales = {
			Locale.ENGLISH,
			new Locale("sv")
	};

	private static Map<String, ResourceBundle> bundles;
	private static ResourceBundle defaultBundle;
	
	static {
		
		bundles = new TreeMap<String, ResourceBundle>();
		ResourceBundle b;
		for (Locale loc : supportedLocales) {
			
			try {
				b = ResourceBundle.getBundle(BO_TRANSLATIONS_LABEL, loc);
				if (b!=null && b.getLocale().getLanguage().equals(loc.getLanguage())) {
					bundles.put(loc.getISO3Language(), b);
				}
			} catch (MissingResourceException mre) {
				System.out.println(mre.getClassName());
			}
			
		}

		try {
			defaultBundle = ResourceBundle.getBundle(BO_TRANSLATIONS_LABEL);
		} catch (MissingResourceException mre) {
			System.out.println(mre.getClassName());
		}
		
	}
	
	/**
	 * Returns a translation of the given label (key).
	 * 
	 * @param label		The label to translate
	 * @param lang		The language to translate to
	 * @return			A translation. If no translation is available the label itself is returned.
	 */
	public String getTranslation(String label, String lang) {
		
		// Locate the bundle
		ResourceBundle b = bundles.get(lang);
		if (b==null) {
			if (defaultBundle!=null) {
				try {
					return defaultBundle.getString(label);
				} catch (MissingResourceException mre) {
					return label;
				}
			} else {
				return label;
			}
		} else {
			String translation = null;
			try {
				translation = b.getString(label);
				return translation;
			} catch (MissingResourceException me) {
				return label;
			}
		}
		
	}

	/**
	 * Capitalizes first letter of a text.
	 * 
	 * @param text		The text.
	 * @return			The text with the first letter as a capital letter.
	 */
	public static String capitalizeFirstLetter(String text) {
		if (text==null) return null;
		StringBuffer sb = new StringBuffer(text);
		sb.replace(0, 1, text.substring(0, 1).toUpperCase());
		return sb.toString();
	}

	@Override
	public String getTranslationAndCapitalizeFirstLetter(String label, String lang) {
		return capitalizeFirstLetter(getTranslation(label, lang));
	}
	
	
	
}
