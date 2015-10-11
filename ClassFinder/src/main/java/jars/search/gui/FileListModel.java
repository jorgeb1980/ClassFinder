package jars.search.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * Implements a model for the directories list.
 */
class FileListModel extends AbstractListModel {
	
	//----------------------------------------------
	// Class constants
	
	/**
	 * Eclipse generated for serialization
	 */
	private static final long serialVersionUID = 6560570616700652378L;
	
	//----------------------------------------------
	// Class properties
	
	// Data for the model
	private List<String> model = null;

	//----------------------------------------------
	// Class methods
	
	/**
	 * Builds a ListModel to populate the File list (this is used to define
	 * the reach of the search)
	 */
	public FileListModel() {
		this.model = new ArrayList<String>();
	}

	/**
	 * Gets the element in the specified index of the list.
	 * @param index 0 <= index < length of the list
	 * @return Element at the specified index
	 */
	public String getElementAt(int index) {
		return this.model.get(index);
	}

	/**
	 * Returns the length of the list
	 * @return Length of the list
	 */
	public int getSize() {
		return this.model.size();
	}

	/**
	 * Adds an entry to the model	
	 * @param directory New directory to appear in the model
	 */
	public void addEntry(String directory) {
		this.model.add(directory);
		Collections.sort(this.model);
		fireContentsChanged(this, 0, this.model.size());
	}

	/**
	 * Removes an entry from the model
	 * @param index Index of the entry (0 <= index < model length) 
	 */
	public void removeEntry(int index) {
		this.model.remove(index);
		fireContentsChanged(this, 0, this.model.size());
	}
}
