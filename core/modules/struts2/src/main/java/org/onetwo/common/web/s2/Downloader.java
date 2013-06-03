package org.onetwo.common.web.s2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.exception.WebException;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.web.utils.StrutsUtils;

/***
 * 下载辅助类
 * 
 * @author weishao
 *
 */
public class Downloader {
	
	public static final String DEFAULT_CONTENT_TYPE = "application/download";
	
	private File file;
	private String fileName;
	private String contentType;

	private InputStream stream;
	
	public Downloader(File file) {
		this(file, null, null);
	}
	
	public Downloader(File file, String name) {
		this(file, name, null);
	}
	
	public Downloader(String path, String name) {
		this(new File(path), name, null);
	}
	
	public Downloader(File file, String fileName, String contentType) {
		if(file==null)
			throw new WebException("file 不能为null！");
		
		this.file = file;
		this.fileName = fileName;
		this.contentType = contentType;
	}

	public Downloader(InputStream stream, String fileName) {
		this(stream, fileName, null);
	}
	
	public Downloader(InputStream stream, String fileName, String contentType) {
		if(stream == null)
			throw new WebException("stream 不能为null！");
		
		this.fileName = fileName;
		this.contentType = contentType;
		this.stream = stream;
	}
	
	public String getFileName() throws UnsupportedEncodingException {
		if(StringUtils.isBlank(fileName))
			fileName = file.getName();
		if(fileName.indexOf('.')==-1){
			fileName = MyUtils.append(fileName, ".", MyUtils.getFileExtName(file.getPath()));
		}
		return new String(fileName.getBytes("GBK"), "ISO8859-1");
	}

	public String getContentType() {
		if(StringUtils.isBlank(this.contentType))
			this.contentType = StrutsUtils.getContentType(file.getName());
		return StringUtils.isBlank(contentType)?contentType:DEFAULT_CONTENT_TYPE;
	}
	
	public InputStream getInputStream() throws FileNotFoundException{
		if(stream == null) {
			stream = new FileInputStream(file);
		}
		return stream;
	}
	

}
