/**
 * 
 */
package jars.search.core;

import java.util.LinkedList;
import java.util.List;

/**
 * This bean contains the resources found in some File inside a Directory
 */
public class FileResult {

	//---------------------------------------------------------------
	// Properties of the class
	
	/** Full path of the file. */
	private String path;
	/** Resources found inside the file. */
	private List<Resource> resources;
	
	//---------------------------------------------------------------
	// Methods of the class

	// Default visibility constructor
	FileResult(String thePath) {
		path = thePath;
		resources = new LinkedList<Resource>();
	}
	
	// Default visibility: resources are added unseen to the exterior of
	//	the class
	void addResource(Resource resource) {
		resources.add(resource);
	}
	
	/**
	 * Getter method for the results inside a File.  
	 * @return Defensive copy of the resources list
	 */
	public List<Resource> getResources() {
		return new LinkedList<Resource>(resources);
	}
	
	/**
	 * Returns the path of the File
	 * @return Full path in canonical form for the File
	 */
	public String getPath() {
		return path;
	}
}
