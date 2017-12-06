package org.onetwo.boot.core.web.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.onetwo.boot.core.web.service.BootCommonService;
import org.onetwo.boot.core.web.service.FileStorerListener;
import org.onetwo.boot.core.web.utils.UploadOptions;
import org.onetwo.boot.utils.ImageCompressor;
import org.onetwo.boot.utils.ImageCompressor.ImageCompressorConfig;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.file.StoreFilePathStrategy;
import org.onetwo.common.file.StoringFileContext;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.copier.CopyUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

@Transactional
public class SimpleBootCommonService implements BootCommonService {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FileStorer<?> fileStorer;
	
	@Autowired(required=false)
	private FileStorerListener fileStorerListener;
	
	@Autowired(required=false)
	private ImageCompressor imageCompressor;
	
	@Autowired(required=false)
	private StoreFilePathStrategy storeFilePathStrategy;
	
	//少于等于0则一律不压缩
	private int compressThresholdSize = -1;
	
	/****
	 * 目前通过设置阈值决定是否压缩
	 * 若需要控制压缩推荐使用uploadFile(UploadOptions options)方法
	 * ImageUtils.readBufferedImage
	 * @author wayshall
	 * @param module
	 * @param file
	 * @return
	 */
	private StoringFileContext create(String module, MultipartFile file){
		InputStream in = null;
		try {
			if(imageCompressor!=null && 
					(compressThresholdSize>0 && file.getSize() > compressThresholdSize) && 
					imageCompressor.isCompressFile(file.getOriginalFilename())){
				in = imageCompressor.compressStream(file.getInputStream());
			}else{
				in = file.getInputStream();
			}
			return new StoringFileContext(module, in, file.getOriginalFilename());
		} catch (IOException e) {
			throw new BaseException("create StoringFileContext error: " + file.getOriginalFilename());
		} finally {
//			IOUtils.closeQuietly(in);
		}
	}

	/***
	 * 此方法安装全局配置来决定是否压缩
	 */
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


	@Override
	public FileStoredMeta uploadFile(UploadOptions options){
		Assert.notNull(options.getModule());
		Assert.notNull(options.getMultipartFile());
		InputStream in;
		try {
			in = options.getMultipartFile().getInputStream();
		} catch (IOException e) {
			throw new BaseException("obtain file stream error: " + options.getMultipartFile().getOriginalFilename());
		}
		if(options.isCompressFile()){
			ImageCompressor imageCompressor = this.imageCompressor;
			if(imageCompressor==null){
				imageCompressor = new ImageCompressor();
			}
			ImageCompressorConfig config = CopyUtils.copy(ImageCompressorConfig.class, options.getCompressConfig());
			in = imageCompressor.compressStream(in, config);
		}
		StoringFileContext context = StoringFileContext.create(options.getModule(), 
																in, 
																options.getMultipartFile().getOriginalFilename());
		context.setStoreFilePathStrategy(storeFilePathStrategy);
		context.setKey(options.getKey());
		FileStoredMeta meta = fileStorer.write(context);
		if(fileStorerListener!=null){
			fileStorerListener.afterFileStored(meta);
		}
		return meta;
	}
	
	
	public void readFileTo(String accessablePath, OutputStream output){
		this.fileStorer.readFileTo(accessablePath, output);
	}

	public void setCompressThresholdSize(String compressThresholdSize) {
		this.compressThresholdSize = FileUtils.parseSize(compressThresholdSize, -1);
	}
	
}
