package jars.search;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassSearcher {
	
	private static final String[] ENDINGS = { ".jar", ".zip", ".ear",
			".war" };

	private Map<String, Map<String, List<String>>> cache = null;
	private static ClassSearcher theSearcher = null;
	private FileFilter zipJarFilter = null;

	private ClassSearcher() {
		this.cache = new HashMap<String, Map<String, List<String>>>();
		this.zipJarFilter = new JarZipFilter();
	}

	public static ClassSearcher getClassSearcher() {
		if (theSearcher == null) {
			theSearcher = new ClassSearcher();
		}
		return theSearcher;
	}

	public List<Map<String, List<String>>> searchClasses(
			List<String> directories, String pattern) {
		addDirectoriesToCache(directories);
		return searchInCache(directories, pattern.toUpperCase());
	}

	private void addDirectoriesToCache(List<String> directories) {
		for (String directory : directories) {
			if (!this.cache.containsKey(directory)) {
				this.cache.put(directory, readJarsFromDirectory(directory));
			}
		}
	}

	private Map<String, List<String>> searchInDirectoryCache(
			String directory, String pattern) {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		Map<String, List<String>> jarFiles;
		if (this.cache.containsKey(directory)) {
			jarFiles = this.cache.get(directory);
			for (Iterator<String> it = jarFiles.keySet().iterator(); it
					.hasNext();) {
				String jarFile = (String) it.next();

				List<String> classFiles = jarFiles.get(jarFile);
				for (String classFile : classFiles) {
					if (classFile.toUpperCase().contains(pattern)) {
						List<String> theList = ret.get(jarFile);
						if (theList == null) {
							theList = new LinkedList<String>();
							ret.put(jarFile, theList);
						}
						theList.add(classFile);
					}
				}
			}
		}
		return ret;
	}

	private Map<String, List<String>> readJarsFromDirectory(String directory) {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		if (directory != null) {
			File dirFile = new File(directory);
			if ((dirFile.exists()) && (dirFile.isDirectory())) {
				File[] files = dirFile.listFiles(this.zipJarFilter);
				for (File fichero : files) {
					String filePath = fichero.getPath();
					List<String> list = getClasses(fichero);
					ret.put(filePath, list);
				}
			}
		}
		return ret;
	}

	private List<Map<String, List<String>>> searchInCache(
			List<String> directories, String pattern) {
		List<Map<String, List<String>>> ret = new LinkedList<Map<String, List<String>>>();
		for (String directory : directories) {
			Map<String, List<String>> table = searchInDirectoryCache(
					directory, pattern);
			if ((table != null) && (table.size() > 0)) {
				ret.add(table);
			}
		}
		return ret;
	}

	private List<String> getClasses(File file) {
		List<String> ret = new LinkedList<String>();
		try {
			JarFile jf = new JarFile(file);
			Enumeration<JarEntry> entries = jf.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = (JarEntry) entries.nextElement();
				if (!entry.isDirectory()) {
					ret.add(entry.getName());
				}
			}
			jf.close();
		} catch (IOException ioe) {
			System.err.println("---> Error processing " + file.getPath());
			ioe.printStackTrace();
		}
		return ret;
	}

	private class JarZipFilter implements FileFilter {
		private JarZipFilter() {
		}

		public boolean accept(File pathname) {
			String fileName = pathname.getName();
			boolean accept = false;
			int i = 0;
			int length = ClassSearcher.ENDINGS.length;
			while ((!accept) && (i < length)) {
				accept = fileName
						.endsWith(ClassSearcher.ENDINGS[(i++)]);
			}
			return accept;
		}
	}
}
