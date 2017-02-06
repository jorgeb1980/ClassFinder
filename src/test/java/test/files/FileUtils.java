/**
 * 
 */
package test.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Simple utilities class to test files
 */
public class FileUtils {

	// Creates a temporary directory
	public static File createTempDir() throws IOException {
		File f = File.createTempFile("tmp", "dir");
		f.delete();
		f.mkdirs();
		return f;
	}
	
	// Creates a zip format container inside a directory
	public static File createContainer(File dir, String name) throws IOException {
		File f = new File(dir.getCanonicalPath() + System.getProperty("file.separator") + name);
		f.createNewFile();
		return f;
	}
	
	// Creates a subdirectory with a relative path
	public static File createSubDir(File dir, String path) throws IOException {
		String realPath = path;
		// Make sure it will work wherever
		realPath = realPath.replaceAll("\\\\", "\\"+System.getProperty("file.separator"));
		realPath = realPath.replaceAll("\\/", "\\"+System.getProperty("file.separator"));
		File subDir = new File(dir.getCanonicalPath() 
				+  System.getProperty("file.separator") + realPath);		
		subDir.mkdirs();
		return subDir;
	}
	
	// Shamelessly copied from stackoverflow.com
	private static void addFilesToZip(File source, File[] files, String path){
	    try{
	        File tmpZip = File.createTempFile(source.getName(), null);
	        tmpZip.delete();
	        if(!source.renameTo(tmpZip)){
	            throw new Exception("Could not make temp file (" + source.getName() + ")");
	        }
	        byte[] buffer = new byte[4096];
	        ZipInputStream zin = new ZipInputStream(new FileInputStream(tmpZip));
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(source));
	        for(int i = 0; i < files.length; i++){
	            InputStream in = new FileInputStream(files[i]);
	            out.putNextEntry(new ZipEntry(path + files[i].getName()));
	            for(int read = in.read(buffer); read > -1; read = in.read(buffer)){
	                out.write(buffer, 0, read);
	            }
	            out.closeEntry();
	            in.close();
	        }
	        for(ZipEntry ze = zin.getNextEntry(); ze != null; ze = zin.getNextEntry()){
	            if(!zipEntryMatch(ze.getName(), files, path)){
	                out.putNextEntry(ze);
	                for(int read = zin.read(buffer); read > -1; read = zin.read(buffer)){
	                    out.write(buffer, 0, read);
	                }
	                out.closeEntry();
	            }
	        }
	        zin.close();
	        out.close();
	        tmpZip.delete();
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}

	private static boolean zipEntryMatch(String zeName, File[] files, String path){
	    for(int i = 0; i < files.length; i++){
	        if((path + files[i].getName()).equals(zeName)){
	            return true;
	        }
	    }
	    return false;
	}
	
	// Adds a resource to a given container
	// resourcePath may have depth (aa.txt, a/b/c/d.txt, etc.)
	public static void addToContainer(File container, String resourceName, String path, byte[] contents) 
			throws IOException {
		File tmp = new File(container.getParentFile().getCanonicalPath() 
				+ System.getProperty("file.separator") 
				+ resourceName);
		FileOutputStream fos = new FileOutputStream(tmp);
		try {
			fos.write(contents, 0, contents.length);
			fos.flush();
		}
		finally {
			fos.close();
		}
		addFilesToZip(container, new File[]{tmp}, path);
        
		tmp.delete();
	}
}
