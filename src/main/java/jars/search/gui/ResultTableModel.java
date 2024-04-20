package jars.search.gui;


import jars.search.core.I18n;
import jars.search.core.Resource;
import jars.search.core.SearchResult;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements a data model for the table presenting the search
 * results.
 */
class ResultTableModel extends AbstractTableModel {
	
	//----------------------------------------------------------
	// Class constants
	
	@Serial
	private static final long serialVersionUID = 1636480537993041534L;
	// Pattern for resource presentation
	// Parameters:
	// Name
	// Size
	// Internationalized string for 'bytes'
	// Compressed size
	// Internationalized string for 'compressed'
	private static final String HTML_RESOURCE_PATTERN = 
		"<html><b>{0}</b> - {1} {2} ({3} {4})</html>";
	
	//----------------------------------------------------------
	// Class properties
	
	// Model data
	private List<String[]> model = null;

	//----------------------------------------------------------
	// Class methods
	
	public ResultTableModel() {
		this.model = new ArrayList<>();
	}

	/**
	 * Table Model constructor 
	 * @param searchResult SearchResult object with the contents of the search
	 */
	public ResultTableModel(SearchResult searchResult) {
		this();
		if (searchResult != null) {
			var results = searchResult.getResourcesByFile();
			var files = new LinkedList<>(results.keySet());
			files.sort(new FileComparator());
			// Run through the File size ordered by canonical path
			for (var file: files) {
				// The first result of each file must show the file path in the
				//	first column
				boolean first = true;
				for (var resource: results.get(file)) {
					String fileColumn = "";
					if (first) {
						fileColumn = file.getPath();
						first = false;
					}
					this.model.add(new String[] { fileColumn, presentHTML(resource) });
				}
			}
		}
	}
	
	// Makes a proper html presentation of the resource name, its size and its
	//	compressed size
	private String presentHTML(Resource resource) {
		String ret = "";
		if (resource != null) {
			ret = MessageFormat.format(
				HTML_RESOURCE_PATTERN,
				resource.getName(),
				resource.getSize(),
				I18n.RESOURCES.getLabel("bytes"),
				resource.getCompressedSize(),
				I18n.RESOURCES.getLabel("compressed")
			);
		}
		return ret;
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
	 * @param columnIndex Horizontal coordinate (0 <= columnIndex < width)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return ((String[]) this.model.get(rowIndex))[columnIndex];
	}

	/**
	 * Returns the name of the column
	 * @param column Index of the column (0 <= column < width)
	 */
	public String getColumnName(int column) {
		var ret = "";
		if (column == 0) {
			ret = I18n.RESOURCES.getLabel("file");
		} else if (column == 1) {
			ret = I18n.RESOURCES.getLabel("resource");
		}
		return ret;
	}
	
	// File comparator
	private static class FileComparator implements Comparator<File> {
		// File canonical path
		private String canonicalPath(File file) {
			String ret = null;
			try {
				ret = file.getCanonicalPath();
			}
			catch (IOException ioe) {
				ret = "";
			}
			return ret;
		}
		
		@Override
		public int compare(File o1, File o2) {
			return canonicalPath(o1).compareTo(canonicalPath(o2));
		}
	}
}
