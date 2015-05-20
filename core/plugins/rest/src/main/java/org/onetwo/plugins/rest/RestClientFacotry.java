package org.onetwo.plugins.rest;

import org.onetwo.common.spring.rest.JFishRestTemplate;

public class RestClientFacotry {
	
	public static <T> T create(Class<T> clazz){
		RestClientHandler handler = new RestClientHandler(clazz);
		return handler.getProxyObject();
	}
	
	public static <T> T create(JFishRestTemplate restTemplate, Class<T> clazz){
		RestClientHandler handler = new RestClientHandler(restTemplate, clazz);
		return handler.getProxyObject();
	}

}
