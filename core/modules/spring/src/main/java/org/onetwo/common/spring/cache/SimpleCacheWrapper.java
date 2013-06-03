package org.onetwo.common.spring.cache;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.mcache.CacheElement;
import org.slf4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

public class SimpleCacheWrapper {
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	public static final int DEFAULT_EXPIRE_TIME = 60 * 30;//a half hour
	
	private Cache cache;

	public SimpleCacheWrapper(Cache cache) {
		super();
		this.cache = cache;
	}
	
	public boolean canCache(){
		return this.cache != null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key){
		if(!canCache())
			return null;
		ValueWrapper value = cache.get(key);
		if(value==null)
			return null;
		CacheElement ce = (CacheElement)value.get();
		
		if(!ce.isIndate()){
			logger.info("clear cache by key: " + ce);
			return null;
		}
		
		if(logger.isInfoEnabled()){
			logger.info("get from cache by key: " + ce);
		}
		
		return (T)ce.getValue();
	}
	
	public void put(String key, Object value){
		put(key, value, DEFAULT_EXPIRE_TIME);
	}
	
	public void put(String key, Object value, int expire){
		if(!canCache() || value==null)
			return ;
		CacheElement ce = CacheElement.create(key, value, expire);
		cache.put(key, ce);

		if(logger.isInfoEnabled()){
			logger.info("put into cache: " + ce);
		}
	}

}
