package org.onetwo.boot.core.web.utils;

import org.onetwo.boot.core.config.BootSiteConfig.CompressConfig;
import org.onetwo.boot.core.config.BootSiteConfig.StoreType;
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
@NoArgsConstructor
public class UploadOptions {
	private String module;
	private MultipartFile multipartFile;
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
	
	@Setter
	@Getter
	private SnapshotConfig snapshotConfig;

	@Setter
	@Getter
	private WaterMaskConfig waterMaskConfig;
	
	/**
	 * 如果指定了key，会覆盖
	 */
	private String key;

	@Setter
	@Getter
	StoreType storeType;
	
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
			//少于等于0则一律不压缩
			int thresholdSize = FileUtils.parseSize(this.compressConfig.getThresholdSize());
			compress = thresholdSize>0 && multipartFile.getSize() > thresholdSize;
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
		String image;

		@Builder
		public WaterMaskConfig(String text, Integer size, String type, String image) {
			super();
			this.text = text;
			this.size = size;
			this.type = type;
			this.image = image;
		}
		
		
	}
	
	/***
	 * 视频截图配置
	 * @author way
	 *
	 */
	@Data
	public static class SnapshotConfig {
		/***
		 * 截图时间	单位ms，[0,视频时长]
		 */
		int time;
		/***
		 * 截图宽度，如果指定为0则自动计算	像素值：[0,视频宽度]
		 */
		int width;
		int height;
		
		@Builder
		public SnapshotConfig(int time, int width, int height) {
			super();
			this.time = time;
			this.width = width;
			this.height = height;
		}
	}
}
