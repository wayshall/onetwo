package org.onetwo.plugins.jsonrpc.client.test;

import java.lang.reflect.Method;

import org.onetwo.common.jsonrpc.protocol.ListParamsRequest;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.proxy.CacheableDynamicProxyHandler;
import org.onetwo.plugins.jsonrpc.client.proxy.RpcMethodResolver;

public class ToListJsonStringHandler extends CacheableDynamicProxyHandler<BaseMethodParameter, RpcMethodResolver> {

	public ToListJsonStringHandler(Class<?>... proxiedInterfaces) {
		super(null, proxiedInterfaces);
	}

	@Override
	protected Object invokeMethod(Object proxy, RpcMethodResolver method, Object[] args) throws Throwable {
		ListParamsRequest request = new ListParamsRequest();
		String methodName = method.getDeclaringClass().getName()+"."+method.getMethod().getName();
		request.setMethod(methodName);
		request.setParams(method.toListByArgs(args));
		return request;
	}

	@Override
	protected RpcMethodResolver createMethodResolver(Method method) {
		return new RpcMethodResolver(method);
	}


}
