package org.onetwo.boot.core.web.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
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
	public static final String ASYNC_TASK_BEAN_NAME = "asyncTaskExecutor";

	@Autowired
	private MvcAsyncProperties mvcAsyncProperties;
	
	@Bean(MVC_ASYNC_TASK_BEAN_NAME)
	@ConditionalOnMissingBean(name=AsyncMvcConfiguration.MVC_ASYNC_TASK_BEAN_NAME)
    public AsyncTaskExecutor mvcAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(mvcAsyncProperties.getCorePoolSize());
        executor.setMaxPoolSize(mvcAsyncProperties.getMaxPoolSize());
        executor.setQueueCapacity(mvcAsyncProperties.getQueueCapacity());
        return executor;
    }
	
	@Bean(ASYNC_TASK_BEAN_NAME)
	@ConditionalOnMissingBean(name=AsyncMvcConfiguration.ASYNC_TASK_BEAN_NAME)
    public AsyncTaskExecutor asyncTaskExecutor() {
        return mvcAsyncTaskExecutor();
    }
	
	@Bean
	@ConditionalOnClass(AsyncConfigurer.class)
	@ConditionalOnMissingBean(AsyncConfigurer.class)
	public AsyncTaskConfigurer asyncTaskConfigurer(){
		return new AsyncTaskConfigurer();
	}
	
}
