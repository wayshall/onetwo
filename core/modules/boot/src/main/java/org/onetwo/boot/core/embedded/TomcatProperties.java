package org.onetwo.boot.core.embedded;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties("jfish.tomcat")
@Data
public class TomcatProperties {
	public static final String ENABLED_CUSTOMIZER_TOMCAT = "enabled";
	
	int backlog = 500;
	int acceptCount = backlog;
	int maxConnections = -1;
	int connectionTimeout = 60000;
	int asyncTimeout = 60000;
	
	/***
	 * 为true，则上传文件时使用connectionTimeout
	 * 为false，则使用connectionUploadTimeout
	 * 默认为true
	 */
//	boolean disableUploadTimeout = true;
	/***
	 * 使用容器默认connectionUploadTimeout=300000,300秒
	 */
	int connectionUploadTimeout = -1;

}
