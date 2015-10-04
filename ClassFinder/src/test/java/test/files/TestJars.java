package test.files;

import java.io.File;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import jars.search.core.Resource;
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
			for (File dir: dirs) {
				System.out.println(dir.getCanonicalPath());
			}
			System.out.println("Results:");
			for (File container: results.getResults().keySet()) {
				System.out.println(container.getCanonicalPath());
				for (Resource res: results.getResults().get(container)) {
					System.out.println(
						"\t"
						+ res.getName() 
						+ " - " 
						+ res.getSize() 
						+ " bytes (" 
						+ res.getCompressedSize() 
						+ " compressed)");
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
			File container = FileUtils.createContainer(tempDir, "resource.jar");
			FileUtils.addToContainer(
					container, 
					"resource.txt", 
					"",
					"contents of the file".getBytes(Charset.forName("utf-8")));
			FileUtils.addToContainer(
					container, 
					"resource2.txt", 
					"",
					"contents of the second file".getBytes(Charset.forName("utf-8")));
			FileUtils.addToContainer(
					container, 
					"resource3.txt", 
					"a/b/c/",
					"contents of the third file".getBytes(Charset.forName("utf-8")));
			// 3 files named liked this inside the container
			SearchResult result1 = ResourceSearcher.SEARCHER.search(tempDir, "resource");
			printResults(Arrays.asList(new File[]{tempDir}), result1);
			Assert.assertEquals(3, result1.getResults().get(container).size());
			// some different filter
			SearchResult result2 = ResourceSearcher.SEARCHER.search(tempDir, "resource2");
			printResults(Arrays.asList(new File[]{tempDir}), result2);
			Assert.assertEquals(1, result2.getResults().get(container).size());
			Assert.assertEquals("resource2.txt", result2.getResults().get(container).get(0).getName());
		}
		catch(Exception t) {
			t.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testManyResourcesSingleDirectory() {
		try {
			// 1 resource in container 1
			File container1 = FileUtils.createContainer(tempDir, "resource1.jar");
			FileUtils.addToContainer(
					container1, 
					"resource1_1.txt", 
					"",
					"contents of the file, first container".
						getBytes(Charset.forName("utf-8")));
			// 2 resources in container 2
			File container2 = FileUtils.createContainer(tempDir, "resource2.jar");
			FileUtils.addToContainer(
					container2, 
					"resource2_1.txt", 
					"",
					"contents of the first file, second container".
						getBytes(Charset.forName("utf-8")));
			FileUtils.addToContainer(
					container2, 
					"resource2_2.txt", 
					"",
					"contents of the second file, second container".
						getBytes(Charset.forName("utf-8")));
			// 1 resource in container 3
			File container3 = FileUtils.createContainer(tempDir, "resource3.jar");
			FileUtils.addToContainer(
					container3, 
					"resource3_1.txt", 
					"a/b/c/",
					"contents of the first file, third container".
						getBytes(Charset.forName("utf-8")));
			// Simple search
			SearchResult result1 = ResourceSearcher.SEARCHER.search(tempDir, "resource");
			printResults(Arrays.asList(new File[]{tempDir}), result1);
			// 3 files
			Assert.assertEquals(3, result1.getResults().keySet().size());
			// Number of resources in each file
			Assert.assertEquals(1, result1.getResults().get(container1).size());
			Assert.assertEquals(2, result1.getResults().get(container2).size());
			Assert.assertEquals(1, result1.getResults().get(container3).size());			
		}
		catch(Exception t) {
			t.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test 
	public void testManyResourcesManyDirectories() {
		try {
			// dir 1: 1 container with 2 resources
			File subDir1 = FileUtils.createSubDir(tempDir, "level1\\level1_1");
			File container11 = FileUtils.createContainer(subDir1, "resource1_1.jar");
			FileUtils.addToContainer(
					container11, 
					"resource11_1.txt", 
					"first/",
					"contents of the first file, first container, first directory".
						getBytes(Charset.forName("utf-8")));
			FileUtils.addToContainer(
					container11, 
					"resource11_2.txt", 
					"a/",
					"contents of the first file, first container, first directory".
						getBytes(Charset.forName("utf-8")));			
			// dir 2: 2 containers with 1 resource each one
			File subDir2 = FileUtils.createSubDir(tempDir, "level1/level1_2");
			File container21 = FileUtils.createContainer(subDir2, "resource2_1.jar");
			FileUtils.addToContainer(
					container21, 
					"resource21_1.txt", 
					"",
					"contents of the first file, first container, second directory".
						getBytes(Charset.forName("utf-8")));
			File container22 = FileUtils.createContainer(subDir2, "resource2_2.jar");
			FileUtils.addToContainer(
					container22,
					"resource22_1.txt", 
					"",
					"contents of the first file, second container, second directory".
						getBytes(Charset.forName("utf-8")));
			// dir 3: 5 containers, 1 with 2 resources, 4 empty
			File subDir3 = FileUtils.createSubDir(tempDir, "level1/level1_3");
			File container31 = FileUtils.createContainer(subDir3, "resource3_1.jar");
			FileUtils.addToContainer(
					container31, 
					"resource31_1.txt", 
					"a/b/c/",
					"contents of the first file, first container, third directory".
						getBytes(Charset.forName("utf-8")));
			FileUtils.addToContainer(
					container31, 
					"resource31_2.txt", 
					"a/b/c/",
					"contents of the second file, first container, third directory".
						getBytes(Charset.forName("utf-8")));
			File container32 = FileUtils.createContainer(subDir3, "resource3_2.jar");
			File container33 = FileUtils.createContainer(subDir3, "resource3_3.jar");
			File container34 = FileUtils.createContainer(subDir3, "resource3_4.jar");
			File container35 = FileUtils.createContainer(subDir3, "resource3_5.jar");
			// Search certain files in the directories
			File[] files = new File[]{subDir1, subDir2, subDir3};
			SearchResult result1 = ResourceSearcher.SEARCHER.search(
				Arrays.asList(files), "resource1");
			printResults(files, result1);
			Assert.assertEquals(2, result1.getResults().get(container11).size());
			Assert.assertEquals(null, result1.getResults().get(container21));
			Assert.assertEquals(null, result1.getResults().get(container22));
			Assert.assertEquals(null, result1.getResults().get(container31));
			Assert.assertEquals(null, result1.getResults().get(container32));
			Assert.assertEquals(null, result1.getResults().get(container33));
			Assert.assertEquals(null, result1.getResults().get(container34));
			Assert.assertEquals(null, result1.getResults().get(container35));
			SearchResult result2 = ResourceSearcher.SEARCHER.search(
					Arrays.asList(files), "resource2");
			printResults(files, result2);
			Assert.assertEquals(null, result2.getResults().get(container11));
			Assert.assertEquals(1, result2.getResults().get(container21).size());
			Assert.assertEquals(1, result2.getResults().get(container22).size());
			Assert.assertEquals(null, result2.getResults().get(container31));
			Assert.assertEquals(null, result2.getResults().get(container32));
			Assert.assertEquals(null, result2.getResults().get(container33));
			Assert.assertEquals(null, result2.getResults().get(container34));
			Assert.assertEquals(null, result2.getResults().get(container35));
			SearchResult result3 = ResourceSearcher.SEARCHER.search(
					Arrays.asList(files), "resource3");
			printResults(files, result3);
			Assert.assertEquals(null, result3.getResults().get(container11));
			Assert.assertEquals(null, result3.getResults().get(container21));
			Assert.assertEquals(null, result3.getResults().get(container22));
			Assert.assertEquals(2, result3.getResults().get(container31).size());
			Assert.assertEquals(null, result3.getResults().get(container32));
			Assert.assertEquals(null, result3.getResults().get(container33));
			Assert.assertEquals(null, result3.getResults().get(container34));
			Assert.assertEquals(null, result3.getResults().get(container35));
		}
		catch(Exception t) {
			t.printStackTrace();
			Assert.fail();
		}
	}
}
