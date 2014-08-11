package jars.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class DeleteListener implements ActionListener {
	private SearchPanel panel;

	public DeleteListener(SearchPanel searchPanel) {
		panel = searchPanel;
	}

	public void actionPerformed(ActionEvent evt) {
		panel.buttonDeleteDirectory(evt);
	}
}
