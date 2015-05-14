package org.onetwo.common.proxy;

import java.lang.reflect.Method;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

abstract public class CacheableDynamicProxyHandler<P extends BaseMethodParameter, M extends AbstractMethodResolver<P>> extends JDKDynamicProxyHandler {

	private final Cache<Method, M> methodCaches;

	public CacheableDynamicProxyHandler(Class<?>... proxiedInterfaces) {
		super(Object.class, proxiedInterfaces);
		this.methodCaches = CacheBuilder.newBuilder().weakKeys().softValues().build();
	}
}
