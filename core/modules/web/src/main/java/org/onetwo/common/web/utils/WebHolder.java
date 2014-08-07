package org.onetwo.common.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.onetwo.common.utils.StringUtils;
import org.springframework.core.NamedThreadLocal;

public class WebHolder {

	private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new NamedThreadLocal<HttpServletRequest>("Request holder");

	public static void reset(){
		resetRequest();
	}
	private static void resetRequest(){
		REQUEST_HOLDER.remove();
	}
	
	public static void setRequest(HttpServletRequest request){
		if(request==null){
			resetRequest();
		}else{
			REQUEST_HOLDER.set(request);
		}
	}
	
	public static HttpServletRequest getRequest(){
		return REQUEST_HOLDER.get();
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
