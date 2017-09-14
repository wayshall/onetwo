package org.onetwo.common.web.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.onetwo.common.utils.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebHolder {

	private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new NamedThreadLocal<HttpServletRequest>("Request holder");
//	private static final ThreadLocal<HttpServletResponse> RESPONSE_HOLDER = new NamedThreadLocal<HttpServletResponse>("Response holder");

	public static void reset(){
		resetRequest();
//		resetResponse();
	}
	private static void resetRequest(){
		REQUEST_HOLDER.remove();
	}
	/*private static void resetResponse(){
		RESPONSE_HOLDER.remove();
	}*/
	
	public static void initHolder(HttpServletRequest request, HttpServletResponse response){
		if(request==null){
			resetRequest();
		}else{
			if(getSpringContextHolderRequest()==null)
				REQUEST_HOLDER.set(request);
		}
		/*if(response==null){
			resetResponse();
		}else{
			RESPONSE_HOLDER.set(response);
		}*/
	}
	
	public static Optional<HttpServletRequest> getRequest(){
		Optional<HttpServletRequest> req = getSpringContextHolderRequest();
		return req.isPresent()?req:Optional.ofNullable(REQUEST_HOLDER.get());
	}
	
	/*public static HttpServletResponse getResponse(){
		HttpServletResponse response = RESPONSE_HOLDER.get();
		Assert.notNull(response);
		return response;
	}*/
	public static Optional<HttpServletRequest> getSpringContextHolderRequest(){
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs==null?Optional.empty():Optional.of(attrs.getRequest());
	}
	
	public static HttpSession getSession(){
		return getRequest().get().getSession();
	}
	
	public static Object getValue(String name){
		Optional<HttpServletRequest> reqOpts = getRequest();
		if(!reqOpts.isPresent()){
			return null;
		}
		HttpServletRequest request = reqOpts.get();
		Object val = request.getParameter(name);
		val = StringUtils.isObjectBlank(val)?request.getAttribute(name):val;
		val = StringUtils.isObjectBlank(val)?request.getSession().getAttribute(name):val;
		val = StringUtils.isObjectBlank(val)?request.getSession().getServletContext().getAttribute(name):val;
		return StringUtils.emptyIfNull(val);
	}
}
