package org.onetwo.boot.core.web.mvc.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */

@ConfigurationProperties("jfish.mvc.accessLog")
@Data
public class AccessLogProperties {
	public static final String PREFIX = "jfish.mvc.accessLog";
	public static final String ENABLE_MVC_LOGGER_INTERCEPTOR = PREFIX+".enabled";
	/***
	 * jfish.mvc.accessLog.fileLogger=true
	 */
	public static final String FILE_LOGGER_ENABLED_KEY = PREFIX+".fileLogger";
	
	String loggerName;
	String seprator = " ";
	String[] pathPatterns;
	boolean logChangedDatas;
	/***
	 * log handler and params
	 */
	boolean logControllerDatas;
	
	/***
	 * 参数值记录的最大长度，超过的截断
	 */
	int logParameterValueMaxLength = 500;
}
