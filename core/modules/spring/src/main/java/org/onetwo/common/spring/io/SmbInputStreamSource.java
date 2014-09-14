package org.onetwo.common.spring.io;

import java.io.IOException;
import java.io.InputStream;

import org.onetwo.common.utils.FileUtils;
import org.springframework.core.io.InputStreamSource;

public class SmbInputStreamSource implements InputStreamSource {
	
	private InputStream inputStream;
	
	public SmbInputStreamSource(String smbPath){
		if(!FileUtils.isSmbPath(smbPath)){
			throw new IllegalArgumentException("It's not a smb path: " + smbPath);
		}
		inputStream = FileUtils.newInputStream(smbPath);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return inputStream;
	}
	
	

}
