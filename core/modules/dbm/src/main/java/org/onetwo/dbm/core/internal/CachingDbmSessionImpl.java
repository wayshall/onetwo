package org.onetwo.dbm.core.internal;

import java.lang.reflect.Method;

import org.onetwo.common.spring.cache.MethodKeyGenerator;
import org.onetwo.dbm.core.spi.CachableSession;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.onetwo.dbm.core.spi.DbmTransaction;
import org.springframework.cache.interceptor.KeyGenerator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CachingDbmSessionImpl extends DbmSessionImpl implements CachableSession {
	
	final private KeyGenerator keyGenerator = new MethodKeyGenerator();
	final private Cache<Object, Object> sessionCaches = CacheBuilder.newBuilder().build();
	
	public CachingDbmSessionImpl(DbmSessionFactory sessionFactory, long id, DbmTransaction transaction) {
		super(sessionFactory, id, transaction);
	}
	
	@Override
	public void putCacche(Object key, Object value){
		sessionCaches.put(key, value);
	}
	
	@Override
	public Object getCacche(Object key){
		return sessionCaches.getIfPresent(key);
	}
	
	@Override
	public Object generateCacheKey(Object target, Method method, Object... params){
		return keyGenerator.generate(target, method, params);
	}

	@Override
	public void flush() {
		this.sessionCaches.cleanUp();
	}
}
