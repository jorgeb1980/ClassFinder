package test.files;

import org.junit.After;
import org.junit.Before;

import java.io.File;

public class BaseFileTest {

File tempDir = null;
	
	@Before
	public void createTempDir() {
		try {
			tempDir = FileUtils.createTempDir();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Recursive cleansing
	private void delete(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File child: file.listFiles()) {
					delete(child);
				}
			}
			file.delete();
		}
	}
	
	@After
	public void cleanTempDir() {
		try {
			if (tempDir != null && tempDir.exists() && tempDir.isDirectory()) {
				delete(tempDir);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
