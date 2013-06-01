package org.onetwo.common.fish.spring.config;

import org.springframework.cache.CacheManager;


public interface JFishContextConfigurerListener {
	
	void onCreateCacheManager(CacheManager cacheManager, boolean isJfishCache);

}
