package org.onetwo.boot.core.web.async;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 
jfish:
	mvc: 
		async:
			timeout: 30000
 * @author wayshall
 * <br/>
 */
@Data
@ConfigurationProperties(MvcAsyncProperties.PREFIX)
public class MvcAsyncProperties {
	public static final String PREFIX = org.onetwo.boot.core.config.BootJFishConfig.ZIFISH_CONFIG_PREFIX+ ".mvc.async";
	public static final String ENABLE_KEY = PREFIX+".enabled";

	/***
	 * 默认60秒
	 */
	private int timeout = 60_000;
	private int corePoolSize = 5;
	private int maxPoolSize = 50;
	private int keepAliveSeconds = 60;
	private int queueCapacity = 100000;
}
