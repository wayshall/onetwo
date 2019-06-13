package org.onetwo.boot.module.alioss;

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
}
