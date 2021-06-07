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
