package test.labels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import jars.search.core.I18n;

// The point of this test is to make sure no multilanguage resource is behind the
//	others in number of resources, and to identify fast whatever is left and where 
public class TestLabels {

	@Test
	public void testDefault() {
		assertEquals(I18n.RESOURCES.getLabel("query.execution.time"), 
					 I18n.RESOURCES.getLabel(Locale.getDefault(), "query.execution.time"));
	}
	
	@Test
	public void testEnglish() {
		assertEquals(I18n.RESOURCES.getLabel(new Locale("en"),
				"query.execution.time"), "Last query execution time:");
	}
	
	@Test
	public void testFrench() {
		assertEquals(I18n.RESOURCES.getLabel(new Locale("fr"), 
				"query.execution.time"), "Temps d'exécution de la dernière recherche:");
	}

	@Test
	public void testSpanish() {
		assertEquals(I18n.RESOURCES.getLabel(new Locale("es"), 
				"query.execution.time"), "Tiempo de ejecución de la última consulta:");
	}
	
	@Test
	public void testSameResources() {
		try {
			// Test that there is no difference between the resources in any language
			var base = I18n.LABELS_NAME;
			// This makes the assumption that it will be ran from the sources (not from a jar)
			var cl = TestLabels.class.getClassLoader();
			var resources = cl.getResources(base.substring(0, base.lastIndexOf('.')).replace('.', '/'));
			if (resources.hasMoreElements()) {
				var url = resources.nextElement();
				var i18nDir = new File(url.toURI());
				var files = i18nDir.listFiles();
				// Filter the properties files
				var resourceFiles = new ArrayList<File>();
				var labelFilePrefix = base.substring(base.lastIndexOf('.') + 1);
                assertNotNull(files);
                for (var file: files) {
					if (file.isFile() && file.getName().startsWith(labelFilePrefix)) {
						resourceFiles.add(file);
					}
				}
				// List the files
				System.out.println("Comparing the next resource bundle files:");
				
				for (var f: resourceFiles) {
					System.out.println(f.getCanonicalPath());
				}
				// Compare every .properties file inside the i18n directory
				var properties = new LinkedList<String>();
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
		var plus = new LinkedList<String>();
		var less = new LinkedList<String>();
		try(var is = new FileInputStream(file)) {
			var prop = new Properties();
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
		if (!plus.isEmpty() || !less.isEmpty()) {
			fail(reportDiff(file, plus, less));
		}
	}

	// Reports the differences between the properties files
	private String reportDiff(File file, List<String> plus, List<String> less) {
		try {
			var sb = new StringBuilder();
			sb.append("Differences in ");
			sb.append(file.getCanonicalPath());
			sb.append(System.lineSeparator());
			if (!plus.isEmpty()) {
				for (String p: plus) {
					sb.append("+ ");
					sb.append(p);
					sb.append(System.lineSeparator());
				}
			}
			if (!less.isEmpty()) {
				for (String l: less) {
					sb.append("- ");
					sb.append(l);
					sb.append(System.lineSeparator());
				}
			}
			System.err.println(sb);
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
		try(var is = new FileInputStream(file)) {
			var prop = new Properties();
			prop.load(is);
			for (var key: prop.keySet()) {
				properties.add((String) key);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
