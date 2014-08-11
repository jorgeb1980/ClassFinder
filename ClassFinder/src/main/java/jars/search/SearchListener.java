package jars.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class SearchListener implements ActionListener {

	private SearchPanel panel;

	public SearchListener(SearchPanel searchPanel) {
		panel = searchPanel;
	}

	public void actionPerformed(ActionEvent evt) {

		panel.buttonSearch(evt);
	}
}
