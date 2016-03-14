package org.onetwo.plugins.jsonrpc.client.core;

import java.lang.reflect.Method;

import org.onetwo.common.jsonrpc.RpcMethodResolver;
import org.onetwo.plugins.jsonrpc.client.core.impl.SimpleServerEndpointProvider;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class RpcClientFacotry {
	private ServerEndpointProvider serverEndpointProvider;
	private RestTemplate restTemplate;
	private Cache<Method, RpcMethodResolver> methodCaches = CacheBuilder.newBuilder().weakKeys().softValues().build();
	private RequestIdGenerator requestIdGenerator = new SeqIdGenerator();
	
	private RpcClientFacotry() {
	}

	public <T> T create(Class<T> clazz){
		Assert.notNull(serverEndpointProvider);
		RpcClientHandler handler = new RpcClientHandler(serverEndpointProvider, restTemplate, methodCaches, requestIdGenerator, clazz);
		return handler.getProxyObject();
	}

	public <T> T create(String serverEndpoint, Class<T> clazz){
		RpcClientHandler handler = new RpcClientHandler(new SimpleServerEndpointProvider(serverEndpoint), restTemplate, methodCaches, requestIdGenerator, clazz);
		return handler.getProxyObject();
	}

	public <T> T create(ServerEndpointProvider serverEndpointProvider, Class<T> clazz){
		RpcClientHandler handler = new RpcClientHandler(serverEndpointProvider, restTemplate, methodCaches, requestIdGenerator, clazz);
		return handler.getProxyObject();
	}

	public static class Builder {
		private RpcClientFacotry rpcClientFacotry = new RpcClientFacotry();
		
		public Builder(){
			
		}

		public Builder serverEndpoint(String serverEndpoint) {
			rpcClientFacotry.serverEndpointProvider = new SimpleServerEndpointProvider(serverEndpoint);
			return this;
		}

		public Builder serverEndpointProvider(ServerEndpointProvider serverEndpointProvider) {
			rpcClientFacotry.serverEndpointProvider = serverEndpointProvider;
			return this;
		}
		
		public Builder restTemplate(RestTemplate restTemplate) {
			rpcClientFacotry.restTemplate = restTemplate;
			return this;
		}
		
		public Builder methodCaches(Cache<Method, RpcMethodResolver> methodCaches) {
			rpcClientFacotry.methodCaches = methodCaches;
			return this;
		}

		public Builder requestIdGenerator(RequestIdGenerator requestIdGenerator) {
			rpcClientFacotry.requestIdGenerator = requestIdGenerator;
			return this;
		}

		public RpcClientFacotry build(){
			return rpcClientFacotry;
		}
	}
}
