package org.onetwo.boot.module.alioss;

import java.util.Arrays;
import java.util.List;

import org.onetwo.boot.module.alioss.image.WatermarkAction.WatermarkFonts;
import org.onetwo.boot.module.alioss.video.SnapshotProperties;
import org.onetwo.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.aliyun.oss.ClientConfiguration;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties("jfish.alioss")
@Data
public class OssProperties {
	String downloadEndPoint;
	String endpoint;
    String accessKeyId;
    String accessKeySecret;
    String bucketName;
    boolean createBucket;

	ClientConfiguration client = new ClientConfiguration();
	
	ResizeProperties resize = new ResizeProperties();
	WaterMaskProperties watermask = new WaterMaskProperties();
	SnapshotProperties snapshot = new SnapshotProperties();
	
	public String getUrl(String key){
		String url = buildUrl(false, getDownloadEndPoint(), bucketName, key); //RequestUtils.HTTP_KEY + bucketName + "." + endpoint + "/" + key;
		return url;
	}
	
	public String getDownloadEndPoint() {
		if (StringUtils.isNotBlank(downloadEndPoint)) {
			return downloadEndPoint;
		}
		return endpoint;
	}

	public static String buildUrl(boolean https, String endpoint, String bucketName, String key){
		StringBuilder url = new StringBuilder(https?"https":"http");
		url.append("://")
			.append(bucketName)
			.append(".")
			.append(endpoint)
			.append("/")
			.append(key);
		return url.toString();
	}
	
	@lombok.Data
	public static class WaterMaskProperties {
		boolean enabled;
		String text;
		String type = WatermarkFonts.FANGZHENGKAITI.getValue();
		String color = "FFFFFF";
		String location;
		/***
		 * 水印图片预处理
		用户在打水印时，可以对水印图片进行预处理，支持的预处理操作有：图片缩放，图片裁剪（不支持内切圆)，图片旋转（具体内容请直接查看文档相关章节）。
		在“resize”操作下还额外支持一个参数：P（大写P），表示水印图片按主图的比例进行处理，取值范围为[1, 100]，表示百分比。
		对panda.png按30%缩放。 那么水印文件是：panda.png?x-oss-process=image/resize,P_30
		 */
		String image;
		Integer size;
		Integer shadow;
		Integer transparency;
		Integer x;
		Integer y;
		Integer voffset;
		Integer fill;
		
		List<String> supportFileTypes = Arrays.asList("jpg", "jpeg", "gif", "png", "bmp");
		
		public boolean isSupportFileType(String postfix) {
			return supportFileTypes.contains(postfix.toLowerCase());
		}
	}

}
