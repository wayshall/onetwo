package org.onetwo.boot.core.web.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnProperty(value = AsyncTaskProperties.ENABLE_KEY, matchIfMissing = true)
@EnableConfigurationProperties(AsyncTaskProperties.class)
@ConditionalOnClass(EnableAsync.class)
@EnableAsync
public class AsyncTaskConfiguration {
	public static final String ASYNC_TASK_BEAN_NAME = "asyncTaskExecutor";

	@Autowired
	private AsyncTaskProperties asyncTaskProperties;
	
	@Bean(ASYNC_TASK_BEAN_NAME)
//	@ConditionalOnMissingBean(AsyncTaskExecutor.class)
    public AsyncTaskExecutor asyncTaskExecutor(DelegateTaskDecorator delegateTaskDecorator) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncTaskProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncTaskProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncTaskProperties.getQueueCapacity());
        executor.setTaskDecorator(delegateTaskDecorator);
        return executor;
    }
	
	@Bean
	public DelegateTaskDecorator delegateTaskDecorator() {
		return new DelegateTaskDecorator();
	}
	
	@Bean
	@ConditionalOnMissingBean(AsyncTaskConfigurer.class)
	@Order(Ordered.LOWEST_PRECEDENCE)
	public AsyncTaskConfigurer asyncTaskConfigurer(DelegateTaskDecorator delegateTaskDecorator){
		AsyncTaskConfigurer configurer = new AsyncTaskConfigurer(asyncTaskExecutor(delegateTaskDecorator));
		return configurer;
	}
	
	@Bean
	public AsyncTaskDelegateService asyncTaskDelegateService(){
		return new AsyncTaskDelegateService();
	}
	
}
