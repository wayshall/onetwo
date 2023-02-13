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
	public static final String PREFIX = "jfish.async";
	public static final String ENABLE_KEY = PREFIX+".enabled";

//	private int timeout = 60000;
	private int corePoolSize = 5;
	/***
	 * maxPoolSize依赖于queueCapacity，因为ThreadPoolTaskExecutor只会在其队列中的项目数超过queueCapacity时创建一个新线程。
	 */
	private int maxPoolSize = 50;
//	private int keepAliveSeconds = 60;
	private int queueCapacity = 10000;
}
