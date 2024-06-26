package test.files;

import static java.nio.charset.Charset.forName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static test.files.FileUtils.addToContainer;
import static test.files.FileUtils.createContainer;

import java.io.File;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import jars.search.core.ResourceSearcher;
import jars.search.core.SearchResult;

public class TestJars extends BaseFileTest {	
	
	private void printResults(File[] dirs, SearchResult results) {
		printResults(Arrays.asList(dirs), results);
	}
	
	private void printResults(List<File> dirs, SearchResult results) {
		try {
			System.out.println("=========================================================");
			System.out.println(MessageFormat.format("Searching pattern {0} in ...", results.getPattern()));
			for (var dir: dirs) {
				System.out.println(dir.getCanonicalPath());
			}
			System.out.println("Results:");
			for (var container: results.getResourcesByFile().keySet()) {
				System.out.println(container.getCanonicalPath());
				for (var res: results.getResourcesByFile().get(container)) {
					System.out.printf(
						"\t%s - %d bytes (%d compressed)\n",
						res.getName(), res.getSize(), res.getCompressedSize()
					);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOneResourceSingleDirectory() {
		try {
			var container = createContainer(tempDir, "resource.jar");
			addToContainer(
				container, 
				"resource.txt", 
				"",
				"contents of the file".getBytes(forName("utf-8"))
			);
			addToContainer(
				container, 
				"resource2.txt", 
				"",
				"contents of the second file".getBytes(forName("utf-8"))
			);
			addToContainer(
				container, 
				"resource3.txt", 
				"a/b/c/",
				"contents of the third file".getBytes(forName("utf-8"))
			);
			// 3 files named liked this inside the container
			var result1 = ResourceSearcher.SEARCHER.search(tempDir, "resource");
			printResults(Arrays.asList(new File[]{tempDir}), result1);
			assertEquals(3, result1.getResourcesByFile().get(container).size());
			// some different filter
			var result2 = ResourceSearcher.SEARCHER.search(tempDir, "resource2");
			printResults(Arrays.asList(new File[]{tempDir}), result2);
			assertEquals(1, result2.getResourcesByFile().get(container).size());
			assertEquals("resource2.txt", result2.getResourcesByFile().get(container).get(0).getName());
		}
		catch(Exception t) {
			t.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testManyResourcesSingleDirectory() {
		try {
			// 1 resource in container 1
			var container1 = createContainer(tempDir, "resource1.jar");
			final Charset charset = forName("utf-8");
			addToContainer(
				container1, 
				"resource1_1.txt", 
				"",
				"contents of the file, first container".getBytes(charset)
			);
			// 2 resources in container 2
			var container2 = createContainer(tempDir, "resource2.jar");
			addToContainer(
				container2, 
				"resource2_1.txt", 
				"",
				"contents of the first file, second container".getBytes(charset)
			);
			addToContainer(
				container2, 
				"resource2_2.txt", 
				"",
				"contents of the second file, second container".getBytes(charset)
			);
			// 1 resource in container 3
			var container3 = createContainer(tempDir, "resource3.jar");
			addToContainer(
				container3, 
				"resource3_1.txt", 
				"a/b/c/",
				"contents of the first file, third container".getBytes(charset)
			);
			// Simple search
			var result1 = ResourceSearcher.SEARCHER.search(tempDir, "resource");
			printResults(Arrays.asList(new File[]{tempDir}), result1);
			// 3 files
			assertEquals(3, result1.getResourcesByFile().keySet().size());
			// Number of resources in each file
			assertEquals(1, result1.getResourcesByFile().get(container1).size());
			assertEquals(2, result1.getResourcesByFile().get(container2).size());
			assertEquals(1, result1.getResourcesByFile().get(container3).size());
		}
		catch(Exception t) {
			t.printStackTrace();
			fail();
		}
	}
	
	@Test 
	public void testManyResourcesManyDirectories() {
		try {
			// dir 1: 1 container with 2 resources
			var subDir1 = FileUtils.createSubDir(tempDir, "level1\\level1_1");
			var container11 = createContainer(subDir1, "resource1_1.jar");
			final Charset charset = forName("utf-8");
			addToContainer(
				container11, 
				"resource11_1.txt", 
				"first/",
				"contents of the first file, first container, first directory".getBytes(charset)
			);
			addToContainer(
				container11, 
				"resource11_2.txt", 
				"a/",
				"contents of the first file, first container, first directory".getBytes(charset)
			);			
			// dir 2: 2 containers with 1 resource each one
			var subDir2 = FileUtils.createSubDir(tempDir, "level1/level1_2");
			var container21 = createContainer(subDir2, "resource2_1.jar");
			addToContainer(
				container21, 
				"resource21_1.txt", 
				"",
				"contents of the first file, first container, second directory".getBytes(charset)
			);
			var container22 = createContainer(subDir2, "resource2_2.jar");
			addToContainer(
				container22,
				"resource22_1.txt", 
				"",
				"contents of the first file, second container, second directory".getBytes(charset)
			);
			// dir 3: 5 containers, 1 with 2 resources, 4 empty
			var subDir3 = FileUtils.createSubDir(tempDir, "level1/level1_3");
			var container31 = createContainer(subDir3, "resource3_1.jar");
			addToContainer(
				container31, 
				"resource31_1.txt", 
				"a/b/c/",
				"contents of the first file, first container, third directory".getBytes(charset)
			);
			addToContainer(
				container31, 
				"resource31_2.txt", 
				"a/b/c/",
				"contents of the second file, first container, third directory".getBytes(charset)
			);
			var container32 = createContainer(subDir3, "resource3_2.jar");
			var container33 = createContainer(subDir3, "resource3_3.jar");
			var container34 = createContainer(subDir3, "resource3_4.jar");
			var container35 = createContainer(subDir3, "resource3_5.jar");
			// Search certain files in the directories
			var files = new File[]{subDir1, subDir2, subDir3};
			
			var result1 = ResourceSearcher.SEARCHER.search(Arrays.asList(files), "resource1");
			printResults(files, result1);
			assertEquals(2, result1.getResourcesByFile().get(container11).size());
            assertNull(result1.getResourcesByFile().get(container21));
            assertNull(result1.getResourcesByFile().get(container22));
            assertNull(result1.getResourcesByFile().get(container31));
            assertNull(result1.getResourcesByFile().get(container32));
			assertNull(result1.getResourcesByFile().get(container33));
			assertNull(result1.getResourcesByFile().get(container34));
			assertNull(result1.getResourcesByFile().get(container35));
			
			var result2 = ResourceSearcher.SEARCHER.search(Arrays.asList(files), "resource2");
			printResults(files, result2);
			assertNull(result2.getResourcesByFile().get(container11));
			assertEquals(1, result2.getResourcesByFile().get(container21).size());
			assertEquals(1, result2.getResourcesByFile().get(container22).size());
			assertNull(result2.getResourcesByFile().get(container31));
			assertNull(result2.getResourcesByFile().get(container32));
			assertNull(result2.getResourcesByFile().get(container33));
			assertNull(result2.getResourcesByFile().get(container34));
			assertNull(result2.getResourcesByFile().get(container35));
			
			var result3 = ResourceSearcher.SEARCHER.search(Arrays.asList(files), "resource3");
			printResults(files, result3);
			assertNull(result3.getResourcesByFile().get(container11));
			assertNull(result3.getResourcesByFile().get(container21));
			assertNull(result3.getResourcesByFile().get(container22));
			assertEquals(2, result3.getResourcesByFile().get(container31).size());
			assertNull(result3.getResourcesByFile().get(container32));
			assertNull(result3.getResourcesByFile().get(container33));
			assertNull(result3.getResourcesByFile().get(container34));
			assertNull(result3.getResourcesByFile().get(container35));
		}
		catch(Exception t) {
			t.printStackTrace();
			fail();
		}
	}
}
