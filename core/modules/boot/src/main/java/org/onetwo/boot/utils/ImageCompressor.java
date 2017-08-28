package org.onetwo.boot.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

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
	
	//8k
	private static final int BUF_SIZE = 8*1024;
	
	private double scale = 1;
	private Double quality = 0.1D;
	private Integer width;
	private Integer height;
	private List<String> fileTypes = Arrays.asList("jpg", "jpeg", "gif", "png", "bmp");
	
	public boolean isCompressFile(String fileName){
		String fileType = FileUtils.getExtendName(fileName);
		return isCompressFileType(fileType);
	}
	
	public boolean isCompressFileType(String fileType){
		return fileTypes.contains(fileType.toLowerCase());
	}

	public InputStream compressStream(InputStream imageInputStream){
		try {
			Builder<InputStream> builder = Thumbnails.fromInputStreams(Arrays.asList(imageInputStream));
			configBuilder(builder);
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
		if(isCompressFile(imagePath)){
			throw new BaseException("not support compress file type: " + FileUtils.getExtendName(imagePath));
		}
		Builder<File> builder = Thumbnails.fromFilenames(Arrays.asList(imagePath));
		configBuilder(builder);
		
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
	

	protected void configBuilder(Builder<?> builder){
		builder.scale(scale);
		if(quality!=null){
			builder.outputQuality(0.01);
		}
		if(width!=null &&height!=null){
			builder.size(width, height);
		}
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public void setQuality(Double quality) {
		this.quality = quality;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setFileTypes(List<String> fileTypes) {
		this.fileTypes = fileTypes;
	}
	
	
}
