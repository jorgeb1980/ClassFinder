/**
 * 
 */
package jars.search;

import java.util.LinkedList;
import java.util.List;

/**
 * This bean contains the Files with resources corresponding to a search
 * inside some Directory.
 */
public class DirectoryResult {

	//---------------------------------------------------------------
	// Properties of the class
	
	/** Full path of the directory. */
	private String path;
	/** List of files inside the directory with results for the search. */
	private List<FileResult> files;
	
	//---------------------------------------------------------------
	// Methods of the class
	
	// Default visibility constructor
	DirectoryResult(String thePath) {
		path = thePath;
		files = new LinkedList<FileResult>();
	}
	
	// Default visibility: files are added unseen to the exterior of
	//	the class
	void addFile(FileResult file) {
		if (file != null) {
			files.add(file);
		}
	}
	
	/**
	 * Getter method for the Files inside a Directory.  
	 * @return Defensive copy of the Files list
	 */
	public List<FileResult> getFiles() {
		return new LinkedList<FileResult>(files);
	}
	
	/**
	 * Returns the path of the Directory
	 * @return Full path in canonical form for the Directory
	 */
	public String getPath() {
		return path;
	}
}
