package org.onetwo.common.spring.cache;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.AbstractCacheManager;

public class JFishSimpleCacheManagerImpl extends AbstractCacheManager implements SimpleCacheManager {

	public static final String DEFAULT_NAME = "default";
	
	public static final String FMT_DATA_COMPONENT_CACHE_NAME = "fmt_data_component_cache";
	public static final String EXCEL_TEMPLATE_CACHE_NAME = "excel_template_cache";

	private Collection<Cache> caches;

	public void setCaches(Collection<Cache> caches) {
		this.caches = caches;
	}
	
	private void checkCaches(){
		if(caches==null){
			caches = new HashSet<Cache>();
		}
	}
	
	@Override
	protected Collection<? extends Cache> loadCaches() {
		this.checkCaches();
		caches.add(this.createCache(DEFAULT_NAME));
		caches.add(this.createCache(FMT_DATA_COMPONENT_CACHE_NAME));
		return caches;
	}
	
	protected Cache createCache(String name){
		Cache cache = new ConcurrentMapCache(name);
		return cache;
	}
	
	public Cache addCacheByName(String name){
		Cache cache = createCache(name);
		addCache(cache);
		return cache;
	}
	
	@Override
	public SimpleCacheWrapper getSimpleCacheWrapper(String name){
		Cache cache = getCache(name);
		return new SimpleCacheWrapper(cache);
	}
	
	@Override
	public SimpleCacheWrapper getExcelTemplateCache(){
		return getSimpleCacheWrapper(EXCEL_TEMPLATE_CACHE_NAME);
	}

}
