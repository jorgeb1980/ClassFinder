package jars.search.gui.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * This class implements a model for the directories list.
 */
public class FileListModel extends AbstractListModel {
	
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
	
	public FileListModel() {
		this.model = new ArrayList<String>();
	}

	public String getElementAt(int index) {
		return this.model.get(index);
	}

	public int getSize() {
		return this.model.size();
	}

	/**
	 * This method adds an entry to the model	
	 * @param directory New directory to appear in the model
	 */
	public void addEntry(String directory) {
		this.model.add(directory);
		Collections.sort(this.model);
		fireContentsChanged(this, 0, this.model.size());
	}

	/**
	 * This method removes an entry from the model
	 * @param index Index of the entry (0 <= index < model length) 
	 */
	public void removeEntry(int index) {
		this.model.remove(index);
		fireContentsChanged(this, 0, this.model.size());
	}
}
