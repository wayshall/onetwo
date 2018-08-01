package org.onetwo.boot.core.web.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wayshall
 * <br/>
 */
public class SimpleMultipartFile implements MultipartFile {
	
	private String name;
	final private String originalFilename;
	private String contentType;
	final private byte[] content;
	
	public SimpleMultipartFile(String originalFilename, Resource resource) {
		this(originalFilename, FileUtils.toByteArray(SpringUtils.getInputStream(resource)));
	}
	
	public SimpleMultipartFile(String originalFilename, InputStream inputStream) {
		this(originalFilename, FileUtils.toByteArray(inputStream));
	}
	
	public SimpleMultipartFile(String originalFilename, byte[] content) {
		this(originalFilename, originalFilename, content);
	}
	public SimpleMultipartFile(String fieldName, String originalFilename, byte[] content) {
		super();
		Assert.notNull(originalFilename, "originalFilename");
		Assert.notNull(content, "content");
		this.originalFilename = originalFilename;
		this.content = content;
		this.name = fieldName;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setContentType(String contentType) {
		this.contentType = contentType;
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
