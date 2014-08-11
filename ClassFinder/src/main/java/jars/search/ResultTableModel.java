package jars.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class ResultTableModel extends AbstractTableModel {
	/**
	 * Eclipse generated for serialization
	 */
	private static final long serialVersionUID = 1636480537993041534L;
	private List<String[]> model = null;

	public ResultTableModel() {
		this.model = new ArrayList<String[]>();
	}

	public ResultTableModel(
			List<Map<String, List<String>>> searchResult) {
		this();
		if (searchResult != null) {
			for (Iterator<Map<String, List<String>>> it = searchResult
					.iterator(); it.hasNext();) {
				Map<String, List<String>> table = it.next();
				for (Iterator<String> itKey = table.keySet().iterator(); itKey
						.hasNext();) {
					String jarFile = (String) itKey.next();
					boolean first = true;
					for (String clazz : table.get(jarFile)) {
						String fileColumn = "";
						if (first) {
							fileColumn = jarFile;
							first = false;
						}
						this.model.add(new String[] { fileColumn, clazz });
					}
				}
			}
		}
	}

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return this.model.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return ((String[]) this.model.get(rowIndex))[columnIndex];
	}

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
