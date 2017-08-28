package org.onetwo.boot.core.web.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.boot.core.web.service.BootCommonService;
import org.onetwo.boot.core.web.service.FileStorerListener;
import org.onetwo.boot.utils.ImageCompressor;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.FileUtils;
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
	
	@Autowired(required=false)
	private ImageCompressor imageCompressor;
	
	//少于0则所有大小一律压缩
	private int compressThresholdSize = -1;
	
	private StoringFileContext create(String module, MultipartFile file){
		InputStream in = null;
		try {
			if(imageCompressor!=null && 
					(compressThresholdSize<0 || file.getSize() > compressThresholdSize) && 
					imageCompressor.isCompressFile(file.getOriginalFilename())){
				in = imageCompressor.compressStream(file.getInputStream());
			}else{
				in = file.getInputStream();
			}
			return new StoringFileContext(module, in, file.getOriginalFilename());
		} catch (IOException e) {
			throw new BaseException("create StoringFileContext error: " + file.getOriginalFilename());
		} finally {
			IOUtils.closeQuietly(in);
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

	public void setCompressThresholdSize(String compressThresholdSize) {
		this.compressThresholdSize = FileUtils.parseSize(compressThresholdSize);
	}
	
}
