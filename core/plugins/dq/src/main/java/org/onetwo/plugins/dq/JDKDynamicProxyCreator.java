package org.onetwo.plugins.dq;

import org.onetwo.common.db.CreateQueryable;
import org.springframework.cache.Cache;

public class JDKDynamicProxyCreator implements QueryObjectFactory {
	
	private Cache methodCache;
	
	public Object createQueryObject(CreateQueryable em, Class<?>... proxiedInterfaces){
		return new DynamicQueryHandler(em, methodCache, proxiedInterfaces).getQueryObject();
	}

	public void setMethodCache(Cache methodCache) {
		this.methodCache = methodCache;
	}

}
