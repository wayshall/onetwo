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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.onetwo.common.utils.propconf.ResourceAdapterImpl;
import org.slf4j.Logger;

@SuppressWarnings("unchecked")
public class FileUtils {

	private static final Logger logger = MyLoggerFactory.getLogger(FileUtils.class);

	public static final String DEFAULT_CHARSET = "utf-8";
	public static final int DEFAULT_BUF_SIZE = 1024 * 4;
	public static final String PACKAGE = "package";
	public static final String PATH = "#path:";
	public static final String SLASH = "/";
	public static final char SLASH_CHAR = '/';

	public static ResourceAdapter[] EMPTY_RESOURCES = new ResourceAdapter[0];
	private static final Expression PLACE_HODER_EXP = Expression.DOLOR;

	private FileUtils() {
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
	    public static FileOutputStream openOutputStream(File file) throws IOException {
	        if (file.exists()) {
	            if (file.isDirectory()) {
	                throw new IOException("File '" + file + "' exists but is a directory");
	            }
	            if (file.canWrite() == false) {
	                throw new IOException("File '" + file + "' cannot be written to");
	            }
	        } else {
	            File parent = file.getParentFile();
	            if (parent != null && parent.exists() == false) {
	                if (parent.mkdirs() == false) {
	                    throw new IOException("File '" + file + "' could not be created");
	                }
	            }
	        }
	        return new FileOutputStream(file);
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
		
		logger.info("file path: "+ path);
		if(path==null){
			path = FileUtils.class.getClassLoader().getResource(fileName);
			if(path==null)
				return fileName;
		}
		realPath = path.getPath();
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
		List<String> datas = new ArrayList<String>();
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
	
	public static String readAsStringWith(File file, String charset, Map<String, Object> context){
		return StringUtils.join(readAsListWithMap(file, charset, context), "");
	}
	
	public static ClassLoader getClassLoader(){
		return ClassUtils.getDefaultClassLoader();
	}

	public static String getFileNameWithoutExt(String fileName) {
		if(fileName.indexOf('\\')!=-1)
			fileName = fileName.replace('\\', '/');
		int start = fileName.lastIndexOf('/');
		int index = fileName.lastIndexOf('.');
		if(index==-1)
			index = fileName.length();
		return fileName.substring(start+1, index);
	}

	public static String getFileName(String fileName) {
//		if(fileName.indexOf(File.separatorChar)!=-1)
//			fileName = fileName.replace('\\', SLASH_CHAR);
		int start = fileName.lastIndexOf(File.separatorChar);
		return fileName.substring(start+1);
	}

	public static String getExtendName(String fileName, boolean hasDot) {
		int index = fileName.lastIndexOf('.');
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

	public static File writeToDisk(File file, String filePath, String fileName) throws Exception {
		InputStream in = null;
		OutputStream out = null;
		File wfile = null;
		try {
			wfile = new File(filePath, fileName);
			int count = 1;
			if (!wfile.getParentFile().exists())
				wfile.getParentFile().mkdirs();
			String newFileName = fileName;
			while (wfile.exists()) {
				if (fileName.lastIndexOf(".") != -1) {
					String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
					newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "(" + count + ")." + ext;
				} else {
					newFileName = fileName + "(" + count + ")";
				}
				wfile = new File(filePath, newFileName);
				count++;
			}
			in = new FileInputStream(file);
			out = new FileOutputStream(wfile);
			byte[] buf = new byte[4096];
			int length = 0;
			while ((length = in.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
		} catch (Exception e) {
			throw new RuntimeException("occur error when write file : " + file.getPath(), e);
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
		return wfile;
	}

	public static void copyFile(File srcFile, File destFile) {
		Assert.notNull(srcFile);
		Assert.notNull(destFile);

		if (destFile.isDirectory())
			throw new BaseException("the file is directory: " + destFile.getPath());
		if (destFile.isHidden() || !destFile.canWrite())
			throw new BaseException("the file is hidden or readonly : " + destFile.getPath());

		BufferedInputStream fin = null;
		BufferedOutputStream fout = null;
		try {
//			System.out.println("creating the file : " + destFile.getPath());
			fin = new BufferedInputStream(new FileInputStream(srcFile));
			fout = new BufferedOutputStream(new FileOutputStream(destFile));
			byte[] buf = new byte[1024 * 5];
			int count = 0;
			while ((count = fin.read(buf, 0, buf.length)) != -1) {
				fout.write(buf, 0, count);
			}
//			System.out.println("file is created!");
		} catch (Exception e) {
			throw new BaseException("copy file error", e);
		} finally {
			IOUtils.closeQuietly(fin);
			IOUtils.closeQuietly(fout);
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

	public static List<File> listFile(File dirFile, Pattern pattern) {
		File[] files = dirFile.listFiles();
		if (files == null)
			return null;

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
	
	public static void writeTo(File file, InputStream in){
		OutputStream out = null;
		try {
			File parent = file.getParentFile();
			if(!parent.exists()){
				parent.mkdirs();
			}
			out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len = -1;
			while((len=in.read(buf))!=-1){
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			logger.error("write file error : " + file.getPath(), e);
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
        OutputStreamWriter w = null;
        try {
        	if(StringUtils.isBlank(charset)){
        		w = new OutputStreamWriter(openOutputStream(file));
        	}else{
        		w = new OutputStreamWriter(openOutputStream(file), charset);
        	}
            w.write(data);
        } catch(Exception e){
        	throw LangUtils.asBaseException("write data error : " + e.getMessage(), e);
        }finally {
            IOUtils.closeQuietly(w);
        }
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
		File outDir = new File(path);
		if(outDir.isFile())
			outDir = outDir.getParentFile();
		
		if(!outDir.exists())
			if(!outDir.mkdirs())
				throw new RuntimeException("can't create output dir:"+path);
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
	
	public static void main(String[] args) {
		String file = "c:\\aa/bb\\ccsfd.txt";
		System.out.println(getFileNameWithoutExt(file));
		String file1 = "#path:/home/user/ccsfd.txt";
		System.out.println(getResourcePath(file1));
	}
}
