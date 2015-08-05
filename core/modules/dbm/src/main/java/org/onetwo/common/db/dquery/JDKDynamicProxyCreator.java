package org.onetwo.common.db.dquery;

import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.jdbc.JdbcDao;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

public class JDKDynamicProxyCreator implements QueryObjectFactory {
	
	private Cache methodCache;
	private ApplicationContext applicationContext;
	
	public Object createQueryObject(QueryProvideManager em, Class<?>... proxiedInterfaces){
		JdbcDao jdao = SpringUtils.getBean(applicationContext, JdbcDao.class);
		return new DynamicQueryHandler(em, methodCache, jdao, proxiedInterfaces).getQueryObject();
	}

	public void setMethodCache(Cache methodCache) {
		this.methodCache = methodCache;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	
}
