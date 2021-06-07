package org.onetwo.boot.core.web.service;

import java.io.OutputStream;

import org.onetwo.boot.core.config.BootSiteConfig.StoreType;
import org.onetwo.boot.core.web.utils.UploadOptions;
import org.onetwo.common.file.FileStoredMeta;
import org.springframework.web.multipart.MultipartFile;

public interface BootCommonService {

	FileStoredMeta uploadFile(String module, MultipartFile file);
	
	FileStoredMeta uploadFile(UploadOptions options);
	
	void delete(String key);
	
	void readFileTo(String accessablePath, OutputStream output);

	boolean isObjectExist(StoreType type, String objectPath);
	
}