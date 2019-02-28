package org.onetwo.boot.core.web.mvc.log;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */

@ConfigurationProperties(org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mvc.accessLog")
@Data
public class AccessLogProperties {
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mvc.accessLog";
	public static final String ENABLE_MVC_LOGGER_INTERCEPTOR = PREFIX+".enabled";
	
	String loggerName;
	String seprator = " ";
	String[] pathPatterns;
	boolean logChangedDatas;
	/***
	 * log handler and params
	 */
	boolean logControllerDatas;
}
