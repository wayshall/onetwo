/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.onetwo.common.test.spring;

import org.springframework.web.servlet.HandlerMapping;

public abstract class SpringTxJUnitTestCase extends SpringBaseJUnitTestCase {

	protected JFishMockHttpServletRequest request(String path){
		return request("GET", path);
	}
	protected JFishMockHttpServletRequest request(String method, String path){
		JFishMockHttpServletRequest request = new JFishMockHttpServletRequest(method, path);
		request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING, true);
		return request;
	}
	
	protected JFishMockHttpServletResponse response(){
		JFishMockHttpServletResponse response = new JFishMockHttpServletResponse();
		return response;
	}
}
