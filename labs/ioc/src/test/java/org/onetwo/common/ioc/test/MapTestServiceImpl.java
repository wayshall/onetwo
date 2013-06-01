package org.onetwo.common.ioc.test;

import java.util.Map;

import org.onetwo.common.ioc.annotation.Inject;


public class MapTestServiceImpl  {
	
	@Inject
	private Map<String, UserTestService> userMap;

	@Inject
	private Map<String, String> map;
	
	@Inject
	private Class<UserTestService> mapClass;
	
	public MapTestServiceImpl(){
		System.out.println("ContainerAwareServiceImpl");
	}

	public Map getMap() {
		return map;
	}

	public Map<String, UserTestService> getUserMap() {
		return userMap;
	}

	public Class<UserTestService> getMapClass() {
		return mapClass;
	}
	

}
