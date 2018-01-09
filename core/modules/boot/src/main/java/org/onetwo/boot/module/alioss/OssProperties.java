package org.onetwo.boot.module.alioss;

import lombok.Data;

import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.aliyun.oss.ClientConfiguration;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties("jfish.alioss")
@Data
public class OssProperties {
	String endpoint;
    String accessKeyId;
    String accessKeySecret;
    String bucketName;
    boolean createBucket;

	ClientConfiguration client = new ClientConfiguration();
	
	public String getUrl(String key){
		String url = RequestUtils.HTTP_KEY + bucketName + "." + endpoint + "/" + key;
		return url;
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
