package org.onetwo.boot.core.web.service.impl;

import java.io.IOException;

import org.onetwo.boot.core.web.service.BootCommonService;
import org.onetwo.boot.core.web.service.FileStorerListener;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.StoringFileContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

@Transactional
public class SimpleBootCommonService implements BootCommonService {
	@Autowired
	private FileStorer<?> fileStorer;
	
	@Autowired(required=false)
	private FileStorerListener fileStorerListener;
	
	private StoringFileContext create(String module, MultipartFile file){
		try {
			return new StoringFileContext(module, file.getInputStream(), file.getOriginalFilename());
		} catch (IOException e) {
			throw new BaseException("create StoringFileContext error: " + file.getOriginalFilename());
		}
	}

	@Override
	public FileStoredMeta uploadFile(String module, MultipartFile file){
		Assert.notNull(file);
		Assert.notNull(fileStorer);
		StoringFileContext context = create(module, file);
		FileStoredMeta meta = fileStorer.write(context);
		if(fileStorerListener!=null){
			fileStorerListener.afterFileStored(meta);
		}
		return meta;
	}
}
