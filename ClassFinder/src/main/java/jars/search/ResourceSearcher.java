package jars.search;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Implements class and resources searching logic.  
 */
public class ResourceSearcher {
	
	//----------------------------------------------------------
	// Class constants
	
	/** Supported file types for the searcher. */
	private static final String[] ENDINGS = { ".jar", ".zip", ".ear",
			".war" };
	
	//----------------------------------------------------------
	// Class properties
	
	/** Resources cache. */
	private Map<String, Map<String, List<String>>> cache = null;
	/** Singleton instance of the resource searcher. */
	private static ResourceSearcher theSearcher = null;
	/** File filter configured to recognize only the supported file types. */
	private FileFilter resourceFilter = null;

	//----------------------------------------------------------
	// Class methods
	
	// Private constructor - we need it to be a singleton
	private ResourceSearcher() {
		this.cache = new HashMap<String, Map<String, List<String>>>();
		this.resourceFilter = new ResourceFilter();
	}

	/**
	 * @return Singleton instance of the searcher
	 */
	public static ResourceSearcher getClassSearcher() {
		if (theSearcher == null) {
			theSearcher = new ResourceSearcher();
		}
		return theSearcher;
	}

	/** 
	 * This method performs a cached search in the selected directories for the
	 * file name pattern passed as a parameter
	 * @param directories List of directories to search into
	 * @param pattern File name pattern
	 * @return List of maps, indexed by file name and containing resource names
	 * that answer to the pattern.  For example:<br/>
	 * pattern -> "util"<br/>
	 * returns -> [ [ "someJar.jar" -> [ "org/apache/commons/BeanUtils.class", "org/apache/commons/BeanUtilsBean.class", "org/apache/commons/BeanUtilsBean2.class", ... ] ], [ "my_library.jar" -> [ "my/class/utilities/another_class.class" ]], [ ... ] ]<br/>
	 */
	public List<Map<String, List<String>>> searchClasses(
			List<String> directories, String pattern) {
		addDirectoriesToCache(directories);
		return searchInCache(directories, pattern.toUpperCase());
	}

	/**
	 * Adds a list of directories to the searcher cache
	 * @param directories List of directories to search into
	 */
	private void addDirectoriesToCache(List<String> directories) {
		for (String directory : directories) {
			if (!this.cache.containsKey(directory)) {
				this.cache.put(directory, readFromDirectory(directory));
			}
		}
	}

	/**
	 * Searches for a pattern into the directories cache.
	 * @param directory Directory to search into
	 * @param pattern Pattern to look for
	 * @return List of maps, indexed by file name and containing resource names
	 * that answer to the pattern.  For example:<br/>
	 * pattern -> "util"<br/>
	 * returns -> [ [ "someJar.jar" -> [ "org/apache/commons/BeanUtils.class", "org/apache/commons/BeanUtilsBean.class", "org/apache/commons/BeanUtilsBean2.class", ... ] ], [ "my_library.jar" -> [ "my/class/utilities/another_class.class" ]], [ ... ] ]<br/>
	 */
	private Map<String, List<String>> searchInDirectoryCache(
			String directory, String pattern) {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		Map<String, List<String>> jarFiles;
		if (this.cache.containsKey(directory)) {
			jarFiles = this.cache.get(directory);
			for (Iterator<String> it = jarFiles.keySet().iterator(); it
					.hasNext();) {
				String jarFile = (String) it.next();

				List<String> classFiles = jarFiles.get(jarFile);
				for (String classFile : classFiles) {
					if (classFile.toUpperCase().contains(pattern)) {
						List<String> theList = ret.get(jarFile);
						if (theList == null) {
							theList = new LinkedList<String>();
							ret.put(jarFile, theList);
						}
						theList.add(classFile);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * This method fills a map of the cache with the set of resources read from
	 * the jars/zips/etc. contained into a directory.
	 * @param directory Directory to read from
	 * @return Map indexed by file name containing lists of resorce names contained in 
	 * each file
	 */
	private Map<String, List<String>> readFromDirectory(String directory) {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		if (directory != null) {
			File dirFile = new File(directory);
			if ((dirFile.exists()) && (dirFile.isDirectory())) {
				File[] files = dirFile.listFiles(this.resourceFilter);
				for (File fichero : files) {
					String filePath = fichero.getPath();
					List<String> list = getResources(fichero);
					ret.put(filePath, list);
				}
			}
		}
		return ret;
	}

	/**
	 * Makes a search into the cache looking for the pattern set in the parameter
	 * @param directories List of directories to search into
	 * @param pattern Pattern to look into
	 * @return List of maps, indexed by file name and containing resource names
	 * that answer to the pattern.  For example:<br/>
	 * pattern -> "util"<br/>
	 * returns -> [ [ "someJar.jar" -> [ "org/apache/commons/BeanUtils.class", "org/apache/commons/BeanUtilsBean.class", "org/apache/commons/BeanUtilsBean2.class", ... ] ], [ "my_library.jar" -> [ "my/class/utilities/another_class.class" ]], [ ... ] ]<br/>
	 */
	private List<Map<String, List<String>>> searchInCache(
			List<String> directories, String pattern) {
		List<Map<String, List<String>>> ret = new LinkedList<Map<String, List<String>>>();
		for (String directory : directories) {
			Map<String, List<String>> table = searchInDirectoryCache(
					directory, pattern);
			if ((table != null) && (table.size() > 0)) {
				ret.add(table);
			}
		}
		return ret;
	}

	/**
	 * Reads the resources available into a file (that must be a zip file)
	 * @param file File to be read
	 * @return List of resources inside the file 
	 */
	private List<String> getResources(File file) {
		List<String> ret = new LinkedList<String>();
		try {
			JarFile jf = new JarFile(file);
			Enumeration<JarEntry> entries = jf.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				if (!entry.isDirectory()) {
					ret.add(entry.getName());
				}
			}
			jf.close();
		} catch (IOException ioe) {
			System.err.println("---> Error processing " + file.getPath());
			ioe.printStackTrace();
		}
		return ret;
	}

	/**
	 * This class implements a file filter to be used in the directory search.
	 */
	private class ResourceFilter implements FileFilter {
		
		// Default constructor
		private ResourceFilter() {
		}

		/**
		 * This method implements the filter: will only accept the 
		 * file endings supported by the class <code>ENDINGS</code> constant
		 */
		public boolean accept(File pathname) {
			String fileName = pathname.getName();
			boolean accept = false;
			int i = 0;
			int length = ResourceSearcher.ENDINGS.length;
			while ((!accept) && (i < length)) {
				accept = fileName
						.endsWith(ResourceSearcher.ENDINGS[(i++)]);
			}
			return accept;
		}
	}
}
