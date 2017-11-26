package org.onetwo.boot.core.web.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnProperty(AsyncTaskProperties.ENABLE_KEY)
@EnableConfigurationProperties(AsyncTaskProperties.class)
public class AsyncTaskConfiguration {
	public static final String ASYNC_TASK_BEAN_NAME = "asyncTaskExecutor";

	@Autowired
	private AsyncTaskProperties asyncTaskProperties;
	
	@Bean(ASYNC_TASK_BEAN_NAME)
	@ConditionalOnMissingBean(name=AsyncTaskConfiguration.ASYNC_TASK_BEAN_NAME)
    public AsyncTaskExecutor mvcAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncTaskProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncTaskProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncTaskProperties.getQueueCapacity());
        return executor;
    }
	
	
}
