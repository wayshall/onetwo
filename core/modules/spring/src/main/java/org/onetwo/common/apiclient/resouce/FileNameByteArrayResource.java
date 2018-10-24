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
	
	public FileNameByteArrayResource(String filename, byte[] byteArray) {
		super(byteArray);
		this.filename = filename;
	}
	
	@Override
	public String getFilename() {
		return filename;
	}
}
