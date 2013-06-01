package org.onetwo.common.ioc.proxy;

import java.util.Arrays;
import java.util.List;

import org.onetwo.common.ioc.proxy.impl.DynamicProxyHandler;

@SuppressWarnings("unchecked")
public abstract class DynamicProxyFactory {
	
	public static BFProxyHandler proxy(Object obj, Class[] proxiedInterfaces, BFInterceptor...interceptors){
		BFProxyHandler handler = new DynamicProxyHandler(obj, proxiedInterfaces, Arrays.asList(interceptors));
		return handler;
	}
	
	public static BFProxyHandler proxy(Object obj, Class[] proxiedInterfaces, List<BFInterceptor> interceptors){
		BFProxyHandler handler = new DynamicProxyHandler(obj, proxiedInterfaces, interceptors);
		return handler;
	}

}
