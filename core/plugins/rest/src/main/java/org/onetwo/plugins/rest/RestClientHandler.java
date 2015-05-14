package org.onetwo.plugins.rest;

import java.lang.reflect.Method;

import org.onetwo.common.proxy.JDKDynamicProxyHandler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class RestClientHandler extends JDKDynamicProxyHandler {

	private final Cache<Method, RestMethodResolver> methodCaches;
	
	public RestClientHandler(Class<?>... proxiedInterfaces) {
		super(Object.class, proxiedInterfaces);
		this.methodCaches = CacheBuilder.newBuilder().weakKeys().softValues().build();
	}

	@Override
	protected Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
		logger.info("test");
		return null;
	}


}
