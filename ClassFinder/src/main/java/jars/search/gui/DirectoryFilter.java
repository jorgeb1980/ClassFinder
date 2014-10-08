package jars.search.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * This class implements a graphic (swing) directory filter
 */
public class DirectoryFilter extends FileFilter {
	
	//--------------------------------------------------
	// Class methods 
	
	
	public DirectoryFilter(SearchPanel searchPanel) {
	}

	/**
	 * This methods implements the filter: will accept whatever is a directory
	 * @param f File to analyce
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
