package jars.search.gui;

import jars.search.core.I18n;
import jars.search.core.Properties;
import jars.search.core.ResourceSearcher;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.Serial;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class SearchPanel extends JPanel {
	@Serial
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
	private JLabel versionLabel;
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
		searchPatternText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// Capture enter key
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					SearchPanel.this.buttonSearch(null);
				}
			}
		});
		this.jLabel6 = new JLabel();

		this.jLabel1.setText(I18n.RESOURCES.getLabel("query.execution.time"));

		this.jLabel2.setText(I18n.RESOURCES.getLabel("directories.list"));

		this.jScrollPane1.setViewportView(this.directoriesList);

		this.jButton1.setText("...");
		this.jButton1.setPreferredSize(new Dimension(23, 23));
		this.jButton1.addActionListener(new DirectoryListener(this));

		this.jLabel3.setText(I18n.RESOURCES.getLabel("add.directory"));

		this.jLabel4.setText(I18n.RESOURCES.getLabel("query.results"));

		this.resultsTable.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null, null }, { null, null, null, null },
				{ null, null, null, null }, { null, null, null, null } },
				new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));

		this.jScrollPane2.setViewportView(this.resultsTable);

		this.jButton2.setText(I18n.RESOURCES.getLabel("search"));
		this.jButton2.addActionListener(new SearchListener(this));

		this.jButton3.setText("X");
		this.jButton3.setPreferredSize(new Dimension(25, 23));
		this.jButton3.addActionListener(new DeleteListener(this));

		this.jLabel5.setText(I18n.RESOURCES.getLabel("remove.directory"));

		this.searchPatternText.setPreferredSize(new Dimension(260, 20));

		this.jLabel6.setText(I18n.RESOURCES.getLabel("search.pattern"));
		
		versionLabel = new JLabel("");
		versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		versionLabel.setText(Properties.INSTANCE.get("classfinder.version"));

		GroupLayout layout = new GroupLayout(this);
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addGroup(layout.createSequentialGroup()
							.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(layout.createSequentialGroup()
							.addComponent(jLabel2)
							.addPreferredGap(ComponentPlacement.RELATED, 461, Short.MAX_VALUE)
							.addComponent(jLabel3)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18))
						.addGroup(layout.createSequentialGroup()
							.addComponent(jLabel4)
							.addContainerGap(581, Short.MAX_VALUE))
						.addGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup(Alignment.TRAILING)
								.addComponent(jScrollPane2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
								.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 745, Short.MAX_VALUE))
							.addContainerGap())
						.addGroup(layout.createSequentialGroup()
							.addComponent(jLabel6)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(searchPatternText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(jButton2)
							.addContainerGap())
						.addGroup(layout.createSequentialGroup()
							.addComponent(jLabel5)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(layout.createSequentialGroup()
							.addGap(376)
							.addComponent(jLabel1)
							.addGap(5)
							.addComponent(lastQueryTimeLabel)
							.addGap(75))
						.addGroup(layout.createSequentialGroup()
							.addComponent(versionLabel, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel2)
						.addComponent(jLabel3)
						.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jButton3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel5))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jLabel4)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jButton2)
						.addComponent(searchPatternText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel6))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel1)
						.addComponent(lastQueryTimeLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(versionLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		setLayout(layout);
	}

	/** 
	 * Listener to the search button press.  Will launch the resource searcher
	 * with the intended pattern and directories list. 
	 * @param evt Button push event
	 */
	void buttonSearch(ActionEvent evt) {
		var pattern = this.searchPatternText.getText();
		if ((pattern != null) && (!pattern.trim().isEmpty())) {
			var model = this.directoriesList.getModel();
			var listSize = model.getSize();
			if (listSize > 0) {
				List<File> directories = new LinkedList<File>();
				for (var i = 0; i < listSize; i++) {
					directories.add(new File((String) model.getElementAt(i)));
				}
				var start = new Date().getTime();
				try {
					var ret = ResourceSearcher.SEARCHER.search(directories, pattern.trim());
					var total = new Date().getTime() - start;
					this.lastQueryTimeLabel.setText(Long.toString(total) + " msec.");
					this.resultsTable.setModel(new ResultTableModel(ret));
				}
				catch(Exception pe) {
					JOptionPane.showMessageDialog(SearchPanel.this, pe.getMessage());
				}
			}
		}
	}

	/**
	 * Listener to the delete directory button.  Will erase a directory from
	 * the directories list.
	 * @param evt Button push event
	 */
	void buttonDeleteDirectory(ActionEvent evt) {
		var index = this.directoriesList.getSelectedIndex();
		if (index >= 0) ((FileListModel) this.directoriesList.getModel()).removeEntry(index);
	}

	/**
	 * Listener to the add directory button.  Will add a directory to the 
	 * directories list
	 * @param evt Button push event
	 */
	void buttonAddDirectory(ActionEvent evt) {
		int result = this.filesDialog.showOpenDialog(this);
		if (result == 0) ((FileListModel) this.directoriesList.getModel()).addEntry(this.filesDialog.getSelectedFile().getPath());
	}

	/**
	 * Implements an action listener for the Delete button
	 */
	static final class DeleteListener implements ActionListener {
		
		//--------------------------------------------------
		// Class properties 
		
		// Reference to the search panel
		private SearchPanel panel;

		//--------------------------------------------------
		// Class methods 
		
		/**
		 * Builds a listener bound to a certain search panel. 
		 * @param searchPanel Panel to be bound to the listener.
		 */
		public DeleteListener(SearchPanel searchPanel) {
			panel = searchPanel;
		}

		/**
		 * Answers to a GUI event.  In this case, it deletes an entry of the
		 * directories list.
		 * @param evt Event to be answered.
		 */
		public void actionPerformed(ActionEvent evt) {
			panel.buttonDeleteDirectory(evt);
		}
	}
	
	/**
	 * Implements an action listener for the "Search" button.
	 */
	final class SearchListener implements ActionListener {
		
		//-------------------------------------------------------
		// Class properties 
		
		// Reference to the search panel
		private SearchPanel panel;

		//-------------------------------------------------------
		// Class properties 
		
		/**
		 * Builds a listener bound to a certain search panel. 
		 * @param searchPanel Panel to be bound to the listener.
		 */
		public SearchListener(SearchPanel searchPanel) {
			panel = searchPanel;
		}
		
		/** 
		 * Answers to a GUI event.  In this case, it fires the search.
		 * @param evt Event to be answered.
		 */
		public void actionPerformed(ActionEvent evt) {
			panel.buttonSearch(evt);
		}
	}
	
	/**
	 * Implements an action listener for the "Add directory" button
	 */
	static final class DirectoryListener implements ActionListener {

		//----------------------------------------------
		// Class properties
		
		// Reference to the search panel
		private SearchPanel panel;
		
		//----------------------------------------------
		// Class methods
		
		/**
		 * Builds a listener bound to a certain search panel. 
		 * @param searchPanel Panel to be bound to the listener.
		 */
		public DirectoryListener(SearchPanel searchPanel) {
			panel = searchPanel;
		}

		/** 
		 * Answers to a GUI event.  In this case, it adds a directory to the
		 * search directories list.
		 * @param evt Event to be answered.
		 */
		public void actionPerformed(ActionEvent evt) {
			panel.buttonAddDirectory(evt);
		}
	}
}
