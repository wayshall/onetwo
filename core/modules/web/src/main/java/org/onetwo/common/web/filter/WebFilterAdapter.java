package org.onetwo.common.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebFilterAdapter implements WebFilterInitializers, WebFilter {

	@Override
	public void onInit(FilterConfig config) {
		
	}

	/*@Override
	public void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws IOException, ServletException {
		if(ex instanceof ServletException)
			throw (ServletException)ex;
		else if(ex instanceof IOException)
			throw (IOException)ex;
		throw new ServletException("error: " + ex.getMessage(), ex);
	}*/

	@Override
	public void onFilter(HttpServletRequest request, HttpServletResponse response) {
		
	}

	@Override
	public void onFinally(HttpServletRequest request, HttpServletResponse response) {
		
	}


}