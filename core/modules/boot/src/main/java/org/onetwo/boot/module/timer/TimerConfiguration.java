package org.onetwo.boot.module.timer;

import java.util.concurrent.Executors;

import org.onetwo.boot.module.timer.TimerProperties.SchedulerProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableScheduling
@ConditionalOnClass(EnableScheduling.class)
@EnableConfigurationProperties(TimerProperties.class)
public class TimerConfiguration {
	
	@Configuration
	@ConditionalOnProperty(TimerProperties.THREADPOOL_ENABLED_KEY)
	protected static class ThreadPoolConfigration implements SchedulingConfigurer {
		@Autowired
		private TimerProperties timerProperties;

		@Override
		public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
			SchedulerProps props = timerProperties.getScheduler();
			taskRegistrar.setScheduler(Executors.newScheduledThreadPool(props.getCorePoolSize()));
		}
		
	}

}
