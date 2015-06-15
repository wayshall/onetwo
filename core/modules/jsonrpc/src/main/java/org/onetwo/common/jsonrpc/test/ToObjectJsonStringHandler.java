package org.onetwo.common.jsonrpc.test;

import java.lang.reflect.Method;

import org.onetwo.common.jsonrpc.RpcMethodResolver;
import org.onetwo.common.jsonrpc.protocol.JsonRpcRequest;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.proxy.CacheableDynamicProxyHandler;

public class ToObjectJsonStringHandler extends CacheableDynamicProxyHandler<BaseMethodParameter, RpcMethodResolver> {

	private boolean namedParam;
	
	public ToObjectJsonStringHandler(boolean namedParam, Class<?>... proxiedInterfaces) {
		super(null, proxiedInterfaces);
		this.namedParam = namedParam;
	}

	@Override
	protected Object invokeMethod(Object proxy, RpcMethodResolver method, Object[] args) throws Throwable {
		JsonRpcRequest request = new JsonRpcRequest();
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
