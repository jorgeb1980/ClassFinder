/**
 * 
 */
package jars.search.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains the result of a search of resources in a repository
 * of directories.
 */
public class SearchResult {
	
	//------------------------------------------------------------
	// Properties of the class
	
	/** Results indexed by File.  The index is the compressed file containing
	 * the resources; the Resources are compressed into the File and follow
	 * the requested pattern */
	private Map<File, List<Resource>> results = null;
	/** Search time in milliseconds */
	private long millis;
	/** Search pattern. */
	private String pattern;
	
	
	//------------------------------------------------------------
	// Methods of the class
	
	/**
	 * Static factory method used to instantiate a result.
	 * @param pattern Search pattern
	 * @param millis Search time in milliseconds
	 * @param results List of results, indexed by File, containing the resources
	 * found inside the File
	 * @return
	 */
	static SearchResult createResult(String pattern, long millis, Map<File, List<Resource>> results) {
		return new SearchResult(pattern, millis, results);
	}
	
	/** 
	 * Private visibility constructor (the class is only intended to be
	 * instantiated from the ResourceSearcher) 
	 * @param pattern Search pattern
	 * @param millis Search time in milliseconds
	 * @param results List of results, indexed by File, containing the resources
	 * found inside the File
	 */
	private SearchResult(String pattern, long millis, Map<File, List<Resource>> results) {
		this.pattern = pattern;
		this.millis = millis;
		this.results = results;
	}	

	/**
	 * @return Search pattern used
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @return Results of the search, indexed by File
	 */
	public Map<File, List<Resource>> getResults() {
		return results;
	}

	/**
	 * @return Search time in milliseconds
	 */
	public long getSearchTime() {
		return millis;
	}	
	
}
