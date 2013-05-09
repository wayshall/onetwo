package org.onetwo.common.spring.mcache;


public interface CacheFacade {

	public CacheElement getFromCache(CacheModel cacheModel);
	
//	public Element getFromCache(Cacheable cacheable, MethodInvocation invocation);

	public CacheElement putInCache(CacheModel cacheModel, Object value);
//	public void putInCache(CacheElement cacheElement);
	
	public void flushCache(FlushCacheModel flushModel);

}