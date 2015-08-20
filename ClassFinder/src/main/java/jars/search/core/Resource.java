package jars.search.core;

/**
 * This class contains the information of a resource stored into a zip/jar
 * file in the application classpath
 */
public class Resource {

	//-------------------------------------------------------------------
	// Class properties
	
	/** Name of the resource */
	private String name;
	/** Size of the resource */
	private long size;
	/** Compressed size of the resource */
	private long compressedSize;
	
	//-------------------------------------------------------------------
	// Class methods
	
	/**
	 * @param name Resource name
	 * @param size Resource size in bytes
	 * @param compressedSize Compressed size of the resource, in bytes
	 */
	public Resource(String name, long size, long compressedSize) {
		super();
		this.name = name;
		this.size = size;
		this.compressedSize = compressedSize;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}
	/**
	 * @return the compressedSize
	 */
	public long getCompressedSize() {
		return compressedSize;
	}
	
	
}
