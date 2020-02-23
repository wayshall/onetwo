package org.onetwo.boot.module.cos;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.region.Region;

import lombok.Data;

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
    boolean alwaysStoreFullPath;
    /***
     * 是否启用异步上传
     */
    boolean enabledAsyncUpload = false;

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

	private String getBucketName(){
		return bucketName;
	}
	
	public String getAppBucketName(){
		return getAppBucketName(bucketName);
	}
	public String getAppBucketName(String bucketName){
		Assert.hasText(appid, "appid must has text");
		String postfix = "-"+appid;
		if(bucketName.endsWith(postfix)){
			return bucketName;
		}
		return bucketName+postfix;
	}
	
	public String getDownloadUrl(String key){
		String url = buildUrl(true, getDownloadEndPoint(), getAppBucketName(), key);
		return url;
	}
	

	public static String buildUrl(boolean https, String endpoint, String bucketName, String key){
		StringBuilder url = new StringBuilder(https?"https":"http");
		url.append("://");
		if (!endpoint.startsWith(bucketName)) {
			url.append(bucketName)
				.append(".");
		}
		url.append(endpoint)
			.append(key);
		return url.toString();
	}
}
