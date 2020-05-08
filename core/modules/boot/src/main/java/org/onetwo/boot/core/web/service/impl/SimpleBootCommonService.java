package org.onetwo.boot.core.web.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.CompressConfig;
import org.onetwo.boot.core.config.BootSiteConfig.StoreType;
import org.onetwo.boot.core.web.service.BootCommonService;
import org.onetwo.boot.core.web.service.FileStorerListener;
import org.onetwo.boot.core.web.utils.UploadOptions;
import org.onetwo.boot.utils.ImageCompressor;
import org.onetwo.boot.utils.ImageCompressor.ImageCompressorConfig;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.file.FileStorer;
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
	private List<FileStorer> fileStorers;
	
	@Autowired(required=false)
	private FileStorerListener fileStorerListener;
	
	@Autowired(required=false)
	private ImageCompressor imageCompressor;
	
	/*@Autowired(required=false)
	private StoreFilePathStrategy storeFilePathStrategy;*/
	
	//少于等于0则一律不压缩
//	private int compressThresholdSize = -1;
	private CompressConfig compressConfig;
	@Autowired
	private BootSiteConfig siteConfig;
	
	/***
	 * 上传的根目录
	 */
	private String fileStoreBaseDir = "";
	
	public void setFileStoreBaseDir(String fileStoreBaseDir) {
		this.fileStoreBaseDir = fileStoreBaseDir;
	}

//	public void setFileStorer(FileStorer fileStorer) {
//		this.fileStorer = fileStorer;
//	}

	
	protected FileStorer getFileStorer() {
		StoreType type = siteConfig.getUpload().getStoreType();
		return this.getFileStorer(type);
	}
	
	protected FileStorer getFileStorer(StoreType storeType) {
		return this.fileStorers.stream().filter(st -> {
			return st.getStoreType().equalsIgnoreCase(storeType.name());
		}).findAny().orElseThrow(()-> {
			return new BaseException("file store not found: " + storeType.name());
		});
	}


	/****
	 * 目前通过设置阈值决定是否压缩
	 * 若需要控制压缩推荐使用uploadFile(UploadOptions options)方法
	 * ImageUtils.readBufferedImage
	 * @author wayshall
	 * @param module
	 * @param file
	 * @return
	
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
//			String storePath = StringUtils.appendEndWithSlash(fileStoreRootPath) + module;
			StoringFileContext ctx = new StoringFileContext(module, in, file.getOriginalFilename());
			ctx.setFileStoreBaseDir(fileStoreBaseDir);
			return ctx;
		} catch (IOException e) {
			throw new BaseException("create StoringFileContext error: " + file.getOriginalFilename(), e);
		} finally {
//			IOUtils.closeQuietly(in);
		}
	} */

	/***
	 * 此方法安装全局配置来决定是否压缩
	 */
	@Override
	public FileStoredMeta uploadFile(String module, MultipartFile file){
		Assert.notNull(file, "file can not be null");
		
		UploadOptions options = new UploadOptions(module, file);
//		options.setCompressConfig(compressConfig);
		return uploadFile(options);
		/*StoringFileContext context = create(module, file);
		FileStoredMeta meta = fileStorer.write(context);
		if(fileStorerListener!=null){
			fileStorerListener.afterFileStored(meta);
		}
		return meta;*/
	}


	@Override
	public FileStoredMeta uploadFile(UploadOptions options){
//		Assert.notNull(options.getModule(), "options.module can not be null");
		Assert.notNull(options.getMultipartFile(), "options.multipartFile can not be null");
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
		BootStoringFileContext context = new BootStoringFileContext(options.getModule(), 
																in, 
																options.getMultipartFile().getOriginalFilename());
		context.setResizeConfig(options.getResizeConfig());
		context.setFileStoreBaseDir(fileStoreBaseDir);
//		context.setStoreFilePathStrategy(storeFilePathStrategy);
		context.setKey(options.getKey());
		context.setWaterMaskConfig(options.getWaterMaskConfig());
		
		StoreType type = options.getStoreType();
		if (type==null) {
			type = siteConfig.getUpload().getStoreType();
		}
		
		FileStorer fileStorer = getFileStorer(type);
		FileStoredMeta meta = fileStorer.write(context);
		if(fileStorerListener!=null){
			fileStorerListener.afterFileStored(meta);
		}
		return meta;
	}
	
	
	public void readFileTo(String accessablePath, OutputStream output){
		this.getFileStorer().readFileTo(accessablePath, output);
	}
	
	public void delete(String key) {
		this.getFileStorer().delete(key);
	}

	public void setCompressConfig(CompressConfig compressConfig) {
		this.compressConfig = compressConfig;
	}

	public void setFileStorers(List<FileStorer> fileStorers) {
		this.fileStorers = fileStorers;
	}
	
}
