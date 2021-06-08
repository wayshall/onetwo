package org.onetwo.boot.core.embedded;

import lombok.Data;

import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".tomcat")
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
	String connectionUploadTimeoutTime;
	
	/***
	 * 是否使用apr协议
	 */
	boolean aprProtocol;
	
	/****
	 * Tomcat在 7.0.73, 8.0.39, 8.5.7 版本后，在http解析时做了严格限制。
RFC3986文档规定，请求的Url中只允许包含英文字母（a-zA-Z）、数字（0-9）、-_.~4个特殊字符以及所有保留字符。
在conf/server.xml中的<Connector>节点中，添加2个属性：
relaxedPathChars="|{}[],"
relaxedQueryChars="|{}[],"
这2个属性，可以接收任意特殊字符的组合，根据需要可以自行增减。
相关的文档说明：
	 * https://tomcat.apache.org/tomcat-8.5-doc/config/systemprops.html
	 */
	String relaxedQueryChars = "-_.|{}[],";
	String relaxedPathChars = "-_.|{}[],";
	
	public int getConnectionUploadTimeout(){
		if(StringUtils.isBlank(connectionUploadTimeoutTime)){
			return connectionUploadTimeout;
		}else{
			Long val = LangOps.timeToMills(connectionUploadTimeoutTime, Long.valueOf(connectionUploadTimeoutTime));
			this.connectionUploadTimeout = val.intValue();
		}
		return connectionUploadTimeout;
	}

}
