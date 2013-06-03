package org.onetwo.common.spring.mcache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;

public class MapCacheAdapter extends AbstractCacheAdapter implements CacheAdapter {

	private Map<Serializable, CacheElement> cache;
	
	private final Logger logger = MyLoggerFactory.getLogger(MapCacheAdapter.class);
	
	public MapCacheAdapter(String name){
		super(name);
		this.cache = new ConcurrentHashMap<Serializable, CacheElement>();
	}
	
	public MapCacheAdapter(String name, Map<Serializable, CacheElement> cache) {
		super(name);
		this.cache = cache;
	}

	public void setCache(Map<Serializable, CacheElement> cache) {
		this.cache = cache;
	}
	
	@Override
	public void put(CacheElement element){
		this.cache.put(element.getKey(), element);
		logger.info("put in cache for key: " + element.getKey());
	}
	
	@Override
	public CacheElement get(Serializable key){
		return this.cache.get(key);
	}
	
	/*@Override
	public void refresh(Serializable key) {
		this.cache.remove(key);
	}*/

	@Override
	public void invalidate(Serializable key) {
		if(this.cache.containsKey(key)){
			this.cache.remove(key);
			logger.info("removed cache for key: "+key);
		}else{
			logger.error("try to remove cache for key: "+key +", but not found!");
		}
	}

	@Override
	public void invalidateAll() {
		this.cache.clear();
		logger.info("removed all cache.");
	}
}
