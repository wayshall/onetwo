package org.onetwo.plugins.rest;

import java.lang.reflect.Method;

import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class RestClientFacotry {
	final private String baseUrl;
	final private RestTemplate restTemplate;
	final private Cache<Method, RestMethodResolver> methodCaches = CacheBuilder.newBuilder().weakKeys().softValues().build();
	
	public RestClientFacotry(String baseUrl, RestTemplate restTemplate) {
		super();
		this.baseUrl = baseUrl;
		this.restTemplate = restTemplate;
	}

	public <T> T create(Class<T> clazz){
		RestClientHandler handler = new RestClientHandler(baseUrl, restTemplate, methodCaches, clazz);
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

		public RestClientFacotry build(){
			return new RestClientFacotry(baseUrl, restTemplate);
		}
	}
}
