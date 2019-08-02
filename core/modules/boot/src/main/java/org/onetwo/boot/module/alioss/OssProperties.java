package org.onetwo.boot.module.alioss;

import org.onetwo.boot.module.alioss.WatermarkAction.WatermarkFonts;
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
		String image;
		Integer size;
		Integer shadow;
		Integer transparency;
		Integer x;
		Integer y;
		Integer voffset;
		Integer fill;
	}

}
