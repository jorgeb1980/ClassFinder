package jars.search.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Implements class and resources searching logic.  Singleton implemented as an enum as
 * recommended in Effective Java 2nd. Edition.
 */
public enum ResourceSearcher {
	
	/** Singleton instance of the Resource Searcher. */
	SEARCHER;
	
	//----------------------------------------------------------
	// Class constants
	
	/** Supported file types for the searcher. */
	private static final String[] ENDINGS = { ".jar", ".zip", ".ear", ".war" };
	
	//----------------------------------------------------------
	// Class properties
	
	/** Resources cache. */
	private Map<File, Map<File, List<Resource>>> cache = null;
	/** File filter configured to recognize only the supported file types. */
	private FileFilter resourceFilter = null;

	//----------------------------------------------------------
	// Class methods
	
	// Private constructor - we need it to be a singleton
	ResourceSearcher() {
		resetCache();
		this.resourceFilter = new ResourceFilter();
	}

	/** 
	 * Cleans the directory cach�.
	 */
	public void resetCache() {
		this.cache = new HashMap<>();
	}
	
	/**
	 * Resets the cache entry for a given directory.
	 * @param directory Name of the directory.
	 */
	public void resetCache(File directory) {
		this.cache.remove(directory);
	}

	/** 
	 * Performs a cached search in the selected directory for the
	 * search pattern passed as a parameter.
	 * @param directory Directory to search into.  This parameter will
	 * limit the scope of the search, even if there are any more directories
	 * in the cach�.  The search will be driven into any zip/jar file contained
	 * in this directory.
	 * @param pattern Search pattern.
	 * @return Search result, with the search pattern used and a list 
	 * of results, grouped by file and indexed by directory.  For example:<br/>
	 * pattern -> "util"<br/>
	 * map of results (simplified) -> [ "/full/path/to/someJar.jar" :  [ "org/apache/commons/BeanUtils.class", "org/apache/commons/BeanUtilsBean.class", "org/apache/commons/BeanUtilsBean2.class", ... ], [ "/full/path/to/my_library.jar" : [ "my/class/utilities/another_class.class" ], [ ... ] ]<br/>
	 * @throws SearchException Wraps pattern validation problems, and every other
	 * possible checked exception thrown inside.
	 */
	public SearchResult search(File directory, String pattern) throws SearchException {
		var dirs = new LinkedList<File>();
		dirs.add(directory);
		return search(dirs, pattern);
	}
	
	/** 
	 * Performs a cached search in the selected directories for the
	 * search pattern passed as a parameter.
	 * @param directories List of directories to search into.  This list will
	 * limit the scope of the search, even if there are any more directories
	 * in the cache.  The search will be driven into any zip/jar file contained
	 * in these directories.
	 * @param pattern Search pattern.
	 * @return Search result, with the search pattern used and a list 
	 * of results, grouped by file and indexed by directory.  For example:<br/>
	 * pattern -> "util"<br/>
	 * map of results (simplified) -> [ "/full/path/to/someJar.jar" :  [ "org/apache/commons/BeanUtils.class", "org/apache/commons/BeanUtilsBean.class", "org/apache/commons/BeanUtilsBean2.class", ... ], [ "/full/path/to/my_library.jar" : [ "my/class/utilities/another_class.class" ], [ ... ] ]<br/>
	 * @throws SearchException Wraps pattern validation problems, and every other
	 * possible checked exception thrown inside.
	 */
	public SearchResult search(List<File> directories, String pattern) throws SearchException {
		List<File> dirs = removeDuplicates(directories);
		addDirectoriesToCache(dirs);
		return searchInCache(dirs, pattern);
	}

	// Removes the duplicates in a list of directories, returning them
	//	in another list
	private List<File> removeDuplicates(List<File> directories) {
		List<File> ret = new LinkedList<File>();
		for (File f: directories) {
			if (!ret.contains(f)) {
				ret.add(f);
			}
		}
		return ret;
	}

	// Safely recovers a directory canonical path
	private String canonicalPath(File dir) {
		String ret = null;
		try {
			if (dir != null && dir.exists() && dir.isDirectory()) {
				ret = dir.getCanonicalPath();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Adds a list of directories to the searcher cach�.
	 * @param directories List of directories to search into.
	 * @throws SearchException If any I/O error happens.
	 */
	private void addDirectoriesToCache(List<File> directories) 
			throws SearchException {
		for (File directory : directories) {
			if (!this.cache.containsKey(canonicalPath(directory))) {
				this.cache.put(directory, readFromDirectory(directory));
			}
		}
	}

	/**
	 * Searches for a pattern into the directories cach�.
	 * @param directory Directory to search into
	 * @param pattern Pattern to look for
	 * @return List of resources that answer to the pattern.  For example:<br/>
	 * pattern -> "util"<br/>
	 * returns -> [ "org/apache/commons/BeanUtils.class", "org/apache/commons/BeanUtilsBean.class", "org/apache/commons/BeanUtilsBean2.class", ... ]<br/>
	 * @throws SearchException Wraps pattern validation problems
	 */
	private Map<File, List<Resource>> searchInDirectoryCache(
			File directory, String pattern) throws SearchException {
		Map<File, List<Resource>> jarFiles;
		var ret = new HashMap<File, List<Resource>>();
		Pattern regEx = null;
		try {
			regEx = Pattern.compile(pattern.toUpperCase());
		}
		catch(PatternSyntaxException pse) {
			throw new SearchException(
				MessageFormat.format(I18n.RESOURCES.getLabel("pattern.error"), pattern));
		}	
		if (this.cache.containsKey(directory)) {
			jarFiles = this.cache.get(directory);
			for (var jarFile: jarFiles.keySet()) {
				var resources = jarFiles.get(jarFile);
				var tmp = new LinkedList<Resource>();
				for (var resource : resources) {
					var matcher = regEx.matcher(resource.getName().toUpperCase());
					if (matcher.find(0)) {
						tmp.add(resource);
					}
				}
				if (!tmp.isEmpty()) {
					ret.put(jarFile, tmp);
				}
			}
		}
		return ret;
	}

	/**
	 * Fills a map of the cach� with the set of resources read from
	 * the jars/zips/etc. contained into a directory.
	 * @param directory Directory to read from.
	 * @return Map indexed by file name containing lists of resorce names contained in 
	 * each file.
	 * @throws SearchException If any I/O error happens.
	 */
	private Map<File, List<Resource>> readFromDirectory(File directory) 
			throws SearchException {
		try {
			var ret = new HashMap<File, List<Resource>>();
			if (directory != null) {
				if ((directory.exists()) && (directory.isDirectory())) {
					File[] files = directory.listFiles(this.resourceFilter);
					for (var file : files) {
						ret.put(file.getCanonicalFile(), getResources(file));
					}
				}
			}
			return ret;
		}
		catch(IOException ioe) {
			throw new SearchException(ioe);
		}
	}

	/**
	 * Makes a search into the cach� looking for the pattern set in the parameter.
	 * @param directories List of directories to search into.
	 * @param pattern Pattern to look into.
	 * @return List of maps, indexed by file name and containing resource names
	 * that answer to the pattern.  For example:<br/>
	 * pattern -> "util"<br/>
	 * returns -> [ [ "someJar.jar" -> [ "org/apache/commons/BeanUtils.class", "org/apache/commons/BeanUtilsBean.class", "org/apache/commons/BeanUtilsBean2.class", ... ] ], [ "my_library.jar" -> [ "my/class/utilities/another_class.class" ]], [ ... ] ]<br/>
	 * @throws SearchException If the pattern is not valid.
	 */
	private SearchResult searchInCache(
			List<File> directories, String pattern) throws SearchException {
		var tmp = new HashMap<File, List<Resource>>();
		var initTime = new Date().getTime();
		for (var directory : directories) {
			var resources = searchInDirectoryCache(directory, pattern);
			if (!resources.values().isEmpty()) {
				for (var jarFile: resources.keySet()) {
					List<Resource> existingResources = null;
					if (tmp.containsKey(jarFile)) {
						existingResources = tmp.get(jarFile);
					}
					else {
						existingResources = new LinkedList<Resource>();
					}
					existingResources.addAll(resources.get(jarFile));
					tmp.put(jarFile, existingResources);
				}
			}
		}
		var endTime = new Date().getTime();
		return SearchResult.createResult(pattern, endTime - initTime, tmp);
	}

	/**
	 * Reads the resources available into a file (that must be a zip file).
	 * @param file File to be read.
	 * @return List of resources inside the file.
	 * @throws SearchException If any I/O error happens.
	 */
	private List<Resource> getResources(File file) throws SearchException {
		try {
			var ret = new LinkedList<Resource>();
			if (file.exists() && file.length() > 0) {
				var jf = new JarFile(file);
				var entries = jf.entries();
				while (entries.hasMoreElements()) {
					var entry = entries.nextElement();
					if (!entry.isDirectory()) {
						var r = new Resource(
							entry.getName(),
							entry.getSize(),
							entry.getCompressedSize(),
							file
						);
						ret.add(r);
					}
				}
				jf.close();
			}
			return ret;
		}
		catch(IOException ioe) {
			throw new SearchException(ioe);
		}
	}

	/**
	 * This class implements a file filter to be used in the directory search.
	 */
	private static class ResourceFilter implements FileFilter {
		
		// Default constructor
		private ResourceFilter() {}

		/**
		 * Implements the filter: it will only accept the 
		 * file endings supported by the class <code>ENDINGS</code> constant.
		 */
		public boolean accept(File pathname) {
			var fileName = pathname.getName();
			var accept = false;
			var i = 0;
			var length = ResourceSearcher.ENDINGS.length;
			while ((!accept) && (i < length)) {
				accept = fileName.endsWith(ResourceSearcher.ENDINGS[(i++)]);
			}
			return accept;
		}
	}
}
