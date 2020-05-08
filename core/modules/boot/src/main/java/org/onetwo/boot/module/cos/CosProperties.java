package org.onetwo.boot.module.cos;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.region.Region;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(CosProperties.PREFIX)
@Data
public class CosProperties {
	public static final String PREFIX = BootJFishConfig.PREFIX + ".cos";
	public static final String ENABLED_KEY = PREFIX + ".bucketName";
	
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
	
	/***
	 * 此功能需要配置腾讯云mps工作流，设置触发目录和任务
	 */
	VideoConfig video = new VideoConfig();
	
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

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	
	public String getEndPoint() {
		return this.endPoint;
	}
	
	public String getEndpoint() {
		return endPoint;
	}

	public void setEndpoint(String endpoint) {
		this.endPoint = endpoint;
	}
	
	@Data
	public static class VideoConfig {
		boolean enabled = false;
		/***
		 * 腾讯云可以截取多张图片，_0表示的是第一张截图
		 */
		String snapshotFileName = "{filename}-cover_0.jpg";
		String waterMaskFileName = "{filename}wm.{format}";
		/***
		 * 上传以下后缀的文件时，触发返回的文件名称处理
		 */
		List<String> postfix = Arrays.asList("mp4", "flv");
	}
}
