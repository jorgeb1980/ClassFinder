package jars.search.core;

import java.io.File;
import java.io.IOException;

/**
 * This class contains the information of a resource stored into a zip/jar
 * file in the application classpath
 */
public class Resource implements Comparable<Resource>{

	//-------------------------------------------------------------------
	// Class properties
	
	/** Name of the resource */
	private String name;
	/** Size of the resource */
	private long size;
	/** Compressed size of the resource */
	private long compressedSize;
	/** File attached to the resource. */
	private File file;
	
	//-------------------------------------------------------------------
	// Class methods
	
	@Override
	public String toString() {
		return name + " [" + size + "]";
	};
	
	/**
	 * Default visibility constructor.  Builds an immutable Resource instance.
	 * @param name Resource name
	 * @param size Resource size in bytes
	 * @param compressedSize Compressed size of the resource, in bytes
	 * @param file Reference to the file this resource comes from
	 */
	Resource(String name, long size, long compressedSize, File file) {
		super();
		this.name = name;
		this.size = size;
		this.compressedSize = compressedSize;
		this.file = file;
	}
	/**
	 * Name of the Resource inside the zip/jar original file.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Size in bytes of the original Resource.
	 * @return the size
	 */
	public long getSize() {
		return size;
	}
	/**
	 * Compressed size of the Resource inside the zip/jar file.
	 * @return the compressedSize
	 */
	public long getCompressedSize() {
		return compressedSize;
	}

	/**
	 * Reference to the zip/jar file containing the Resource
	 * @return the file
	 */
	public File getContainerFile() {
		return file;
	}
	
	/**
	 * Compares a Resource to another one, by comparing each Resource's respective
	 * canonical path of the containing zip/jar file, and then its name.
	 * @param o Resource to compare with
	 */
	public int compareTo(Resource o) {
		int ret = 0;
		try {
			ret = this.file.getCanonicalPath().compareTo(o.file.getCanonicalPath());
			if (ret == 0) {
				ret = this.name.compareTo(o.name);
			}
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return ret;
	}
}
