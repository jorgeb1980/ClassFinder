package jars.search.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Implements a graphic (swing) directory filter
 */
class DirectoryFilter extends FileFilter {
	
	//--------------------------------------------------
	// Class methods 
	
	
	public DirectoryFilter(SearchPanel searchPanel) {
	}

	/**
	 * Implements the filter: will accept whatever is a directory
	 * @param f File to analyze
	 * @return true if the file is a directory
	 */
	public boolean accept(File f) {
		return f.isDirectory();
	}

	/**
	 * @return Description of the filter
	 */
	public String getDescription() {
		return "Directories";
	}
}
