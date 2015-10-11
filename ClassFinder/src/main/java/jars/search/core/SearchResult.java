/**
 * 
 */
package jars.search.core;

import java.io.File;
import java.util.Collections;
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
	 * @return Instance of a SearchResult object
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
	 * Search pattern used in the search that ended with this SearchResult.
	 * @return Search pattern used
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * Results of the search, expressed as a Map indexed by zip/jar file, with
	 * a List of Resources for each zip/jar file being in the reach of the search.
	 * @return Results of the search, indexed by File
	 */
	public Map<File, List<Resource>> getResourcesByFile() {
		return results;
	}
	
	/**
	 * Results of the search for a given File that was in the reach of the search.
	 * @param file File to search for in the search results.  If no result was found
	 * in the search, the method will return no Resources.  If the file is null,
	 * it will neither give any result.
	 * @return Results of the search for the specified File
	 */
	public List<Resource> getResourcesByFile(File f) {
		return results.get(f);
	}
	
	/**
	 * Results of the search, expressed as a List of Resources.  This method should
	 * be used in order to produce the full results of the search, ordered by file path and
	 * resource name.
	 * @return Results of the search
	 */
	public List<Resource> getResources() {
		List<Resource> ret = new LinkedList<Resource>();
		for (File f: results.keySet()) {
			ret.addAll(results.get(f));
		}
		// Sort by file path
		Collections.sort(ret);
		return ret;
	}

	/**
	 * Time taken to do the search that ended with this SearchResult.
	 * @return Search time in milliseconds
	 */
	public long getSearchTime() {
		return millis;
	}	
	
}
