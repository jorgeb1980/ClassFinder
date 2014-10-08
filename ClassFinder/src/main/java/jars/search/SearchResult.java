/**
 * 
 */
package jars.search;

import java.util.LinkedList;
import java.util.List;

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
	
	//------------------------------------------------------------
	// Methods of the class
	
	/** 
	 * Default visibility constructor (the class is only intended to be
	 * instantiated from the ResourceSearcher) 
	 * @param pattern Search pattern
	 * @param tmp Temporary form of the result 
	 */
//	SearchResult(String pattern, List<Map<String, List<String>>> tmp) {
//		this(pattern);
//	}
	
	/** 
	 * Default visibility constructor (the class is only intended to be
	 * instantiated from the ResourceSearcher) 
	 * @param pattern Search pattern 
	 */
	SearchResult(String pattern) {
		this.pattern = pattern;
		this.directories = new LinkedList<DirectoryResult>();
	}

	// Default visibility method in order to add directories to the search result
	//	only from classes inside this package
	void addDirectory(DirectoryResult directory) {
		directories.add(directory);
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
	public List<DirectoryResult> getDirectories() {
		return new LinkedList<DirectoryResult>(directories);
	}
	
	
}
