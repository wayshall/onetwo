package org.onetwo.common.spring.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.onetwo.common.utils.file.FileUtils;
import org.springframework.core.io.InputStreamSource;

public class SmbToByteArrayInputStreamSource implements InputStreamSource {
	
	private InputStream inputStream;
	
	public SmbToByteArrayInputStreamSource(String smbPath){
		if(!FileUtils.isSmbPath(smbPath)){
			throw new IllegalArgumentException("It's not a smb path: " + smbPath);
		}
		InputStream in = FileUtils.newInputStream(smbPath);
		byte[] bytes = FileUtils.readFileToByteArray(in);
		inputStream = new ByteArrayInputStream(bytes);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return inputStream;
	}
	
	

}
