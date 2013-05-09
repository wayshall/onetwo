package org.onetwo.common.fish.plugin;

import org.onetwo.common.fish.spring.config.JFishContextConfigurerListener;
import org.springframework.cache.CacheManager;

public class JFishContextConfigurerListenerAdapter implements JFishContextConfigurerListener {

	@Override
	public void onCreateCacheManager(CacheManager cacheManager, boolean isJfishCache) {
		
	}

}
