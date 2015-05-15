package org.onetwo.common.proxy;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.springframework.core.MethodParameter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

abstract public class CacheableDynamicProxyHandler<P extends MethodParameter, M extends AbstractMethodResolver<P>> extends JDKDynamicProxyHandler {

	private final Cache<Method, M> methodCaches;

	public CacheableDynamicProxyHandler(Class<?>... proxiedInterfaces) {
		super(Object.class, proxiedInterfaces);
		this.methodCaches = CacheBuilder.newBuilder().weakKeys().softValues().build();
	}

	@Override
	protected Object doInvoke(Object proxy, final Method method, Object[] args) throws Throwable {
		M methodResolver = methodCaches.get(method, new Callable<M>() {

			@Override
			public M call() throws Exception {
				return createMethodResolver(method);
			}
			
		});
		return invokeMethod(proxy, methodResolver, args);
	}
	
	abstract protected Object invokeMethod(Object proxy, final M method, Object[] args) throws Throwable ;
	
	abstract protected M createMethodResolver(Method method);
	
}
