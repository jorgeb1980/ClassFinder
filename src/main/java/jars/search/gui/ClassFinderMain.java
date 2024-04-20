package jars.search.gui;

import jars.search.core.I18n;

import javax.swing.*;

/**
 * Implements the main method and entry point for the GUI Swing application.
 */
public class ClassFinderMain {
	/**
	 * Entry point for the application.
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		var principal = new JFrame(I18n.RESOURCES.getLabel("app.header"));

		var panel = new SearchPanel();
		principal.setContentPane(panel);
		principal.pack();
		principal.setDefaultCloseOperation(3);

		principal.setLocationRelativeTo(null);

		principal.setVisible(true);
	}
}
