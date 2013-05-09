package org.onetwo.common.spring.cache;

import java.io.Serializable;

import org.onetwo.common.spring.mcache.AbstractCacheAdapter;
import org.onetwo.common.spring.mcache.CacheAdapter;
import org.onetwo.common.spring.mcache.CacheElement;

import com.google.common.cache.LoadingCache;

public class LoadingCacheAdapter extends AbstractCacheAdapter implements CacheAdapter {

	private LoadingCache<Serializable, CacheElement> cache;
	
	public LoadingCacheAdapter(String name, LoadingCache<Serializable, CacheElement> cache) {
		super(name);
		this.cache = cache;
	}

	public void setCache(LoadingCache<Serializable, CacheElement> cache) {
		this.cache = cache;
	}
	

	@Override
	public void put(CacheElement val){
		this.cache.put(val.getKey(), val);
	}
	
	@Override
	public CacheElement get(Serializable key){
		return this.cache.getUnchecked(key);
	}
	
	/*@Override
	public void refresh(Serializable key) {
		this.cache.refresh(key);
	}*/

	@Override
	public void invalidate(Serializable key) {
		this.cache.invalidate(key);
	}

	@Override
	public void invalidateAll() {
		this.cache.invalidateAll();
	}
}
