package org.onetwo.plugins.jsonrpc.client.test;

import java.lang.reflect.Method;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jsonrpc.RpcMethodResolver;
import org.onetwo.common.jsonrpc.protocol.NamedParamsRequest;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.proxy.CacheableDynamicProxyHandler;

public class ToParamsJsonStringHandler extends CacheableDynamicProxyHandler<BaseMethodParameter, RpcMethodResolver> {

	public ToParamsJsonStringHandler(Class<?>... proxiedInterfaces) {
		super(null, proxiedInterfaces);
	}

	@Override
	protected Object invokeMethod(Object proxy, RpcMethodResolver method, Object[] args) throws Throwable {
		NamedParamsRequest request = new NamedParamsRequest();
		String methodName = method.getDeclaringClass().getName()+"."+method.getMethod().getName();
		request.setMethod(methodName);
//		request.setId(System.nanoTime());
		if(method.isNamedParam()){
			request.setParams(method.toMapByArgs(args));
		}else{
			throw new BaseException("error");
		}
		return request;
	}

	@Override
	protected RpcMethodResolver createMethodResolver(Method method) {
		return new RpcMethodResolver(method);
	}


}
