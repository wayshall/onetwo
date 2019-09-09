package org.onetwo.boot.core.web.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnProperty(MvcAsyncProperties.ENABLE_KEY)
@EnableConfigurationProperties(MvcAsyncProperties.class)
public class AsyncMvcConfiguration {
	public static final String MVC_ASYNC_TASK_BEAN_NAME = "mvcAsyncTaskExecutor";

	@Autowired
	private MvcAsyncProperties mvcAsyncProperties;
	
	@Bean(MVC_ASYNC_TASK_BEAN_NAME)
	@ConditionalOnMissingBean(name=AsyncMvcConfiguration.MVC_ASYNC_TASK_BEAN_NAME)
	@Primary
    public AsyncTaskExecutor mvcAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(mvcAsyncProperties.getCorePoolSize());
        executor.setMaxPoolSize(mvcAsyncProperties.getMaxPoolSize());
        executor.setQueueCapacity(mvcAsyncProperties.getQueueCapacity());
        return executor;
    }
	
	
}
