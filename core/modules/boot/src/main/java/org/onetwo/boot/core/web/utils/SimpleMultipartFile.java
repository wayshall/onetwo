package org.onetwo.boot.core.web.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.onetwo.common.file.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wayshall
 * <br/>
 */
public class SimpleMultipartFile implements MultipartFile {
	
	private String name;
	private String originalFilename;
	private String contentType;
	private byte[] content;
	
	public SimpleMultipartFile(String originalFilename, InputStream inputStream) {
		this(originalFilename, FileUtils.toByteArray(inputStream));
	}
	
	public SimpleMultipartFile(String originalFilename, byte[] content) {
		super();
		this.originalFilename = originalFilename;
		this.content = content;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getOriginalFilename() {
		return originalFilename;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public boolean isEmpty() {
		return content.length==0;
	}

	@Override
	public long getSize() {
		return content.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return content;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(content);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		throw new UnsupportedOperationException();
	}

}
