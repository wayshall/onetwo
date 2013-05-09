package org.onetwo.common.spring.mcache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.cache.Cacheable;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

@SuppressWarnings({"rawtypes", "unchecked"})
abstract public class AbstractCacheFacadeImpl implements InitializingBean, CacheFacade {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private final Map<String, CacheAdapter> cacheMap = new ConcurrentHashMap<String, CacheAdapter>();

//	private Set<String> cacheNames = new LinkedHashSet<String>();
	
	@Resource
	protected CacheModelManager cacheModelManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Collection<CacheAdapter> caches = loadCaches();
		Assert.notEmpty(caches, "caches can not be empty!");
		for(CacheAdapter cache : caches){
			cacheMap.put(cache.getName(), cache);
		}
	}
	
	abstract protected Collection<CacheAdapter> loadCaches();
	
	public CacheElement getFromCache(CacheModel cacheModel){
		CacheAdapter cache = cacheMap.get(cacheModel.getGroup());
		Assert.notNull(cache, "can not find the cache group : " + cacheModel.getGroup());
		CacheElement element = cache.get(cacheModel.getKey());
		if(element==null)
			return null;
		if(!element.isIndate()){
			cache.invalidate(element.getKey());
			return null;
		}
		return element;
	}
	
	public Object getFromCache(Cacheable cacheable, MethodInvocation invocation){
		CacheModel cacheModel = cacheModelManager.getCacheModel(cacheable, invocation);
		return getFromCache(cacheModel);
	}
	
	public CacheElement putInCache(CacheModel cacheModel, Object value){
		CacheAdapter cache = cacheMap.get(cacheModel.getGroup());
		CacheElement ele = CacheElement.create(cacheModel, value);
		cache.put(ele);
		return ele;
	}

	public void flushCache(FlushCacheModel flushModel){
		if(flushModel==null)
			return ;
		for(String group : flushModel.getGroups()){
			if(StringUtils.isBlank(group))
				continue;
			CacheAdapter adpter = cacheMap.get(group);
			if(flushModel.isValidKey()){
				adpter.invalidate(flushModel.getKey());
			}else{
				adpter.invalidateAll();
			}
		}
	}
	
	
}
