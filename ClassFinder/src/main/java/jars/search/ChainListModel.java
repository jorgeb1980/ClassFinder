package jars.search;

import javax.swing.AbstractListModel;

/**
 * Implements a list model with strings.
 */
public final class ChainListModel extends AbstractListModel<String> {
	
	//----------------------------------------------------------
	// Class constants
	
	/**
	 * Eclipse generated for serialization.
	 */
	private static final long serialVersionUID = -2081591990343054530L;
	
	//----------------------------------------------------------
	// Class properties
	
	private String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

	//----------------------------------------------------------
	// Class methods
	
	public ChainListModel(SearchPanel searchPanel) {
	}

	public int getSize() {
		return this.strings.length;
	}

	public String getElementAt(int i) {
		return this.strings[i];
	}
}
