package org.onetwo.boot.core.web.service;

import org.onetwo.common.file.FileStoredMeta;
import org.springframework.web.multipart.MultipartFile;

public interface BootCommonService {

	FileStoredMeta uploadFile(String module, MultipartFile file);

}