package org.onetwo.boot.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;

/**
 * @author wayshall
 * <br/>
 */
public class ImageCompressor {
	

	static public ImageCompressor of(ImageCompressorConfig config){
		ImageCompressor compressor = new ImageCompressor();
		compressor.setConfig(config);
		return compressor;
	}
	
	@Data
	@lombok.Builder
	@NoArgsConstructor
	@AllArgsConstructor
	static public class ImageCompressorConfig {
		Double scale;
		Double quality;
		Integer width;
		Integer height;
		List<String> fileTypes = Arrays.asList("jpg", "jpeg", "gif", "png", "bmp");
	}
	
	//8k
	private static final int BUF_SIZE = 8*1024;
	
	private ImageCompressorConfig config;
	private List<String> fileTypes = Arrays.asList("jpg", "jpeg", "gif", "png", "bmp");
	
	public boolean isCompressFile(String fileName){
		String fileType = FileUtils.getExtendName(fileName);
		return isCompressFileType(fileType);
	}
	
	public boolean isCompressFileType(String fileType){
		return fileTypes.contains(fileType.toLowerCase());
	}

	public InputStream compressStream(InputStream imageInputStream){
		return this.compressStream(imageInputStream, config);
	}
	
	public InputStream compressStream(InputStream imageInputStream, ImageCompressorConfig config){
		try {
			Builder<InputStream> builder = Thumbnails.fromInputStreams(Arrays.asList(imageInputStream));
			configBuilder(builder, config);
			ByteArrayOutputStream os = new ByteArrayOutputStream(BUF_SIZE);
			builder.toOutputStream(os);
			InputStream in = new ByteArrayInputStream(os.toByteArray());
			return in;
		} catch (Exception e) {
			throw new BaseException("compress image stream error: " + e.getMessage(), e);
		} finally{
			//由调用者自行处理
//			FileUtils.close(imageInputStream);
		}
	}
	
	public String compressTo(String imagePath, String targetPath){
		if(!isCompressFile(imagePath)){
			throw new BaseException("not support compress file type: " + FileUtils.getExtendName(imagePath));
		}
		Builder<File> builder = Thumbnails.fromFilenames(Arrays.asList(imagePath));
		configBuilder(builder, config);
		
		File targetFile = null;
		if(StringUtils.isBlank(targetPath)){
			File imageFile = new File(imagePath);
			String fileName = FileUtils.getFileNameWithoutExt(imageFile.getName()) + "-compress" + FileUtils.getExtendName(imageFile.getName(), true);
			fileName = FileUtils.newFileNameAppendRepeatCount(imageFile.getParent(), fileName);
			targetFile = new File(imageFile.getParentFile(), fileName);
		}else{
			targetFile = new File(targetPath);
		}
		try {
			builder.toFile(targetFile);
		} catch (IOException e) {
			throw new BaseException("compress image error: " + e.getMessage(), e);
		}
		return targetPath;
	}
	

	/***
	 * 
	 * @author wayshall
	 * @param builder
	 */
	protected void configBuilder(Builder<?> builder, ImageCompressorConfig config){
		if(config==null){
			return ;
		}
		if(config.getScale()!=null){
			builder.scale(config.getScale());//经过测试，可以大幅减少大小，比如 0.5
		}
		if(config.getWidth()!=null && config.getHeight()!=null){
			builder.size(config.getWidth(), config.getHeight());
		}else if(config.getWidth()!=null){
			builder.width(config.getWidth());
		}else if(config.getHeight()!=null){
			builder.height(config.getHeight());
		}
		if(config.getQuality()!=null){
			builder.outputQuality(config.getQuality());//压缩质量，比如 0.3，太低会影响质量，变成色块
		}
	}


	public void setConfig(ImageCompressorConfig config) {
		this.config = config;
	}

	public void setFileTypes(List<String> fileTypes) {
		this.fileTypes = fileTypes;
	}
	
	
}
