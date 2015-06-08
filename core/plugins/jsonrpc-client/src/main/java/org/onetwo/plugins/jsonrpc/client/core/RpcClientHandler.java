package org.onetwo.plugins.jsonrpc.client.core;

import java.lang.reflect.Method;

import org.onetwo.common.jsonrpc.ClientResponseParser;
import org.onetwo.common.jsonrpc.RpcMethodResolver;
import org.onetwo.common.jsonrpc.exception.JsonRpcError;
import org.onetwo.common.jsonrpc.exception.JsonRpcException;
import org.onetwo.common.jsonrpc.protocol.JsonRpcRequest;
import org.onetwo.common.jsonrpc.protocol.JsonRpcResponse;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.proxy.CacheableDynamicProxyHandler;
import org.onetwo.common.spring.rest.JFishRestTemplate;
import org.onetwo.common.utils.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;

public class RpcClientHandler extends CacheableDynamicProxyHandler<BaseMethodParameter, RpcMethodResolver> {

//	private final Cache<Method, RestMethodResolver> methodCaches;

	private String serverEndpoint;
	private RestTemplate restTemplate;
	private RequestIdGenerator requestIdGenerator;
	
	public RpcClientHandler(){
		super(null);
	}
	
	public RpcClientHandler(String baseUrl, RestTemplate restTemplate, Cache<Method, RpcMethodResolver> methodCaches, 
							RequestIdGenerator requestIdGenerator, Class<?>... proxiedInterfaces) {
		super(methodCaches, proxiedInterfaces);
		Assert.hasText(baseUrl, "no jsonrpc server endpoint must be config!");
		this.restTemplate = restTemplate==null?new JFishRestTemplate():restTemplate;
		this.serverEndpoint = baseUrl;
		this.requestIdGenerator = requestIdGenerator;
	}

	@Override
	protected Object invokeMethod(Object proxy, RpcMethodResolver method, Object[] args) throws Throwable {
		JsonRpcRequest request = new JsonRpcRequest();
		request.setId(requestIdGenerator.generateId());
		String methodName = method.getDeclaringClass().getName()+"."+method.getMethod().getName();
		request.setMethod(methodName);
		if(method.isNamedParam()){
			request.setParams(method.toMapByArgs(args));
		}else{
			request.setParams(method.toListByArgs(args));
		}
		String jsonstr = this.restTemplate.postForObject(serverEndpoint, request, String.class);
		if(StringUtils.isBlank(jsonstr)){
			throw new JsonRpcException(JsonRpcError.INVALID_RESPONSE, "empty response from server.");
		}
		ClientResponseParser parser = new ClientResponseParser(jsonstr);
		JsonRpcResponse baseResponse = parser.parseBase().getResponse();
		if(baseResponse.getError()!=null){
			throw new JsonRpcException(JsonRpcError.valueOf(baseResponse.getError()));
		}
		Object result = parser.parseResult(method).getResponse().getResult();
//		this.restTemplate.postForEntity(baseUrl, request, method.getResponseType());
		return result;
	}

	@Override
	protected RpcMethodResolver createMethodResolver(Method method) {
		return new RpcMethodResolver(method);
	}


	public String getServerEndpoint() {
		return serverEndpoint;
	}

	public void setServerEndpoint(String serverEndpoint) {
		this.serverEndpoint = serverEndpoint;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public RequestIdGenerator getRequestIdGenerator() {
		return requestIdGenerator;
	}

	public void setRequestIdGenerator(RequestIdGenerator requestIdGenerator) {
		this.requestIdGenerator = requestIdGenerator;
	}


}
