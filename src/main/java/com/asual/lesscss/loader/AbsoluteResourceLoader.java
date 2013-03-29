package com.asual.lesscss.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Jmu 
 * @version 2012-8-22 上午9:13:36
 * 
 */
public class AbsoluteResourceLoader extends FilesystemResourceLoader {

	private String baseDir;

	public AbsoluteResourceLoader(String baseDir) {
		this.baseDir = baseDir;
	}
	
	private String getFile(File f,String filename){
		File[] fls = f.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if(pathname.isDirectory()||pathname.getName().endsWith(".less")){
					return true;
				}
				return false;
			}
		});
		String result = "";
		for (File fs : fls) {
			if(fs.isDirectory()){
				result= getFile(fs, filename);
			}else{
				if(fs.getName().equals(filename)){
					result = fs.getAbsolutePath();
					break;
				}
			}
		}
		return result;
	}
	
	protected InputStream openStream(String path) throws IOException {
		InputStream in = super.openStream(baseDir+"/"+path);
		if(in == null){
			File f = new File(baseDir);
			String result =  getFile(f,path.substring(path.lastIndexOf("/")+1));
			if(result.length()>1){
				in = super.openStream(result);
			}
		}
		return in;
	}

	public boolean exists(String path) throws IOException {
		return true;
	}

	public String load(String path, String charset) throws IOException {
		if(path.indexOf("file://")>-1){
			path = path.replace("file://", "");
		}
		InputStream is = openStream(path);
		if (is != null) {
			return readStream(is, charset);
		}
		throw new IOException("No such file " + path);
	}

	

}
