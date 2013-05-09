package org.onetwo.common.spring.mcache;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.cache.CacheUtils;
import org.onetwo.common.cache.Cacheable;
import org.onetwo.common.exception.ServiceException;

@SuppressWarnings("rawtypes")
public class MethodCacheInterceptor implements MethodInterceptor{

	@Resource
	protected CacheFacade cacheFacade;
	
	@Resource
	protected CacheModelManager cacheModelManager;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Class targetClass = invocation.getThis().getClass();
		Cacheable cacheable = CacheUtils.findCacheable(targetClass, invocation.getMethod());
		if(cacheable==null)
			return invocation.proceed();
		
		if(!CacheUtils.isCacheable(invocation.getMethod()))
			throw new ServiceException("the method can't return void!");

		CacheModel cacheModel = cacheModelManager.getCacheModel(cacheable, invocation);
		CacheElement element = cacheFacade.getFromCache(cacheModel);
		if(element!=null){
			return element.getValue();
		}
		
		Object result = invocation.proceed();
		cacheFacade.putInCache(cacheModel, result);
		
		return result;
	}

}
