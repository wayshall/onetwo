package org.onetwo.plugins.dq;

import org.onetwo.common.db.BaseEntityManager;
import org.springframework.cache.Cache;

public class JDKDynamicProxyCreator implements QueryObjectFactory {
	
	private Cache methodCache;
	
	public Object createQueryObject(BaseEntityManager em, Class<?>... proxiedInterfaces){
		return new DynamicQueryHandler(em, methodCache, proxiedInterfaces).getQueryObject();
	}

	public void setMethodCache(Cache methodCache) {
		this.methodCache = methodCache;
	}

}
