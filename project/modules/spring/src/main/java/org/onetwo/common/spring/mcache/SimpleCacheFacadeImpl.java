package org.onetwo.common.spring.mcache;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.WeakHashMap;

import org.onetwo.common.cache.CacheUtils;

public class SimpleCacheFacadeImpl extends AbstractCacheFacadeImpl {

	private Collection<String> groups;
	
	@Override
	protected Collection<CacheAdapter> loadCaches() {
		if(groups==null){
			groups = new HashSet<String>();
			groups.add(CacheUtils.DEFAULT_CACHE_GROUP);
		}
		Collection<CacheAdapter> caches = new HashSet<CacheAdapter>();
		
//		MapCacheAdapter testCache = new MapCacheAdapter(CacheUtils.DEFAULT_CACHE_GROUP);
		
		for(String group : groups){
			MapCacheAdapter testCache = new MapCacheAdapter(group, new WeakHashMap<Serializable, CacheElement>());
			caches.add(testCache);
		}
		
		return caches;
	}

	public Collection<String> getGroups() {
		return groups;
	}

	public void setGroups(Collection<String> groups) {
		this.groups = groups;
	}
	
}
