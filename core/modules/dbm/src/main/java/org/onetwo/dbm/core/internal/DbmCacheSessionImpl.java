package org.onetwo.dbm.core.internal;

import java.lang.reflect.Method;

import org.onetwo.common.spring.cache.MethodKeyGenerator;
import org.onetwo.dbm.core.spi.CachableSession;
import org.onetwo.dbm.core.spi.DbmInterceptorChain;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.onetwo.dbm.core.spi.DbmTransaction;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class DbmCacheSessionImpl extends DbmSessionImpl implements CachableSession {
	static final private KeyGenerator DEFAULT_KEY_GENERATOR = new MethodKeyGenerator();
	
	final private KeyGenerator keyGenerator = DEFAULT_KEY_GENERATOR;
	final private Cache<Object, ValueWrapper> sessionCaches = CacheBuilder.newBuilder().build();
	
	public DbmCacheSessionImpl(DbmSessionFactory sessionFactory, long id, DbmTransaction transaction) {
		super(sessionFactory, id, transaction);
	}
	
//	@Override
	public void putCacche(Object key, ValueWrapper value){
		sessionCaches.put(key, value);
	}
	
	@Override
	public Object getCaccheOrInvoke(DbmInterceptorChain chain){
		Object key = generateCacheKey(null, chain.getTargetMethod(), chain.getTargetArgs());
		ValueWrapper valueWrapper = getCacche(key);
		if(valueWrapper!=null){
			if(logger.isDebugEnabled()){
				logger.info("fetch value from cache for method:{}, key: {}", chain.getTargetMethod(), key);
			}
			return valueWrapper.get();
		}else{
			Object value = chain.invoke();
			valueWrapper = new SimpleValueWrapper(value);
			putCacche(key, valueWrapper);
			return value;
		}
	}
	
	public ValueWrapper getCacche(Object key){
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
