package jars.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class DirectoryListener implements ActionListener {

	private SearchPanel panel;

	public DirectoryListener(SearchPanel searchPanel) {
		panel = searchPanel;
	}

	public void actionPerformed(ActionEvent evt) {
		panel.buttonAddDirectory(evt);
	}
}
