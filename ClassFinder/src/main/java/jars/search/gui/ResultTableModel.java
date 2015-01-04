package jars.search.gui;


import jars.search.core.DirectoryResult;
import jars.search.core.FileResult;
import jars.search.core.SearchResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * This class implements a data model for the table presenting the search
 * results.
 */
public class ResultTableModel extends AbstractTableModel {
	
	//----------------------------------------------------------
	// Class constants
	
	/**
	 * Eclipse generated for serialization
	 */
	private static final long serialVersionUID = 1636480537993041534L;
	
	//----------------------------------------------------------
	// Class properties
	
	// Model data
	private List<String[]> model = null;

	//----------------------------------------------------------
	// Class methods
	
	public ResultTableModel() {
		this.model = new ArrayList<String[]>();
	}

	/**
	 * Table Model constructor 
	 * @param searchResult SearchResult object with the contents of the search
	 */
	public ResultTableModel(SearchResult searchResult) {
		this();
		if (searchResult != null) {
			for (Iterator<DirectoryResult> it = searchResult.getResultDirectories().iterator(); it.hasNext();) {
				DirectoryResult dir = it.next();
				for (Iterator<FileResult> itFile = dir.getFiles().iterator(); itFile.hasNext();) {
					FileResult file = itFile.next();
					boolean first = true;
					for (String clazz: file.getResources()) {
						String fileColumn = "";
						if (first) {
							fileColumn = file.getPath();
							first = false;
						}
						this.model.add(new String[] { fileColumn, clazz });
					}
				}
			}
		}
	}

	/**
	 * The model will present 2 columns: file and resource name
	 * @return 2
	 */
	public int getColumnCount() {
		return 2;
	}

	/**
	 * @return The data model length
	 */
	public int getRowCount() {
		return this.model.size();
	}

	/**
	 * Returns the data in the given coordinates
	 * @param rowIndex Vertical coordinate (0 <= rowIndex < height)
	 * @parma columnIndex Horizontal coordinate (0 <= columnIndex < width)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return ((String[]) this.model.get(rowIndex))[columnIndex];
	}

	/**
	 * Returns the name of the column
	 * @param column Index of the column (0 <= column < width)
	 */
	public String getColumnName(int column) {
		String ret = "";
		if (column == 0) {
			ret = "File";
		} else if (column == 1) {
			ret = "Resource";
		}
		return ret;
	}
}
