package org.onetwo.common.test.spring;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.mock.web.MockHttpServletRequest;

public class JFishMockHttpServletRequest extends MockHttpServletRequest {
	
	public JFishMockHttpServletRequest request(String method, String path){
		JFishMockHttpServletRequest req = new JFishMockHttpServletRequest(method, path);
		return req;
	}

	public JFishMockHttpServletRequest() {
		super();
	}

	public JFishMockHttpServletRequest(ServletContext servletContext, String method, String requestURI) {
		super(servletContext, method, requestURI);
	}

	public JFishMockHttpServletRequest(ServletContext servletContext) {
		super(servletContext);
	}

	public JFishMockHttpServletRequest(String method, String requestURI) {
		super(method, requestURI);
	}

	public JFishMockHttpServletRequest param(String name, String value){
		this.addParameter(name, value);
		return this;
	}
	
	public JFishMockHttpServletRequest param(String name, String[] values) {
		this.addParameter(name, values);
		return this;
	}

	public JFishMockHttpServletRequest params(Map<?, ?> params) {
		this.addParameters(params);
		return this;
	}
}
