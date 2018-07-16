package org.onetwo.common.apiclient.resouce;

import org.springframework.core.io.ByteArrayResource;

/**
 * 某些情况下需要给byteArrayResource一个 filename
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
