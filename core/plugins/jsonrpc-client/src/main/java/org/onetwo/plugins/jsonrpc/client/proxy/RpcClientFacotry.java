package org.onetwo.plugins.jsonrpc.client.proxy;

import java.lang.reflect.Method;

import org.onetwo.common.jsonrpc.RpcMethodResolver;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class RpcClientFacotry {
	final private String baseUrl;
	final private RestTemplate restTemplate;
	final private Cache<Method, RpcMethodResolver> methodCaches = CacheBuilder.newBuilder().weakKeys().softValues().build();
	
	public RpcClientFacotry(String baseUrl, RestTemplate restTemplate) {
		super();
		this.baseUrl = baseUrl;
		this.restTemplate = restTemplate;
	}

	public <T> T create(Class<T> clazz){
		RpcClientHandler handler = new RpcClientHandler(baseUrl, restTemplate, methodCaches, clazz);
		return handler.getProxyObject();
	}

	
	public static class Builder {
		private String baseUrl;
		private RestTemplate restTemplate;

		public Builder baseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
			return this;
		}
		
		public Builder restTemplate(RestTemplate restTemplate) {
			this.restTemplate = restTemplate;
			return this;
		}

		public RpcClientFacotry build(){
			return new RpcClientFacotry(baseUrl, restTemplate);
		}
	}
}
