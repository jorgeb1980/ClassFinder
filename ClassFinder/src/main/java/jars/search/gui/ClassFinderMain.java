package jars.search.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class ClassFinderMain {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame principal = new JFrame(I18n.RESOURCES.getLabel("app.header"));

		JPanel panel = new SearchPanel();
		principal.setContentPane(panel);
		principal.setSize(panel.getPreferredSize());
		principal.setDefaultCloseOperation(3);

		principal.setLocationRelativeTo(null);

		principal.setVisible(true);
	}
}
