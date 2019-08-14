package org.onetwo.common.apiclient.resouce;

import org.springframework.core.io.ByteArrayResource;

/**
 * 某些情况下需要给byteArrayResource一个 filename，
 * 比如微信要求提交表单时通过header方式提供文件名：Content-Disposition=[form-data; name="buffer"; filename="kq.jpg"]
 * 如果直接传ByteArrayResource的话就会出错
 * @author wayshall
 * <br/>
 */
public class FileNameByteArrayResource extends ByteArrayResource {

	private String filename;
	private String contentType;
	

	public FileNameByteArrayResource(byte[] byteArray) {
		super(byteArray);
	}
	
	public FileNameByteArrayResource(String filename, byte[] byteArray) {
		this(filename, byteArray, null);
	}
	public FileNameByteArrayResource(String filename, byte[] byteArray, String description) {
		super(byteArray, description);
		this.filename = filename;
	}
	
	@Override
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}
