package jars.search;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Main {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame principal = new JFrame("Class Finder App");

		JPanel panel = new SearchPanel();
		principal.setContentPane(panel);
		principal.setSize(panel.getPreferredSize());
		principal.setDefaultCloseOperation(3);

		principal.setLocationRelativeTo(null);

		principal.setVisible(true);
	}
}
