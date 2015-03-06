package org.onetwo.common.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;

public class WebFilterAdapter implements WebFilterInitializers, WebFilter {

	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
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