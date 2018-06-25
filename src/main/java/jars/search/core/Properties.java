package jars.search.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class finder properties singleton
 */
public enum Properties {
	INSTANCE;
	
	//------------------------------------
	// Class constants
	
	// Name of the properties file
	private static final String PROPERTIES_FILE = "classfinder.properties";
	
	//------------------------------------
	// Class properties
	
	// Data
	private java.util.Properties properties = new java.util.Properties();
	
	//------------------------------------
	// Class methods
	
	// Hidden constructor
	private Properties() {
		InputStream is = null;
		try {
			is = Properties.class.getClassLoader().
					getResourceAsStream(PROPERTIES_FILE);
			properties.load(is);;
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		finally {
			if (is != null) {
				try {
					is.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Returns a property stored in the file
	 * @param property Name of the requested property
	 * @return
	 */
	public String get(String property) {
		return properties.getProperty(property);
	}
}