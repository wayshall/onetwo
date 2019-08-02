package org.onetwo.boot.core.web.utils;

import org.onetwo.boot.core.config.BootSiteConfig.CompressConfig;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wayshall
 * <br/>
 */
@Builder
@AllArgsConstructor
public class UploadOptions {
	final private String module;
	final private MultipartFile multipartFile;
	/***
	 * 上传的时候是否压缩图片本身，统一实现
	 */
	private CompressConfig compressConfig;
	
	/****
	 * 是否在上传的同事生成一张缩略图，各自storer实现
	 * 暂时支持的fileStore：
	 * ossFileStorer
	 */
	@Setter
	@Getter
	private ResizeConfig resizeConfig;
	
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
	
	@NoArgsConstructor
	@Data
	static public class ResizeConfig {
		/***
		 * 指定目标缩略图的宽度
		 */
		Integer width;
		Integer heigh;

		@Builder
		public ResizeConfig(Integer width, Integer heigh) {
			super();
			this.width = width;
			this.heigh = heigh;
		}
	}

	@Data
	public static class WaterMaskConfig {
		String text;
		Integer size;
		String type;
	}
}
