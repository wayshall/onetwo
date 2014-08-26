package org.onetwo.app.taskserver;

import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.spring.config.JFishProfile;
import org.onetwo.plugins.task.service.impl.TaskQueueServiceImpl;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@JFishProfile
@ImportResource("classpath:applicationContext.xml")
@ComponentScan("org.onetwo.app.taskserver")
public class TasksysContextConfig {
	
	@Bean
	public TaskServerConfig tasksysConfig(){
		return TaskServerConfig.getInstance();
	}
	
	@Bean
	public CacheManager cacheManager() {
		JFishSimpleCacheManagerImpl cache = new JFishSimpleCacheManagerImpl();
		return cache;
	}
	
	@Bean
	public TaskQueueServiceImpl taskQueueService(){
		return new TaskQueueServiceImpl();
	}
}
