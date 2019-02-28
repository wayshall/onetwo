package org.onetwo.boot.core.web.async;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@Data
@ConfigurationProperties(AsyncTaskProperties.PREFIX)
public class AsyncTaskProperties {
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".async";
	public static final String ENABLE_KEY = PREFIX+".enabled";

//	private int timeout = 60000;
	private int corePoolSize = 5;
	private int maxPoolSize = 50;
//	private int keepAliveSeconds = 60;
	private int queueCapacity = 100000;
}
