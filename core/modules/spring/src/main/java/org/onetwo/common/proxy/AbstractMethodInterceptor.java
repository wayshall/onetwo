package org.onetwo.common.proxy;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.common.cache.Cache;

/**
 * @author wayshall
 * <br/>
 */
public abstract class AbstractMethodInterceptor<M extends AbstractMethodResolver<?>> implements MethodInterceptor {
	final protected Cache<Method, M> methodCache;


	public AbstractMethodInterceptor(Cache<Method, M> methodCache) {
		this.methodCache = methodCache;
	}


	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		Object proxy = invocation.getThis();
		if(Object.class  == method.getDeclaringClass()) {
			String name = method.getName();
			if("equals".equals(name)) {
				return proxy == invocation.getArguments()[0];
			} else if("hashCode".equals(name)) {
				return System.identityHashCode(proxy);
			} else if("toString".equals(name)) {
				return proxy.getClass().getName() + "@" +
	               Integer.toHexString(System.identityHashCode(proxy)) + ", InvocationHandler " + this;
			} else {
				throw new IllegalStateException(String.valueOf(method));
			}
		}
		
//		M invokeMethod = methodCache.get(method);
		M invokeMethod = methodCache.get(method, () -> {
			return createMethod(method);
		});
		return doInvoke(invocation, invokeMethod);
	}
	
	protected M createMethod(Method method) {
		throw new UnsupportedOperationException();
	}
	
	abstract protected Object doInvoke(MethodInvocation invocation, M invokeMethod) throws Throwable ;
	
}
