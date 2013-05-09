package org.onetwo.common.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FilterInitializer {

	public void onInit(FilterConfig config);

	public void onFilter(HttpServletRequest request, HttpServletResponse response);
	
//	public void onException(HttpServletRequest request, HttpServletResponse response, Exception ex);
	
	public void onFinally(HttpServletRequest request, HttpServletResponse response);

//	public int getOrder();

}