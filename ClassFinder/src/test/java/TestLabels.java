import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import jars.search.gui.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.Test;


public class TestLabels {

	@Test
	public void testDefault() {
		assertEquals(Resources.RESOURCES.getLabel("query.execution.time"), 
					 Resources.RESOURCES.getLabel(Locale.getDefault(), "query.execution.time"));		
	}
	
	@Test
	public void testEnglish() {
		assertEquals(Resources.RESOURCES.getLabel(new Locale("en"), "query.execution.time"), "Last query execution time:");		
	}
	
	@Test
	public void testFrench() {
		assertEquals(Resources.RESOURCES.getLabel(new Locale("fr"), "query.execution.time"), "Temps d'exécution de la dernière recherche:");		
	}

	@Test
	public void testSpanish() {
		assertEquals(Resources.RESOURCES.getLabel(new Locale("es"), "query.execution.time"), "Tiempo de ejecución de la última consulta:");		
	}
	
	@Test
	public void testSameResources() {
		try {
			// Test that there is no difference between the resources in any language
			String base = Resources.LABELS_NAME;
			// This makes the assumption that it will be ran from the sources (not from a jar)
			ClassLoader cl = TestLabels.class.getClassLoader();
			Enumeration<URL> resources = cl.getResources(base.substring(0, base.lastIndexOf('.')).replace('.', '/'));
			if (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				File i18nDir = new File(url.toURI());
				File files[] = i18nDir.listFiles();
				// Filter the properties files
				List<File> resourceFiles = new ArrayList<File>();
				String labelFilePrefix = base.substring(base.lastIndexOf('.') + 1);
				for (File file: files) {
					if (file.isFile() && file.getName().startsWith(labelFilePrefix)) {
						resourceFiles.add(file);
					}
				}
				// List the files
				System.out.println("Comparing the next resource bundle files:");
				
				for (File f: resourceFiles) {
					System.out.println(f.getCanonicalPath());
				}
				// Compare every .properties file inside the i18n directory
				List<String> properties = new LinkedList<String>();
				// The first one is the reference
				loadProperties(resourceFiles.get(0), properties);
				// Compare the rest
				for (int i = 1; i < resourceFiles.size(); i++) {
					compareProperties(resourceFiles.get(i), properties);
				}
			} 
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	// Compares the properties declared in the file (does something like a diff)
	private void compareProperties(File file, List<String> properties) {
		List<String> plus = new LinkedList<String>();
		List<String> less = new LinkedList<String>();
		InputStream is = null;
		try {
			Properties prop = new Properties();
			is = new FileInputStream(file);
			prop.load(is);
			for (Object key: prop.keySet()) {
				if (!properties.contains(key)) {
					plus.add((String) key);
				}
			}
			for (String property: properties) {
				if (!prop.containsKey(property)) {
					less.add(property);
				}
			}
		}		
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		finally {
			if (is != null) {
				try {
					is.close();
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
					fail();
				}
			}
		}
		if (plus.size() > 0 || less.size() > 0) {
			fail(reportDiff(file, plus, less));
		}
	}

	// Reports the differences between the properties files
	private String reportDiff(File file, List<String> plus, List<String> less) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Differences in ");
			sb.append(file.getCanonicalPath());
			sb.append(System.getProperty("line.separator"));
			if (plus.size() > 0) {
				for (String p: plus) {
					sb.append("+ ");
					sb.append(p);
					sb.append(System.getProperty("line.separator"));
				}
			}
			if (less.size() > 0) {
				for (String l: less) {
					sb.append("- ");
					sb.append(l);
					sb.append(System.getProperty("line.separator"));
				}
			}
			System.err.println(sb.toString());
			return sb.toString();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			fail();
			return "";
		}
	}

	// Loads the properties declared in the first file
	private void loadProperties(File file, List<String> properties) {
		InputStream is = null;
		try {
			Properties prop = new Properties();
			is = new FileInputStream(file);
			prop.load(is);
			for (Object key: prop.keySet()) {
				properties.add((String) key);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		finally {
			if (is != null) {
				try {
					is.close();
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
					fail();
				}
			}
		}
	}
}
