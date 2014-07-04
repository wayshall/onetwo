package org.onetwo.common.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WebFilter {

	public void onFilter(HttpServletRequest request, HttpServletResponse response);
	
//	public void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws IOException, ServletException;
	
	public void onFinally(HttpServletRequest request, HttpServletResponse response);

//	public int getOrder();

}