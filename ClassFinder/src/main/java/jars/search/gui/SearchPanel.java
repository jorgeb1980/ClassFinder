package jars.search.gui;

import jars.search.ResourceSearcher;
import jars.search.SearchResult;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

public class SearchPanel extends JPanel {
	/**
	 * Eclipse generated for serialization
	 */
	private static final long serialVersionUID = -6396120998133277126L;
	private JFileChooser filesDialog = null;
	private FileFilter directoriesFilter = null;
	private JLabel lastQueryTimeLabel;
	private JButton jButton1;
	private JButton jButton2;
	private JButton jButton3;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JSeparator jSeparator1;
	private JList<String> directoriesList;
	private JTable resultsTable;
	private JTextField searchPatternText;

	public SearchPanel() {
		initComponents();
		this.directoriesList.setModel(new FileListModel());
		this.resultsTable.setModel(new ResultTableModel());
		this.filesDialog = new JFileChooser();
		this.directoriesFilter = new DirectoryFilter(this);
		this.filesDialog.setFileFilter(this.directoriesFilter);
		this.filesDialog.setMultiSelectionEnabled(false);
		this.filesDialog.setFileSelectionMode(1);
	}

	private void initComponents() {
		this.jLabel1 = new JLabel();
		this.lastQueryTimeLabel = new JLabel();
		this.jLabel2 = new JLabel();
		this.jScrollPane1 = new JScrollPane();
		this.directoriesList = new JList<String>();
		this.jButton1 = new JButton();
		this.jLabel3 = new JLabel();
		this.jSeparator1 = new JSeparator();
		this.jLabel4 = new JLabel();
		this.jScrollPane2 = new JScrollPane();
		this.resultsTable = new JTable();
		this.jButton2 = new JButton();
		this.jButton3 = new JButton();
		this.jLabel5 = new JLabel();
		this.searchPatternText = new JTextField();
		this.jLabel6 = new JLabel();

		this.jLabel1.setText("Last query execution time:");

		this.jLabel2.setText("Directories list");

		this.directoriesList.setModel(new ChainListModel(this));

		this.jScrollPane1.setViewportView(this.directoriesList);

		this.jButton1.setText("...");
		this.jButton1.setPreferredSize(new Dimension(23, 23));
		this.jButton1.addActionListener(new DirectoryListener(this));

		this.jLabel3.setText("Add directory");

		this.jLabel4.setText("Query results");

		this.resultsTable.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null, null }, { null, null, null, null },
				{ null, null, null, null }, { null, null, null, null } },
				new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));

		this.jScrollPane2.setViewportView(this.resultsTable);

		this.jButton2.setText("Search");
		this.jButton2.addActionListener(new SearchListener(this));

		this.jButton3.setText("X");
		this.jButton3.setPreferredSize(new Dimension(25, 23));
		this.jButton3.addActionListener(new DeleteListener(this));

		this.jLabel5.setText("Remove directory");

		this.searchPatternText.setPreferredSize(new Dimension(260, 20));

		this.jLabel6.setText("Search pattern:");

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		// Código autogenerado por netbeans para el layout de la página...
		// buffff
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createParallelGroup(
																GroupLayout.Alignment.LEADING)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createParallelGroup(
																								GroupLayout.Alignment.LEADING)
																								.addGroup(
																										layout.createSequentialGroup()
																												.addComponent(
																														this.jScrollPane1,
																														-1,
																														473,
																														32767)
																												.addContainerGap())
																								.addGroup(
																										GroupLayout.Alignment.TRAILING,
																										layout.createSequentialGroup()
																												.addComponent(
																														this.jLabel2)
																												.addPreferredGap(
																														LayoutStyle.ComponentPlacement.RELATED,
																														269,
																														32767)
																												.addComponent(
																														this.jLabel3)
																												.addPreferredGap(
																														LayoutStyle.ComponentPlacement.RELATED)
																												.addComponent(
																														this.jButton1,
																														-2,
																														-1,
																														-2)
																												.addGap(18,
																														18,
																														18))
																								.addGroup(
																										layout.createSequentialGroup()
																												.addComponent(
																														this.jLabel4)
																												.addContainerGap(
																														361,
																														32767))
																								.addGroup(
																										GroupLayout.Alignment.TRAILING,
																										layout.createSequentialGroup()
																												.addGroup(
																														layout.createParallelGroup(
																																GroupLayout.Alignment.TRAILING)
																																.addComponent(
																																		this.jScrollPane2,
																																		GroupLayout.Alignment.LEADING,
																																		-1,
																																		473,
																																		32767)
																																.addComponent(
																																		this.jSeparator1,
																																		-1,
																																		473,
																																		32767))
																												.addContainerGap()))
																				.addGroup(
																						GroupLayout.Alignment.TRAILING,
																						layout.createSequentialGroup()
																								.addComponent(
																										this.jLabel6)
																								.addPreferredGap(
																										LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(
																										this.searchPatternText,
																										-2,
																										-1,
																										-2)
																								.addGap(18,
																										18,
																										18)
																								.addComponent(
																										this.jButton2)
																								.addContainerGap()))
																.addGroup(
																		GroupLayout.Alignment.TRAILING,
																		layout.createSequentialGroup()
																				.addComponent(
																						this.jLabel5)
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						this.jButton3,
																						-2,
																						39,
																						-2)
																				.addContainerGap()))
												.addGroup(
														GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(
																		this.jLabel1)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		this.lastQueryTimeLabel)
																.addGap(81, 81,
																		81)))));

		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(this.jLabel2)
												.addComponent(this.jLabel3)
												.addComponent(this.jButton1,
														-2, -1, -2))
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(this.jScrollPane1, -2, 114, -2)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(this.jButton3,
														-2, -1, -2)
												.addComponent(this.jLabel5))
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(this.jSeparator1, -2, -1, -2)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(this.jLabel4)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(this.jScrollPane2, -1, 100, 32767)
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(this.jButton2)
												.addComponent(
														this.searchPatternText,
														-2, -1, -2)
												.addComponent(this.jLabel6))
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(this.jLabel1)
												.addComponent(
														this.lastQueryTimeLabel))
								.addContainerGap()));
	}

	/** 
	 * Listener to the search button press.  Will launch the resource searcher
	 * with the intended pattern and directories list. 
	 * @param evt Button push event
	 */
	void buttonSearch(ActionEvent evt) {
		String pattern = this.searchPatternText.getText();
		if ((pattern != null) && (pattern.trim().length() > 0)) {
			ListModel<String> model = this.directoriesList.getModel();
			int listSize = model.getSize();
			if (listSize > 0) {
				List<String> directories = new LinkedList<String>();
				for (int i = 0; i < listSize; i++) {
					directories.add((String) model.getElementAt(i));
				}
				long start = new Date().getTime();

				SearchResult ret = ResourceSearcher
						.getClassSearcher().search(directories,
								pattern.trim());

				long total = new Date().getTime() - start;
				this.lastQueryTimeLabel.setText(Long.toString(total) + " msec.");
				this.resultsTable.setModel(new ResultTableModel(ret));
			}
		}
	}

	/**
	 * Listener to the delete directory button.  Will erase a directory from
	 * the directories list.
	 * @param evt Button push event
	 */
	void buttonDeleteDirectory(ActionEvent evt) {
		int index = this.directoriesList.getSelectedIndex();
		if (index >= 0) {
			((FileListModel) this.directoriesList.getModel())
					.removeEntry(index);
		}
	}

	/**
	 * Listener to the add directory button.  Will add a directory to the 
	 * directories list
	 * @param evt Button push event
	 */
	void buttonAddDirectory(ActionEvent evt) {
		int result = this.filesDialog.showOpenDialog(this);
		switch (result) {
		case 0:
			((FileListModel) this.directoriesList.getModel())
					.addEntry(this.filesDialog.getSelectedFile()
							.getPath());

			break;
		}
	}
}
