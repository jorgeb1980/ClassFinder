package test.files;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseFileTest {

File tempDir = null;
	
	@BeforeEach
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
	
	@AfterEach
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
