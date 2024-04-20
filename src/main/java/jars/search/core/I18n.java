package jars.search.core;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Internationalization bundle singleton.  Implemented as an enum as
 * recommended in Effective Java 2nd. Edition.  Intended for inner use of the 
 * library only.
 */
public enum I18n {

	/** Internationalization bundle class singleton. */
	RESOURCES;
	
	//-------------------------------------------------------
	// Class constants
	
	/** Location of the labels resouce bundle. */
	public static final String LABELS_NAME = "jars.search.i18n.Labels";
	
	//-------------------------------------------------------
	// Class properties
	
	// ResourceBundle instance
	private Map<Locale, ResourceBundle> bundles = null;
	
	//-------------------------------------------------------
	// Class methods
	
	/**
	 * Private constructor
	 */
	I18n() {
		bundles = new Hashtable<Locale, ResourceBundle>();
		bundles.put(Locale.getDefault(), ResourceBundle.getBundle(LABELS_NAME, Locale.getDefault()));
	}
	
	/**
	 * Searchs for a label in the resources bundle.
	 * @param labelId Name of the resource.
	 * @return Value of the resource.
	 */
	public String getLabel(String labelId) {
		return getLabel(Locale.getDefault(), labelId);
	}
	
	/**
	 * Searchs for a label in a particular resource bundle.
	 * @param locale Bundle that has been asked for.
	 * @param labelId Name of the resource.
	 * @return Value of the resource.
	 */
	public String getLabel(Locale locale, String labelId) {
		if (!bundles.containsKey(locale)) {
			bundles.put(locale, ResourceBundle.getBundle(LABELS_NAME, locale));
		}
		return bundles.get(locale).getString(labelId);
	}
}
