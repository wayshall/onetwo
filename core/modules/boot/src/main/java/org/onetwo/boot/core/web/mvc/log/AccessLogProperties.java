package org.onetwo.boot.core.web.mvc.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */

@ConfigurationProperties(AccessLogProperties.PREFIX)
@Data
public class AccessLogProperties {
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mvc.access-log";
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
