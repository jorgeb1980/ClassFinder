package jars.search.gui;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Internationalization bundle singleton, implemented as an Enumeration.
 */
public enum I18n {

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
	private I18n() {
		bundles = new Hashtable<Locale, ResourceBundle>();
		bundles.put(Locale.getDefault(), ResourceBundle.getBundle(LABELS_NAME, Locale.getDefault()));
	}
	
	/**
	 * This method searchs for a label in the resources bundle
	 * @param labelId Name of the resource
	 * @return Value of the resource
	 */
	public String getLabel(String labelId) {
		return getLabel(Locale.getDefault(), labelId);
	}
	
	/**
	 * This method searchs for a label in a particular resource bundle.
	 * @param locale Bundle that has been asked for
	 * @param labelId Name of the resource
	 * @return Value of the resource
	 */
	public String getLabel(Locale locale, String labelId) {
		if (!bundles.containsKey(locale)) {
			bundles.put(locale, ResourceBundle.getBundle(LABELS_NAME, locale));
		}
		return bundles.get(locale).getString(labelId);
	}
}
