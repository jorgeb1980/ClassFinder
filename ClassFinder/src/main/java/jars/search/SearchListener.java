package jars.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class implements an action listener for the "Search" button.
 */
final class SearchListener implements ActionListener {
	
	//-------------------------------------------------------
	// Class properties 
	
	// Reference to the search panel
	private SearchPanel panel;

	//-------------------------------------------------------
	// Class properties 
	
	public SearchListener(SearchPanel searchPanel) {
		panel = searchPanel;
	}

	public void actionPerformed(ActionEvent evt) {

		panel.buttonSearch(evt);
	}
}
