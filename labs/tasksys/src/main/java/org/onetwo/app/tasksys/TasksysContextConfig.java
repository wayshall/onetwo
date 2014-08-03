package org.onetwo.app.tasksys;

import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.spring.config.JFishProfile;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@JFishProfile
@ImportResource("classpath:applicationContext.xml")
public class TasksysContextConfig {
	
	@Bean
	public TasksysConfig tasksysConfig(){
		return TasksysConfig.getInstance();
	}
	
	@Bean
	public CacheManager cacheManager() {
		JFishSimpleCacheManagerImpl cache = new JFishSimpleCacheManagerImpl();
		return cache;
	}
}
