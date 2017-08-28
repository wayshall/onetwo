package org.onetwo.boot.core.web.service;

import java.io.OutputStream;

import org.onetwo.common.file.FileStoredMeta;
import org.springframework.web.multipart.MultipartFile;

public interface BootCommonService {

	FileStoredMeta uploadFile(String module, MultipartFile file);
	
	void readFileTo(String accessablePath, OutputStream output);

}