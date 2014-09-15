package jars.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class implements an action listener for the "Add directory" button
 */
final class DirectoryListener implements ActionListener {

	//----------------------------------------------
	// Class properties
	
	// Reference to the search panel
	private SearchPanel panel;
	
	//----------------------------------------------
	// Class methods
	
	public DirectoryListener(SearchPanel searchPanel) {
		panel = searchPanel;
	}

	public void actionPerformed(ActionEvent evt) {
		panel.buttonAddDirectory(evt);
	}
}
