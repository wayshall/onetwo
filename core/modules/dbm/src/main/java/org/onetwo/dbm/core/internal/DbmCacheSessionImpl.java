package org.onetwo.dbm.core.internal;

import java.lang.reflect.Method;

import org.onetwo.common.spring.cache.MethodKeyGenerator;
import org.onetwo.dbm.core.spi.CachableSession;
import org.onetwo.dbm.core.spi.DbmInterceptorChain;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.onetwo.dbm.core.spi.DbmTransaction;
import org.springframework.cache.interceptor.KeyGenerator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class DbmCacheSessionImpl extends DbmSessionImpl implements CachableSession {
	
	final private KeyGenerator keyGenerator = new MethodKeyGenerator();
	final private Cache<Object, Object> sessionCaches = CacheBuilder.newBuilder().build();
	
	public DbmCacheSessionImpl(DbmSessionFactory sessionFactory, long id, DbmTransaction transaction) {
		super(sessionFactory, id, transaction);
	}
	
	@Override
	public void putCacche(Object key, Object value){
		sessionCaches.put(key, value);
	}
	
	@Override
	public Object getCaccheOrInvoke(DbmInterceptorChain chain){
		Object key = generateCacheKey(null, chain.getTargetMethod(), chain.getTargetArgs());
		Object value = getCacche(key);
		if(value!=null){
			return value;
		}
		return chain.invoke();
	}
	
	public Object getCacche(Object key){
		return sessionCaches.getIfPresent(key);
	}
	
	public Object generateCacheKey(Object target, Method method, Object... params){
		return keyGenerator.generate(target, method, params);
	}

	@Override
	public void flush() {
		this.sessionCaches.cleanUp();
	}
}
