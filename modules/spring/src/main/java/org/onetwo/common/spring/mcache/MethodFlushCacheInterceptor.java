package org.onetwo.common.spring.mcache;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.cache.CacheUtils;
import org.onetwo.common.cache.FlushCache;

@SuppressWarnings("rawtypes")
public class MethodFlushCacheInterceptor implements MethodInterceptor{

	@Resource
	protected CacheFacade cacheFacade;
	
	@Resource
	protected CacheModelManager cacheModelManager;
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object returnValue = invocation.proceed();
		
		Class targetClass = invocation.getThis().getClass();
		FlushCache flushable = CacheUtils.findFlushCache(targetClass, invocation.getMethod());
		if(flushable==null)
			return returnValue;
		
		FlushCacheModel flushModel = cacheModelManager.getFlushCacheModel(flushable, invocation, returnValue);
		cacheFacade.flushCache(flushModel);
		System.out.println("flushcache : " + flushable);

		return returnValue;
	}

}
