package jars.search.core;

import jars.search.gui.I18n;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Implements class and resources searching logic.  
 */
public enum ResourceSearcher {
	
	SEARCHER;
	
	//----------------------------------------------------------
	// Class constants
	
	/** Supported file types for the searcher. */
	private static final String[] ENDINGS = { ".jar", ".zip", ".ear",
			".war" };
	
	//----------------------------------------------------------
	// Class properties
	
	/** Resources cache. */
	private Map<String, Map<String, List<Resource>>> cache = null;
	/** File filter configured to recognize only the supported file types. */
	private FileFilter resourceFilter = null;

	//----------------------------------------------------------
	// Class methods
	
	// Private constructor - we need it to be a singleton
	private ResourceSearcher() {
		resetCache();
		this.resourceFilter = new ResourceFilter();
	}

	/** 
	 * This method cleans the results caché.
	 */
	public void resetCache() {
		this.cache = new HashMap<String, Map<String, List<Resource>>>();
	}
	
	/**
	 * This method resets the cache entry for a given directory
	 * @param directory Name of the directory
	 */
	public void resetCache(String directory) {
		this.cache.remove(directory);
	}

	/** 
	 * This method performs a cached search in the selected directories for the
	 * file name pattern passed as a parameter.
	 * @param directories List of directories to search into.  This list will
	 * limit the results of the search, even if there are any more directories
	 * in the caché
	 * @param pattern File name pattern
	 * @return Search result, with the search pattern used and a list 
	 * of results, grouped by file and indexed by directory.  For example:<br/>
	 * pattern -> "util"<br/>
	 * returns -> [ "/full/path/to" -> [ "/full/path/to/someJar.jar" -> [ "org/apache/commons/BeanUtils.class", "org/apache/commons/BeanUtilsBean.class", "org/apache/commons/BeanUtilsBean2.class", ... ] ], [ "/full/path/to/my_library.jar" -> [ "my/class/utilities/another_class.class" ]], [ ... ] ]<br/>
	 * @throws PatternException If the pattern is not valid
	 */
	public SearchResult search(
			List<String> directories, String pattern) throws PatternException {
		addDirectoriesToCache(directories);
		return searchInCache(directories, pattern);
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
	 * returns -> [ [ "/full/path/to/someJar.jar" -> [ "org/apache/commons/BeanUtils.class", "org/apache/commons/BeanUtilsBean.class", "org/apache/commons/BeanUtilsBean2.class", ... ] ], [ "/yet/another/full/path/to/my_library.jar" -> [ "my/class/utilities/another_class.class" ]], [ ... ] ]<br/>
	 * @throws PatternException If the pattern is not valid
	 */
	private DirectoryResult searchInDirectoryCache(
			String directory, String pattern) throws PatternException {
		Map<String, List<Resource>> jarFiles;
		DirectoryResult ret = new DirectoryResult(directory);
		Pattern regEx = null;
		try {
			regEx = Pattern.compile(pattern.toUpperCase());
		}
		catch(PatternSyntaxException pse) {
			throw new PatternException(
				MessageFormat.format(I18n.RESOURCES.getLabel("pattern.error"), pattern));
		}	
		if (this.cache.containsKey(directory)) {
			jarFiles = this.cache.get(directory);
			for (Iterator<String> it = jarFiles.keySet().iterator(); it
					.hasNext();) {
				String jarFile = (String) it.next();
				FileResult currentFile = null;
				List<Resource> resources = jarFiles.get(jarFile);
				for (Resource resource : resources) {
					Matcher matcher = regEx.matcher(resource.getName().toUpperCase());
					if (matcher.find(0)) {
						if (currentFile == null) {
							currentFile = new FileResult(jarFile);
						}
						currentFile.addResource(resource.getName());
					}
				}
				ret.addFile(currentFile);
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
	private Map<String, List<Resource>> readFromDirectory(String directory) {
		Map<String, List<Resource>> ret = new HashMap<String, List<Resource>>();
		if (directory != null) {
			File dirFile = new File(directory);
			if ((dirFile.exists()) && (dirFile.isDirectory())) {
				File[] files = dirFile.listFiles(this.resourceFilter);
				for (File fichero : files) {
					try {
						String filePath = fichero.getCanonicalPath();
						List<Resource> list = getResources(fichero);
						ret.put(filePath, list);
					}
					catch (IOException ioe) {
						ioe.printStackTrace();
					}
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
	 * @throws PatternException If the pattern is not valid
	 */
	private SearchResult searchInCache(
			List<String> directories, String pattern) throws PatternException {
		SearchResult ret = new SearchResult(pattern);
		for (String directory : directories) {
			DirectoryResult dirResult = searchInDirectoryCache(
					directory, pattern);
			ret.addDirectory(dirResult);
		}
		return ret;
	}

	/**
	 * Reads the resources available into a file (that must be a zip file)
	 * @param file File to be read
	 * @return List of resources inside the file 
	 */
	private List<Resource> getResources(File file) {
		List<Resource> ret = new LinkedList<Resource>();
		try {
			JarFile jf = new JarFile(file);
			Enumeration<JarEntry> entries = jf.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				if (!entry.isDirectory()) {
					Resource r = new Resource(entry.getName(), entry.getSize(), entry.getCompressedSize());
					ret.add(r);
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
