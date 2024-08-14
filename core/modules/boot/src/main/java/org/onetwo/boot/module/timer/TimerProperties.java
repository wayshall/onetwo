package org.onetwo.boot.module.timer;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(value=TimerProperties.PREFIX_KEY)
@Data
public class TimerProperties {
	public static final String PREFIX_KEY = BootJFishConfig.ZIFISH_CONFIG_PREFIX + ".timer";
	public static final String THREADPOOL_ENABLED_KEY = PREFIX_KEY + ".scheduler.enabled";
	
	SchedulerProps scheduler = new SchedulerProps();
	
	@Data
	public static class SchedulerProps {
		int corePoolSize = 3;
	}
	
}
