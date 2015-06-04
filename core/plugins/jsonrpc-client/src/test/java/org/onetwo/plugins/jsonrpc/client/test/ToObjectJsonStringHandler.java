package org.onetwo.plugins.jsonrpc.client.test;

import java.lang.reflect.Method;

import org.onetwo.common.jsonrpc.protocol.JsonRpcParamsRequest;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.proxy.CacheableDynamicProxyHandler;
import org.onetwo.plugins.jsonrpc.client.proxy.RpcMethodResolver;

public class ToObjectJsonStringHandler extends CacheableDynamicProxyHandler<BaseMethodParameter, RpcMethodResolver> {

	private boolean namedParam;
	
	public ToObjectJsonStringHandler(boolean namedParam, Class<?>... proxiedInterfaces) {
		super(null, proxiedInterfaces);
		this.namedParam = namedParam;
	}

	@Override
	protected Object invokeMethod(Object proxy, RpcMethodResolver method, Object[] args) throws Throwable {
		JsonRpcParamsRequest request = new JsonRpcParamsRequest();
		String methodName = method.getDeclaringClass().getName()+"."+method.getMethod().getName();
		request.setMethod(methodName);
		if(namedParam){
			request.setParams(method.toMapByArgs(args));
		}else{
			request.setParams(method.toListByArgs(args));
		}
		return request;
	}

	@Override
	protected RpcMethodResolver createMethodResolver(Method method) {
		return new RpcMethodResolver(method);
	}


}
