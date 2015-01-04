/**
 * 
 */
package jars.search.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class contains the result of a search of resources in a repository
 * of directories.
 */
public class SearchResult {
	
	//------------------------------------------------------------
	// Properties of the class
	
	/** Search pattern. */
	private String pattern;
	/** List of Directories that contain results for the search. */
	private List<DirectoryResult> directories = null;
	/** Will index a map of directories by directory name to search faster. */
	private Map<String, DirectoryResult> index = null;
	
	//------------------------------------------------------------
	// Methods of the class
	
	
	/** 
	 * Default visibility constructor (the class is only intended to be
	 * instantiated from the ResourceSearcher) 
	 * @param pattern Search pattern 
	 */
	SearchResult(String pattern) {
		this.pattern = pattern;
		directories = new LinkedList<DirectoryResult>();
		index = new HashMap<String, DirectoryResult>();
	}

	// Default visibility method in order to add directories to the search result
	//	only from classes inside this package
	void addDirectory(DirectoryResult directory) {
		if (!index.containsKey(directory.getPath())) {
			directories.add(directory);
			index.put(directory.getPath(), directory);
		}
	}
	
	/**
	 * @return Search pattern used
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @return Defensive copy of the directories list
	 */
	public List<DirectoryResult> getResultDirectories() {
		return new LinkedList<DirectoryResult>(directories);
	}
	
	/**
	 * Returns a directory result by its path
	 * @param path Canonical path of the directory to look for inside the
	 * searchs result
	 * @return Directory result indexed by the path
	 */
	public DirectoryResult getDirectoryByPath(String path) {
		return index.get(path);
	}
	
	/**
	 * Shortcut to the full list of results, indexed by file.
	 * @return List of all the FileResults in the search
	 */
	public List<FileResult> getResults() {
		List<FileResult> ret = new LinkedList<FileResult>();
		for (DirectoryResult dir: directories) {
			for (FileResult file: dir.getFiles()) {
				ret.add(file);
			}
		}
		return ret;
	}
}
