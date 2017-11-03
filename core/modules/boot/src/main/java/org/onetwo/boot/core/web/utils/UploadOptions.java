package org.onetwo.boot.core.web.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;

import org.onetwo.boot.core.config.BootSiteConfig.CompressConfig;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wayshall
 * <br/>
 */
@Builder
@AllArgsConstructor
public class UploadOptions {
	final private String module;
	final private MultipartFile multipartFile;
	private CompressConfig compressConfig;
	/**
	 * 如果指定了key，会覆盖
	 */
	final private String key;
	
	public UploadOptions(String module, MultipartFile multipartFile) {
		this(null, module, multipartFile);
	}
	
	public UploadOptions(String key, String module, MultipartFile file) {
		super();
		this.module = module;
		this.multipartFile = file;
		this.key = key;
	}
	
	public boolean isCompressFile(){
		boolean compress = this.compressConfig != null;
		if(!compress){
			return compress;
		}
		if(StringUtils.isNotBlank(this.compressConfig.getThresholdSize())){
			int thresholdSize = FileUtils.parseSize(this.compressConfig.getThresholdSize());
			compress = multipartFile.getSize() > thresholdSize;
		}
		return compress;
	}

	public String getModule() {
		return module;
	}

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}
	
	public void setCompressConfig(CompressConfig compressConfig) {
		this.compressConfig = compressConfig;
	}

	public CompressConfig getCompressConfig() {
		return compressConfig;
	}

	public String getKey() {
		return key;
	}
	
}
