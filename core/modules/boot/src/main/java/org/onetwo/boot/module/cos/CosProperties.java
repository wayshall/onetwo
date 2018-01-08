package org.onetwo.boot.module.cos;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.region.Region;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties("jfish.cos")
@Data
public class CosProperties {
	String downloadEndPoint;
	String uploadEndPoint;
	String endPoint;
	/***
	 * https://cloud.tencent.com/document/product/436/6224
	 * 如：<bucketname-APPID>.cos.ap-guangzhou.myqcloud.com
	 */
	private String regionName;
    String appid;
    String accessKey;
    String secretKey;
    String bucketName;
    boolean createBucket;

	ClientConfig client = new ClientConfig(null);
	
	public String getDownloadEndPoint() {
		if(StringUtils.isBlank(downloadEndPoint)){
			return endPoint;
		}
		return downloadEndPoint;
	}

	public String getUploadEndPoint() {
		if(StringUtils.isBlank(uploadEndPoint)){
			return endPoint;
		}
		return uploadEndPoint;
	}

	public ClientConfig getClient(){
		if(client.getRegion()==null){
			client.setRegion(new Region(regionName));
		}
		return client;
	}
	
	public String getBucketName(){
		return getBucketName(bucketName);
	}
	public String getBucketName(String bucketName){
		Assert.hasText(appid);
		String postfix = "-"+appid;
		if(bucketName.endsWith(postfix)){
			return bucketName;
		}
		return bucketName+postfix;
	}
	
	public String getDownloadUrl(String key){
		String url = buildUrl(true, getDownloadEndPoint(), getBucketName(), key);
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
