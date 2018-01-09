package org.onetwo.cloud.bugfix;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.pre.FormBodyWrapperFilter;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;

/**
 * instead of OrderedHiddenHttpMethodFilter
 * @author wayshall
 * <br/>
 */
@Deprecated
public class FixFormBodyWrapperFilter extends FormBodyWrapperFilter {
	
	private static final String HIDDEN_METHOD_REQUEST_CLASS_NAME = "org.springframework.web.filter.HiddenHttpMethodFilter$HttpMethodRequestWrapper";
	private static final String SERVLET30_REQUEST_WRAPPER_CLASS_NAME = "org.springframework.cloud.netflix.zuul.filters.pre.Servlet30RequestWrapper";

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		if(isServlet30Request(request)){
			HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper)request;
			request = wrapper.getRequest();
		}
		if(isHiddenMethodRequest(request)){
			//com.netflix.zuul.http.HttpServletRequestWrapper zuul重新包装的request，只有request.getMethod为post时才会转发
			javax.servlet.http.HttpServletRequestWrapper wrapper = (javax.servlet.http.HttpServletRequestWrapper) request;
			ctx.setRequest((HttpServletRequest)wrapper.getRequest());
		}
		return super.shouldFilter();
	}
	
	protected boolean isServlet30Request(HttpServletRequest request){
		return request.getClass().getName().equals(SERVLET30_REQUEST_WRAPPER_CLASS_NAME);
	}
	
	protected boolean isHiddenMethodRequest(HttpServletRequest request){
		return request.getClass().getName().equals(HIDDEN_METHOD_REQUEST_CLASS_NAME);
	}
}
