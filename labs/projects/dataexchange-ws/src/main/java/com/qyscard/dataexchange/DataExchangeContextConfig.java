package com.qyscard.dataexchange;

import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataExchangeContextConfig {
	
	@Bean
	public WebConfig webConfig(){
		return WebConfig.getInstance();
	}
	
	@Bean
	public CacheManager cacheManager() {
		JFishSimpleCacheManagerImpl cache = new JFishSimpleCacheManagerImpl();
		return cache;
	}
}
