package org.onetwo.common.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.onetwo.common.utils.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebHolder {

	private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new NamedThreadLocal<HttpServletRequest>("Request holder");
	private static final ThreadLocal<HttpServletResponse> RESPONSE_HOLDER = new NamedThreadLocal<HttpServletResponse>("Response holder");

	public static void reset(){
		resetRequest();
		resetResponse();
	}
	private static void resetRequest(){
		REQUEST_HOLDER.remove();
	}
	private static void resetResponse(){
		RESPONSE_HOLDER.remove();
	}
	
	public static void initHolder(HttpServletRequest request, HttpServletResponse response){
		if(request==null){
			resetRequest();
		}else{
			REQUEST_HOLDER.set(request);
		}
		if(response==null){
			resetResponse();
		}else{
			RESPONSE_HOLDER.set(response);
		}
	}
	
	public static HttpServletRequest getRequest(){
		HttpServletRequest req = getSpringContextHolderRequest();
		return req==null?REQUEST_HOLDER.get():req;
	}
	
	public static HttpServletResponse getResponse(){
		HttpServletResponse response = RESPONSE_HOLDER.get();
		Assert.notNull(response);
		return response;
	}
	public static HttpServletRequest getSpringContextHolderRequest(){
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs==null?null:attrs.getRequest();
	}
	
	public static HttpSession getSession(){
		return getRequest().getSession();
	}
	
	public static Object getValue(String name){
		Object val = getRequest().getParameter(name);
		val = StringUtils.isObjectBlank(val)?getRequest().getAttribute(name):val;
		val = StringUtils.isObjectBlank(val)?getRequest().getSession().getAttribute(name):val;
		val = StringUtils.isObjectBlank(val)?getRequest().getSession().getServletContext().getAttribute(name):val;
		return StringUtils.emptyIfNull(val);
	}
}
