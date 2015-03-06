package org.onetwo.common.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.apache.commons.lang.RandomStringUtils;
import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.onetwo.common.utils.propconf.ResourceAdapterImpl;
import org.slf4j.Logger;

@SuppressWarnings("unchecked")
public class FileUtils {

	private static final Logger logger = MyLoggerFactory.getLogger(FileUtils.class);

	public static final String UTF8 = "utf-8";
	public static final String DEFAULT_CHARSET = UTF8;
	public static final int DEFAULT_BUF_SIZE = 1024 * 4;
	public static final String PACKAGE = "package";
	public static final String PATH = "#path:";
	public static final String SLASH = "/";
	public static final char SLASH_CHAR = '/';
	public static final char BACK_SLASH_CHAR = '\\';
	public static final char DOT_CHAR = '.';
	public static final String NEW_LINE = "\n";
	public static final String SMB_PREFIX = "smb://";
	public static final String COLON_DB_SLASH_HEAD = "://";
	public static final String DB_SLASH_HEAD = "//";

	public static ResourceAdapter[] EMPTY_RESOURCES = new ResourceAdapter[0];
	private static final Expression PLACE_HODER_EXP = Expression.DOLOR;

	private FileUtils() {
	}


	public static boolean delete(File file){
		return file.delete();
	}

	public static boolean delete(String path){
		File file = newFile(path, false);
		return file.delete();
	}

	public static File newFile(String path){
		return newFile(path, false);
	}
	public static File newFile(String path, boolean checkExists){
		File f = new File(path);
		if(checkExists && !f.exists())
			throw new BaseException("file not found at path: " + path);
		return f;
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
	

	public static InputStream newInputStream(String baseDir, String subPath){
		String path = StringUtils.trimRight(baseDir, SLASH);
		path += StringUtils.appendStartWith(subPath, SLASH);
		return newInputStream(path);
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
		String path = replaceBackSlashToSlash(fpath);
		if(isSmbPath(path)){
			in = newSmbInputStream(fpath);
		}else{
			File f = newFile(path);
			try {
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				throw new BaseException("create inputstream["+fpath+"] error : " + e.getMessage(), e);
			}
		}
		return in;
	}

	public static OutputStream newOutputStream(String baseDir, String subPath){
		String path = StringUtils.trimRight(baseDir, SLASH);
		path += StringUtils.appendStartWith(subPath, SLASH);
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
			String path = replaceBackSlashToSlash(fpath);
			if(isSmbPath(path)){
				SmbFile smbf = new SmbFile(path);
				mkdirs(smbf);
				out = new SmbFileOutputStream(smbf);
			}else{
				File f = newFile(path);
				makeDirs(f, true);
				out = new FileOutputStream(f);
			}
		} catch (Exception e) {
			throw LangUtils.asBaseException("create OutputStream error : " + e.getMessage(), e);
		}
		return out;
	}
    
	  //-----------------------------------------------------------------------
    /**
     * Opens a {@link FileOutputStream} for the specified file, checking and
     * creating the parent directory if it does not exist.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * The parent directory will be created if it does not exist.
     * The file will be created if it does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be written to.
     * An exception is thrown if the parent directory cannot be created.
     * 
     * @param file  the file to open for output, must not be <code>null</code>
     * @return a new {@link FileOutputStream} for the specified file
     * @throws IOException if the file object is a directory
     * @throws IOException if the file cannot be written to
     * @throws IOException if a parent directory needs creating but that fails
     * @since Commons IO 1.3
     */
    public static FileOutputStream openOutputStream(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new BaseException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new BaseException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && parent.exists() == false) {
                if (parent.mkdirs() == false) {
                    throw new BaseException("File '" + file + "' could not be created");
                }
            }
        }
        try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			throw new BaseException("create FileOutputStream error.", e);
		}
    }
	    

	    
	public static boolean exists(String path){
		return exists(new File(path));
	}
	
	public static boolean exists(File file){
		return file.exists();
	}
	

	public static String getResourcePath(String fileName){
		return getResourcePath(ClassUtils.getDefaultClassLoader(), fileName);
	}
	
	/** URL protocol for an entry from a jar file: "jar" */
	public static final String URL_PROTOCOL_JAR = "jar";

	/** URL protocol for an entry from a zip file: "zip" */
	public static final String URL_PROTOCOL_ZIP = "zip";
	/** URL protocol for an entry from a WebSphere jar file: "wsjar" */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";

	/** URL protocol for an entry from an OC4J jar file: "code-source" */
	public static final String URL_PROTOCOL_CODE_SOURCE = "code-source";

	/** Separator between JAR URL and file path within the JAR */
	public static final String JAR_URL_SEPARATOR = "!/";

	public static boolean isJarURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(protocol) ||
				URL_PROTOCOL_ZIP.equals(protocol) ||
				URL_PROTOCOL_WSJAR.equals(protocol) ||
				(URL_PROTOCOL_CODE_SOURCE.equals(protocol) && url.getPath().contains(JAR_URL_SEPARATOR)));
	}
	public static boolean isJarURL(String url) {
		return url.contains(JAR_URL_SEPARATOR);
	}
	 
	public static String getResourcePath(ClassLoader cld, String fileName){
		if(cld==null)
			cld = ClassUtils.getDefaultClassLoader();
		if(fileName.startsWith(PATH)){
			return fileName.substring(PATH.length());
		}
		if(fileName.indexOf(":")!=-1)
			return fileName;
		String realPath = null;
		URL path = null;
		path = cld.getResource(fileName);
		
		logger.info("Default ClassLoader path1: "+ path);
		if(path==null){
			/*path = FileUtils.class.getClassLoader().getResource(fileName);
			if(path==null)
				return fileName;*/
			realPath = cld.getResource("").getPath()+fileName;
			logger.info("Default ClassLoader path2: "+ realPath);
			if(StringUtils.isBlank(realPath)){
				logger.info("FileUtils ClassLoader path3: "+ realPath);
				realPath = getResourcePath(FileUtils.class.getClassLoader(), fileName);
				if(StringUtils.isBlank(realPath))
					throw new BaseException("get resource path error: " + fileName);
			}
		}else{
			realPath = path.getPath();
		}
		if(realPath.indexOf("\\")!=-1)
			realPath = realPath.replace("\\", "/");
//		if(realPath.startsWith("/"))
//			realPath = realPath.substring(1);
		return realPath;
	}
	 
	public static InputStream getResourceAsStream(ClassLoader cld, String fileName){
		InputStream in = null;
		if(cld!=null)
			in = cld.getResourceAsStream(fileName);
		if(in==null){
			in = getClassLoader().getResourceAsStream(fileName);
			in = in!=null?in:FileUtils.class.getClassLoader().getResourceAsStream(fileName);
		}
		return in;
	}
	 
	public static InputStream getResourceAsStream(String fileName){
		return getResourceAsStream(null, fileName);
	}
	
	public static BufferedReader asBufferedReader(InputStream in){
		return asBufferedReader(in, DEFAULT_CHARSET);
	}
	
	public static BufferedReader asBufferedReader(File file){
		try {
			return asBufferedReader(new FileInputStream(file), DEFAULT_CHARSET);
		} catch (FileNotFoundException e) {
			throw LangUtils.asBaseException("reader file error: " + file, e);
		}
	}

	public static BufferedReader asBufferedReader(InputStream in, String charset){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in, charset));
		} catch (UnsupportedEncodingException e) {
			LangUtils.throwBaseException("charset error : " + e.getMessage(), e);
		}
		return br;
	}
	

	public static List<String> readAsList(InputStream in){
		return readAsList(in, DEFAULT_CHARSET);
	}

	public static List<String> readAsList(InputStream in, String charset){
		final List<String> datas = new ArrayList<String>();
		reader(asBufferedReader(in, charset), new FileLineCallback() {
			
			@Override
			public boolean doWithLine(String line, int lineIndex) {
				datas.add(line);
				return true;
			}
		});
		return datas;
	}
	
	
	public static void reader(BufferedReader br, FileLineCallback flcb){
//		List<String> datas = new ArrayList<String>();
//		BufferedReader br = null;
		try{
//			br = asBufferedReader(in, charset);
			String buf = null;
			int lineIndex = 0;
			while((buf=br.readLine())!=null){
//				datas.add(buf);
				if(!flcb.doWithLine(buf, lineIndex++)){
					break;
				}
			}
		}catch(Exception e){
			LangUtils.throwBaseException("read file["+br+"] error : " + e.getMessage(), e);
		}finally{
			close(br);
		}
//		return datas;
	}
	
	public static BufferedReader asBufferedReader(String path, String charset){
		if(isSmbPath(path)){
			return asBufferedReader(newSmbInputStream(path), charset);
		}
		
		String classpath = null;
		BufferedReader br = null;
		try {
			classpath = getResourcePath(path);
			br = asBufferedReader(new FileInputStream(classpath), charset);
		} catch (Exception e) {
			System.out.println("read file error, try to load again :" + classpath);
			br = asBufferedReader(getResourceAsStream(path), charset);
			if(br==null)
				LangUtils.throwBaseException("file not found : " + classpath);
		}
		return br;
	}
	

	public static List<String> readAsList(String fileName){
		return readAsList(fileName, DEFAULT_CHARSET);
	}


	public static List<String> readAsList(String fileName, String charset){
		return readAsListWithMap(fileName, charset, null);
	}

	public static List<String> readAsListWith(String fileName, String charset, Map<String, Object> context){
		return readAsListWithMap(fileName, charset, context);
	}
	
	public static List<String> readAsListWithMap(String fileName, String charset, final Map<String, Object> context){
		final List<String> datas = new ArrayList<String>();
		BufferedReader br = null;
		try{
			br = asBufferedReader(fileName, charset);
			reader(br, new FileLineCallback() {
				
				@Override
				public boolean doWithLine(String line, int lineIndex) {
					if(LangUtils.isNotEmpty(context) && PLACE_HODER_EXP.isExpresstion(line)){
						line = PLACE_HODER_EXP.parseByProvider(line, context);
					}
					datas.add(line);
					return true;
				}
			});
		}catch(Exception e){
			LangUtils.throwBaseException("read file["+fileName+"] error : " + e.getMessage(), e);
		}finally{
			close(br);
		}
		return datas;
	}
	

	public static List<String> readAsList(File file){
		return readAsList(file, DEFAULT_CHARSET);
	}

	public static List<String> readAsList(File file, String charset){
		return readAsListWithMap(file, charset, null);
	}
	
	public static List<String> readAsListWithMap(File file, String charset, Map<String, Object> context){
		List<String> datas = new JFishList<String>();
		BufferedReader br = null;
		try{
			br = asBufferedReader(new FileInputStream(file), charset);
			String buf = null;
			while((buf=br.readLine())!=null){
				if(LangUtils.isNotEmpty(context) && PLACE_HODER_EXP.isExpresstion(buf)){
					buf = PLACE_HODER_EXP.parseByProvider(buf, context);
				}
				datas.add(buf);
			}
		}catch(Exception e){
			LangUtils.throwBaseException("read file["+file.getPath()+"] error : " + e.getMessage(), e);
		}finally{
			close(br);
		}
		return datas;
	}


	public static String readAsString(String fileName){
		return readAsString(fileName, DEFAULT_CHARSET);
	}
	
	public static String readAsString(String fileName, String charset){
		return readAsStringWith(fileName, charset, (Map<String, Object>)null);
	}
	
	public static String readAsStringWith(String fileName, Object... context){
		return StringUtils.join(readAsListWith(fileName, DEFAULT_CHARSET, LangUtils.asMap(context)), "");
	}
	
	public static String readAsStringWith(String fileName, String charset, Map<String, Object> context){
		return StringUtils.join(readAsListWith(fileName, charset, context), "");
	}
	

	public static String readAsString(File file){
		return readAsStringWith(file, DEFAULT_CHARSET, null);
	}
	public static String readAsString(File file, String charset){
		return readAsStringWith(file, charset, null);
	}
	
	public static String readAsStringWith(File file, String charset, Map<String, Object> context){
		return StringUtils.join(readAsListWithMap(file, charset, context), "");
	}
	
	public static ClassLoader getClassLoader(){
		return ClassUtils.getDefaultClassLoader();
	}

	public static String getFileNameWithoutExt(String fileName) {
		if(fileName.indexOf('\\')!=-1)
			fileName = fileName.replace('\\', SLASH_CHAR);
		int start = fileName.lastIndexOf(SLASH_CHAR);
		int index = fileName.lastIndexOf(DOT_CHAR);
		if(index==-1)
			index = fileName.length();
		return fileName.substring(start+1, index);
	}

	public static String getFileName(String fileName) {
		fileName = replaceBackSlashToSlash(fileName);
		int start = fileName.lastIndexOf(SLASH_CHAR);
		return fileName.substring(start+1);
	}
	
	public static String convertDir(String path){
		String dir = replaceBackSlashToSlash(path);
		int index = dir.indexOf(COLON_DB_SLASH_HEAD);
		if(index==-1){
			if(dir.contains(DB_SLASH_HEAD)){
				dir = dir.replace(DB_SLASH_HEAD, SLASH);
			}
		}else{
			String head = dir.substring(0, index+COLON_DB_SLASH_HEAD.length());
			String spath = dir.substring(head.length());
			if(spath.contains(DB_SLASH_HEAD)){
				spath = spath.replace(DB_SLASH_HEAD, SLASH);
			}
			dir = head + spath;
		}
		return StringUtils.appendEndWith(dir, SLASH);
	}
	
	public static String replaceBackSlashToSlash(String path){
		return path.indexOf(BACK_SLASH_CHAR)!=-1?path.replace(BACK_SLASH_CHAR, SLASH_CHAR):path;
	}

	public static String getExtendName(String fileName, boolean hasDot) {
		int index = fileName.lastIndexOf(DOT_CHAR);
		if (index == -1)
			return "";
		if(hasDot)
			return fileName.substring(index);
		else
			return fileName.substring(index + 1);
	}

	public static String getExtendName(String fileName) {
		return getExtendName(fileName, false);
	}
	

	public static String newFileNameByDateAndRand(String fileNameNoDirPath){
		return newFileNameByDateAndRand(fileNameNoDirPath, "-", DateUtil.DateTime, 6);
	}
	
	public static String newFileNameByDateAndRand(String fileNameNoDirPath, String seprator, String dateformat, int count){
		String newFileName = getFileNameWithoutExt(fileNameNoDirPath)+ seprator + NiceDate.New().format(dateformat);
		if(count>0){
			newFileName += seprator + RandomStringUtils.randomNumeric(count);
		}
		newFileName += getExtendName(fileNameNoDirPath, true);
		return newFileName;
	}


	public static String newFileNameAppendRepeatCount(File file){
		return newFileNameAppendRepeatCount(file.getParent(), file.getName());
	}
	public static String newFileNameAppendRepeatCount(String baseDir, String fileName){
		File wfile = new File(baseDir, fileName);
		if(!wfile.exists()){
			return fileName;
		}
		int count = 1;
		String ext = FileUtils.getExtendName(fileName, true);
		String srcfileName = FileUtils.getFileNameWithoutExt(fileName);
		String newFileName = srcfileName + "(" + count + ")" + ext;
		wfile = new File(baseDir, newFileName);
		while (wfile.exists()) {
			newFileName = srcfileName + "(" + count + ")"+ext;
			wfile = new File(baseDir, newFileName);
			count++;
		}
		return newFileName;
	}
	

	public static void copyFileTo(File file, String targetDir, String targetFileName){
		InputStream in;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new BaseException("file not found: " + file.getPath(), e);
		}
		writeInputStreamTo(in, targetDir, targetFileName);
	}
	
	public static int writeInputStreamTo(InputStream in, String targetDir, String targetFileName){
		OutputStream out = null;
		try{
			out = newOutputStream(targetDir, targetFileName);
			return IOUtils.copy(in, out);
		}catch(Exception e){
			throw new BaseException("write inputstream error: " + e.getMessage(), e);
		}finally{
			close(out);
		}
	}

	public static File writeToDisk(File srcfile, String filePath, String fileName) {
		InputStream in = null;
		OutputStream out = null;
		File wfile = null;
		try {
			wfile = new File(filePath, FileUtils.newFileNameAppendRepeatCount(filePath, fileName));
			in = new FileInputStream(srcfile);
			out = new FileOutputStream(wfile);
			byte[] buf = new byte[4096];
			int length = 0;
			while ((length = in.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
		} catch (Exception e) {
			throw new RuntimeException("occur error when write file : " + srcfile.getPath(), e);
		} finally {
			FileUtils.close(in);
			FileUtils.close(out);
		}
		return wfile;
	}
	

	public static File copyFileToDir(File srcFile, String targetDir) {
		String fname = getFileName(srcFile.getName());
		File destFile = new File(targetDir + File.separator + fname);
		String newFileName = newFileNameAppendRepeatCount(destFile);
		destFile = new File(destFile.getParentFile(), newFileName);
		
		copyFile(srcFile, destFile);
		return destFile;
	}

	public static File copyFileToDir(SmbFile srcFile, String targetDir) {
		SmbFileInputStream in = newSmbInputStream(srcFile);
		
		String fname = getFileName(srcFile.getName());
		File destFile = new File(targetDir + File.separator + fname);
		String newFileName = newFileNameAppendRepeatCount(destFile);
		destFile = new File(destFile.getParentFile(), newFileName);
		
		writeInputStreamTo(in, targetDir, newFileName);
		return destFile;
	}
	

	public static File copyFile(String srcFile, String destFile) {
		File dest = new File(destFile);
		copyFile(new File(srcFile), dest);
		return dest;
	}

	public static void copyFile(File srcFile, File destFile) {
		Assert.notNull(srcFile);
		Assert.notNull(destFile);

		if (destFile.isDirectory()){
			throw new BaseException("the file is directory: " + destFile.getPath());
		}

		makeDirs(destFile, true);
		if(!destFile.exists()){
			try {
				destFile.createNewFile();
			} catch (IOException e) {
				throw new BaseException("create new file error: " + destFile.getPath(), e);
			}
		}else if (destFile.isHidden() || !destFile.canWrite()){
			throw new BaseException("the file is hidden or readonly : " + destFile.getPath());
		}

		BufferedOutputStream fout = null;
		try {
			fout = new BufferedOutputStream(new FileOutputStream(destFile));
			copyFileToOutputStream(fout, srcFile);
		} catch (Exception e) {
			throw new BaseException("copy file error", e);
		} finally {
//			IOUtils.closeQuietly(fin);
			IOUtils.closeQuietly(fout);
		}
	}
	

	public static void copyFileToOutputStream(OutputStream out, File srcFile) {
		Assert.notNull(srcFile);
		Assert.notNull(out);

		BufferedInputStream fin = null;
		BufferedOutputStream fout = null;
		try {
			fin = new BufferedInputStream(new FileInputStream(srcFile));
			fout = new BufferedOutputStream(out);
			byte[] buf = new byte[1024 * 5];
			int count = 0;
			while ((count = fin.read(buf, 0, buf.length)) != -1) {
				fout.write(buf, 0, count);
			}
			fout.flush();
		} catch (Exception e) {
			throw new BaseException("copy file error", e);
		} finally {
			IOUtils.closeQuietly(fin);
//			IOUtils.closeQuietly(fout);
		}
	}

	public static void copyDir(String srcPath, String destPath) throws Exception {
		File destFile = new File(destPath);
		File srcFile = new File(srcPath);
		if (!srcFile.exists())
			return;
		if (srcFile.isFile()) {
			if (destFile.isDirectory()) {
				if (!destFile.exists())
					destFile.mkdirs();
				destFile = new File(destFile, srcFile.getName());
			}
			copyFile(srcFile, destFile);
		} else {
			if (!destFile.exists()) {
				destFile.mkdirs();
//				System.out.println("create drectory : " + destFile.getPath());
			}
			File[] list = srcFile.listFiles();
			for (int i = 0; i < list.length; i++) {
				File file = list[i];
				if (file.isDirectory())
					copyDir(file.getPath(), destPath + "/" + file.getName());
				else
					copyDir(file.getPath(), destPath);
			}
		}
	}

	public static List<File> listFile(String dirPath, String regex) {
		Pattern pattern = null;
		if(StringUtils.isNotBlank(regex)){
			if (!regex.startsWith("^"))
				regex = "^" + regex;
	
			if (!regex.endsWith("$"))
				regex = regex + "$";
			pattern = Pattern.compile(regex);
		}

		File dirFile = new File(dirPath);
		
		return listFile(dirFile, pattern);

	}
	
	public static Map<String, byte[]> readFilesAsBytesToMap(String dir){
		File[] files = listFiles(dir, null);
		if(LangUtils.isEmpty(files))
			return null;
		Map<String, byte[]> mapBytes = new HashMap<String, byte[]>();
		byte[] temp = null;
		for(File f : files){
			temp = readFileToByteArray(f);
			mapBytes.put(f.getName(), temp);
		}
		return mapBytes;
	}


    public static byte[] readFileToByteArray(File file) {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtils.toByteArray(in);
        }catch(IOException ioe){
        	throw new BaseException("readFileToByteArray error: "+ioe.getMessage(), ioe);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static byte[] readFileToByteArray(InputStream in) {
        try {
            return IOUtils.toByteArray(in);
        }catch(IOException ioe){
        	throw new BaseException("readFileToByteArray error: "+ioe.getMessage(), ioe);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
	public static String getParentpath(String path){
		int index = path.lastIndexOf("/");
		return path.substring(0, index);
	}


	public static List<File> listFile(File dirFile) {
		return listFile(dirFile, (Pattern)null);
	}
	
	public static List<File> listFile(File dirFile, Pattern pattern) {
		File[] files = dirFile.listFiles();
		if (files == null)
			return Collections.EMPTY_LIST;

		List<File> fileList = new ArrayList<File>();
		for (File f : files) {
			if (f.isFile() && (pattern==null || pattern.matcher(f.getPath()).matches())) {
				fileList.add(f);
			} 
			else {
				List<File> l = listFile(f, pattern);
				if(l==null || l.isEmpty())
					continue;
				fileList.addAll(l);
			}
		}

		return fileList;
	}
	
	public static String getPackageName(File file){
		String packageName = "";
		BufferedReader br = null;
		try {
			br =new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line=br.readLine())!=null){
				line = line.trim();
				if(line.startsWith("package")){
					packageName = line.substring(PACKAGE.length(), line.indexOf(';')).trim();
					break;
				}
			}
		} catch (Exception e) {
			throw new BaseException(e);
		}finally{
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {
					throw new BaseException(e);
				}
		}
		return packageName;
	}
	
	public static Class loadClass(File file){
		Class clazz = null;
		String className = getPackageName(file)+"."+getFileNameWithoutExt(file.getName());
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new BaseException(e);
		}
		return clazz;
	}

	public static File writeTo(String dir, String name, InputStream in){
		File file = new File(dir, name);
		writeTo(file, in);
		return file;
	}
	
	public static void writeTo(File tofile, InputStream srcInput){
		OutputStream out = null;
		try {
			File parent = tofile.getParentFile();
			if(!parent.exists()){
				parent.mkdirs();
			}
			out = new FileOutputStream(tofile);
			byte[] buf = new byte[1024];
			int len = -1;
			while((len=srcInput.read(buf))!=-1){
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			logger.error("write file error : " + tofile.getPath(), e);
		} finally{
			close(out);
		}
	}
	

    public static void writeByteArrayToFile(File file, byte[] data){
        OutputStream out = null;
        try {
            out = openOutputStream(file);
            out.write(data);
        } catch(Exception e){
        	throw LangUtils.asBaseException("write data error : " + e.getMessage(), e);
        }finally {
            IOUtils.closeQuietly(out);
        }
    }
    

    public static void writeStringToFile(File file, String data){
    	writeStringToFile(file, null, data);
    }
    
    public static void writeStringToFile(File file, String charset, String data){
        Writer w = writer(file, charset);
        try {
            w.write(data);
        } catch(Exception e){
        	throw LangUtils.asBaseException("write data error : " + e.getMessage(), e);
        }finally {
            IOUtils.closeQuietly(w);
        }
    }
    

    public static void writeListTo(File file, List<?> datas){
    	writeListTo(file, null, datas, "\n");
    }
    
    public static void writeListTo(File file, String charset, List<?> datas, String separator){
        Writer w = writer(file, charset);
        try {
            for(Object data : datas){
            	w.write(data.toString());
            	w.write(separator);
            }
        } catch(Exception e){
        	throw LangUtils.asBaseException("write data error : " + e.getMessage(), e);
        }finally {
            IOUtils.closeQuietly(w);
        }
    }
    

    public static void writeListTo(OutputStream output, String charset, List<?> datas){
    	writeListTo(newWriter(output, charset), false, datas, "\n");
    }
    public static void writeListToWithClose(OutputStream output, String charset, List<?> datas){
    	writeListTo(newWriter(output, charset), true, datas, "\n");
    }
    
    
    /****
     * 
     * @param output 本方法不关闭stream
     * @param charset
     * @param datas
     * @param separator
     */
    public static void writeListTo(OutputStream output, String charset, List<?> datas, String separator){
    	writeListTo(newWriter(output, charset), false, datas, separator);
    }
    
    public static void writeListTo(Writer w, boolean closeStream, List<?> datas, String separator){
        try {
            for(Object data : datas){
            	w.write(data.toString());
            	w.write(separator);
            }
        } catch(Exception e){
        	throw LangUtils.asBaseException("write data error : " + e.getMessage(), e);
        }finally {
        	if(closeStream)
        		IOUtils.closeQuietly(w);
        }
    }
    
    public static Writer writer(File file, String charset){
        return newWriter(openOutputStream(file), charset);
    }
    
    
    public static Writer newWriter(OutputStream output){
    	return newWriter(output, null);
    }
    public static Writer newWriter(OutputStream output, String charset){
    	OutputStreamWriter w = null;
        try {
        	if(StringUtils.isBlank(charset)){
        		w = new OutputStreamWriter(output);
        	}else{
        		w = new OutputStreamWriter(output, charset);
        	}
        } catch(Exception e){
        	throw new BaseException("create writer error : " + e.getMessage(), e);
        }
        return w;
    }
    
	
	public static void close(Closeable io){
		try {
			if(io!=null)
				io.close();
		} catch (IOException e) {
			logger.error("close error : " + e.getMessage(), e);
		}
	}

	public static File[] listFiles(String dir, final String postfix){
		File dirFile = new File(dir);
		/*File[] sqlFileList = sqlDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(new File(name).isDirectory())
					return false;
				if(StringUtils.isBlank(postfix))
					return true;
				boolean rs = name.toLowerCase().endsWith(postfix);
				return rs;
			}
		});*/
		File[] sqlFileList = dirFile.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File dir) {
				if(dir.isDirectory())
					return false;
				if(StringUtils.isBlank(postfix))
					return true;
				boolean rs = dir.getName().toLowerCase().endsWith(postfix);
				return rs;
			}
		});
		return sqlFileList;
	}

	public static Map<String, File> listAsMap(String sqldirPath, final String postfix){
		final Map<String, File> sqlfileMap = new HashMap<String, File>();
		File[] sqlFileList = listFiles(sqldirPath, postfix);
		if(LangUtils.isEmpty(sqlFileList)){
			return Collections.EMPTY_MAP;
		}
		for(File f : sqlFileList){
			sqlfileMap.put(f.getName(), f);
		}
		return sqlfileMap;
	}
	

	public static void writeToFile(BufferedImage image, String format, File file) {
		try {
			if (!ImageIO.write(image, format, file)) {
				throw new IOException("occur error when write image to file: " + file);
			}
		} catch (Exception e) {
			throw new BaseException("write image to file error.", e);
		}
	}

	public static void writeToStream(BufferedImage image, String format, OutputStream stream) {
		try {
			if (!ImageIO.write(image, format, stream)) {
				throw new IOException("occur error when write image to stream:" + stream);
			}
		} catch (Exception e) {
			throw new BaseException("write image to file error.", e);
		}
	}

	public static BufferedImage readAsBufferedImage(File imgFile) {
		try {
			return ImageIO.read(imgFile);
		} catch (Exception e) {
			throw new BaseException("read image to file error.", e);
		}
	}
	

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }
    
	public static void makeDirs(String path){
		makeDirs(path, !new File(path).isDirectory());
	}
    
	public static void makeDirs(String path, boolean file){
		File outDir = new File(path);
		if(file)
			outDir = outDir.getParentFile();
		
		if(!outDir.exists())
			if(!outDir.mkdirs())
				throw new RuntimeException("can't create output dir:"+path);
	}
    
	public static void makeDirs(File outDir, boolean file){
		if(file)
			outDir = outDir.getParentFile();
		
		if(!outDir.exists())
			if(!outDir.mkdirs())
				throw new RuntimeException("can't create output dir:"+outDir.getPath());
	}
	
	public static File getMavenProjectDir(){
		String baseDirPath = FileUtils.getResourcePath("");
		File baseDir = new File(baseDirPath);
		baseDir = baseDir.getParentFile().getParentFile();
		return baseDir;
	}
	
	public static ResourceAdapter adapterResource(Object resource){
		return new ResourceAdapterImpl(resource);
	}
	
	public static ResourceAdapter[] adapterResources(Object[] resource){
		if(LangUtils.isEmpty(resource))
			return EMPTY_RESOURCES;
		ResourceAdapterImpl[] reslist = new ResourceAdapterImpl[resource.length];
		int index = 0;
		for(Object obj : resource){
			reslist[index++] = new ResourceAdapterImpl(obj);
		}
		return reslist;
	}
	
	public static void createIfNotExists(File file){
		try {
			if(!file.exists()){
				if(!file.createNewFile()){
					throw new BaseException("create new file error!");
				}
			}
		} catch (IOException e) {
			throw new BaseException("create new file error!", e);
		}
	}
	
	public static void createOrDelete(File file){
		try {
			if(!file.exists()){
				if(!file.createNewFile()){
					throw new BaseException("create new file error!");
				}
			}else{
				if(!file.delete()){
					throw new BaseException("delete file error!");
				}
			}
		} catch (IOException e) {
			throw new BaseException("create new file error!", e);
		}
	}
	
	/*****
	 * 合并文件
	 * @param charset
	 * @param mergedFileName
	 * @param dir
	 * @param postfix
	 * @return
	 */
	public static File mergeFiles(String charset, String mergedFileName, String dir, String postfix){
		return mergeFiles(MergeFileConfig.build(charset, mergedFileName, dir, postfix, null));
	}

	public static File mergeFiles(MergeFileConfig config){
		File mergedFile = new File(config.getMergedFileName());
		createOrDelete(mergedFile);
		Writer writer = writer(mergedFile, config.getCharset());
		MergeFileContext context = new MergeFileContext(writer);
		try {
			MergeFileListener listener = config.getListener();
			int fileIndex = 0;
			JFishList<File> fileList = JFishList.wrap(config.getFiles());
			
			fileList.sort(new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					return o1.getPath().compareTo(o2.getPath());
				}
				
			});
			
			int totalLineIndex = 0;
			if(listener!=null)
				listener.onStart(context);
			
			for(File file : fileList){
				context.setFile(file);
				context.setFileIndex(fileIndex);
				
				if(listener!=null)
					listener.onFileStart(context);
				List<String> lines = readAsList(file, config.getCharset());
				int lineIndex = 0;
				for(String line : lines){
					context.setTotalLineIndex(totalLineIndex);
					if(listener!=null)
						listener.writeLine(context, line, lineIndex);
					else
						writer.write(line+NEW_LINE);
					totalLineIndex++;
				}
				if(listener!=null)
					listener.onFileEnd(context);
				fileIndex++;
			}
			if(listener!=null)
				listener.onEnd(context);
		} catch (Exception e) {
			throw new BaseException("merge file error", e);
		} finally{
			IOUtils.closeQuietly(writer);
		}
		return mergedFile;
	}
	
	public static long size(File...files){
		Assert.notEmpty(files);
		long size = 0;
		for(File f : files){
			size += f.length();
		}
		return size;
	}
	
	public static double sizeAsKb(File...files){
		return size(files)/1024.0;
	}
	
	public static double sizeAsMb(File...files){
		return size(files)/1024.0/1024.0;
	}
	
	/****
	 * 
	 * @param targetZipFilePath 目标文件
	 * @param file
	 * @return
	 */
	public static File zipfile(String targetZipFilePath, File file){
		return zipfile(targetZipFilePath, file, file.isFile());
	}
	
	public static File zipfile(String targetZipFilePath, File file, boolean isfile){
		if(isfile){
			return zipfiles(targetZipFilePath, file);
		}else{
			List<File> files = listFile(file);
			return zipfiles(targetZipFilePath, files.toArray(new File[0]));
		}
	}

	/****
	 * 压缩文件
	 * @param file
	 * @return
	 */
	public static File zipfile(File file){
		String targetZipFilePath = getNewFilenameBy(file, ".zip");
		return zipfile(targetZipFilePath, file);
	}

	public static String getNewFilenameBy(File file, String newPostfix){
		String targetZipFilePath = file.getParent() + File.separator + FileUtils.getFileNameWithoutExt(file.getPath()) + newPostfix;
		return targetZipFilePath;
	}
	
	public static File zipfiles(String targetZipFilePath, File...files){
		Assert.notEmpty(files);
		File zipfile = new File(targetZipFilePath);
		makeDirs(zipfile, true);
		ZipOutputStream zipout = null;
		try {
			zipout = new ZipOutputStream(new FileOutputStream(zipfile));
			for(File f : files){
				String entryName = f.getName();
				ZipEntry zipentry = new ZipEntry(entryName);
				zipout.putNextEntry(zipentry);
				copyFileToOutputStream(zipout, f);
			}
			zipout.finish();
		} catch (Exception e) {
			throw new BaseException("zip file error: " + e.getMessage(), e);
		} finally{
			close(zipout);
		}
		return zipfile;
	}
	
	public static String getJavaIoTmpdir(){
		return getJavaIoTmpdir(false);
	}
	
	public static String getJavaIoTmpdir(boolean convert){
		String dir = System.getProperty("java.io.tmpdir");
		return convert?convertDir(dir):dir;
	}
	
	public static void main(String[] args) {
		String file = "c:\\aa/bb\\ccsfd.txt";
		System.out.println(getFileNameWithoutExt(file));
		String file1 = "#path:/home/user/ccsfd.txt";
		System.out.println(getResourcePath(file1));
		System.out.println(System.getProperty("java.io.tmpdir"));
		System.out.println(getJavaIoTmpdir());
	}
}
