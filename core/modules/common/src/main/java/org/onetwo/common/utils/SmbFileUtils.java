package org.onetwo.common.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.onetwo.common.exception.BaseException;

public class SmbFileUtils {

//	private static final Logger logger = JFishLoggerFactory.getLogger(SmbFileUtils.class);

	public static final String SMB_PREFIX = "smb://";

	private SmbFileUtils() {
	}

	public static String newSmbPath(String user, String password, String path){
		return LangUtils.append(SMB_PREFIX, user, ":", password, "@", path);
	}

	public static InputStream newSmbInputStream(String user, String password, String path){
		String smbpath = newSmbPath(user, password, path);
		return newInputStream(smbpath);
	}

	public static OutputStream newSmbOutputStream(String user, String password, String path){
		String smbpath = newSmbPath(user, password, path);
		return newOutputStream(smbpath);
	}
	
	public static boolean isSmbPath(String path){
		return path.toLowerCase().startsWith(SMB_PREFIX);
	}


	public static SmbFile newSmbFile(String fpath){
		SmbFile smbf;
		try {
			smbf = new SmbFile(fpath);
		} catch (MalformedURLException e) {
			throw new BaseException("create smbfile error: " + e.getMessage(), e);
		}
		return smbf;
	}


	public static SmbFileInputStream newSmbInputStream(String fpath){
		return newSmbInputStream(newSmbFile(fpath));
	}
	public static SmbFileInputStream newSmbInputStream(SmbFile smbf){
		try {
			SmbFileInputStream in = new SmbFileInputStream(smbf);
			return in;
		} catch (Exception e) {
			throw new BaseException("create SmbFileInputStream error: " + e.getMessage(), e);
		}
	}

	
	public static InputStream newInputStream(String fpath){
		InputStream in = null;
		String path = FileUtils.replaceBackSlashToSlash(fpath);
		if(isSmbPath(path)){
			in = newSmbInputStream(fpath);
		}else{
			/*File f = newFile(path);
			try {
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				throw new BaseException("create inputstream["+fpath+"] error : " + e.getMessage(), e);
			}*/
			in = FileUtils.newInputStream(fpath);
		}
		return in;
	}

	public static OutputStream newOutputStream(String baseDir, String subPath){
		String path = StringUtils.trimRight(baseDir, FileUtils.SLASH);
		path += StringUtils.appendStartWith(subPath, FileUtils.SLASH);
		return newOutputStream(path);
	}
	
	public static void mkdirs(SmbFile smbf){
		try {
			SmbFile parent = new SmbFile(smbf.getParent());
			if(!parent.exists())
				parent.mkdirs();
		} catch (Exception e) {
			throw new BaseException("make smb direcotry error: " + smbf.getParent());
		}
	}
	
	public static OutputStream newOutputStream(String fpath){
		OutputStream out = null;
		try {
			String path = FileUtils.replaceBackSlashToSlash(fpath);
			if(isSmbPath(path)){
				SmbFile smbf = new SmbFile(path);
				mkdirs(smbf);
				out = new SmbFileOutputStream(smbf);
			}else{
				/*File f = newFile(path);
				makeDirs(f, true);
				out = new FileOutputStream(f);*/
				out = FileUtils.newOutputStream(fpath);
			}
		} catch (Exception e) {
			throw LangUtils.asBaseException("create OutputStream error : " + e.getMessage(), e);
		}
		return out;
	}
	
	public static BufferedReader asBufferedReader(String path, String charset){
		if(isSmbPath(path)){
			return FileUtils.asBufferedReader(newSmbInputStream(path), charset);
		}
		
		String classpath = null;
		BufferedReader br = null;
		try {
			classpath = FileUtils.getResourcePath(path);
			br = FileUtils.asBufferedReader(new FileInputStream(classpath), charset);
		} catch (Exception e) {
			System.out.println("read file error, try to load again :" + classpath);
			br = FileUtils.asBufferedReader(FileUtils.getResourceAsStream(path), charset);
			if(br==null)
				LangUtils.throwBaseException("file not found : " + classpath);
		}
		return br;
	}

}
