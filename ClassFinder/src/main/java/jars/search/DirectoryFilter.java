package jars.search;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class DirectoryFilter extends FileFilter {
	public DirectoryFilter(SearchPanel searchPanel) {
	}

	public boolean accept(File f) {
		return f.isDirectory();
	}

	public String getDescription() {
		return "Directories";
	}
}
