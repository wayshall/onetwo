package org.onetwo.common.web.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssPreventRequestWrapper extends HttpServletRequestWrapper {

	public XssPreventRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	
}
