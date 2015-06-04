package org.onetwo.plugins.jsonrpc.client.proxy;

import java.lang.reflect.Method;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jsonrpc.RpcMethodResolver;
import org.onetwo.common.jsonrpc.protocol.NamedParamsRequest;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.proxy.CacheableDynamicProxyHandler;
import org.onetwo.common.spring.rest.JFishRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;

public class RpcClientHandler extends CacheableDynamicProxyHandler<BaseMethodParameter, RpcMethodResolver> {

//	private final Cache<Method, RestMethodResolver> methodCaches;

	final private String baseUrl;
	private RestTemplate restTemplate;
	
	public RpcClientHandler(String baseUrl, Cache<Method, RpcMethodResolver> methodCaches, Class<?>... proxiedInterfaces) {
		this(baseUrl, null, methodCaches, proxiedInterfaces);
	}
	
	public RpcClientHandler(String baseUrl, RestTemplate restTemplate, Cache<Method, RpcMethodResolver> methodCaches, Class<?>... proxiedInterfaces) {
		super(methodCaches, proxiedInterfaces);
		this.restTemplate = restTemplate==null?new JFishRestTemplate():restTemplate;
		this.baseUrl = baseUrl;
	}

	@Override
	protected Object invokeMethod(Object proxy, RpcMethodResolver method, Object[] args) throws Throwable {
		NamedParamsRequest request = new NamedParamsRequest();
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
