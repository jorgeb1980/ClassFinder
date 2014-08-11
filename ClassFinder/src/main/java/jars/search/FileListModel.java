package jars.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractListModel;

public class FileListModel extends AbstractListModel<String> {
	/**
	 * Eclipse generated for serialization
	 */
	private static final long serialVersionUID = 6560570616700652378L;
	private List<String> model = null;

	public FileListModel() {
		this.model = new ArrayList<String>();
	}

	public String getElementAt(int index) {
		return this.model.get(index);
	}

	public int getSize() {
		return this.model.size();
	}

	public void addEntry(String directory) {
		this.model.add(directory);
		Collections.sort(this.model);
		fireContentsChanged(this, 0, this.model.size());
	}

	public void removeEntry(int índice) {
		this.model.remove(índice);
		fireContentsChanged(this, 0, this.model.size());
	}
}
