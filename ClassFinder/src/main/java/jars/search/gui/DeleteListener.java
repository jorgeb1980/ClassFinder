package jars.search.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class implements an action listener for the Delete button
 */
final class DeleteListener implements ActionListener {
	
	//--------------------------------------------------
	// Class properties 
	
	// Reference to the search panel
	private SearchPanel panel;

	//--------------------------------------------------
	// Class methods 
	
	public DeleteListener(SearchPanel searchPanel) {
		panel = searchPanel;
	}

	public void actionPerformed(ActionEvent evt) {
		panel.buttonDeleteDirectory(evt);
	}
}