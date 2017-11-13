package org.onetwo.common.web.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.onetwo.common.utils.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebHolder {

	/***
	 * 一致采用spring封装的，以免太多线程变量
	 */
//	private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new NamedThreadLocal<HttpServletRequest>("Request holder");
//	private static final ThreadLocal<HttpServletResponse> RESPONSE_HOLDER = new NamedThreadLocal<HttpServletResponse>("Response holder");
	private static final String INIT_TAG = WebHolder.class.getName()+".initialized";
	private static final String RESPONSE_TAG = WebHolder.class.getName()+".response";
	
	public static void reset(){
		resetRequest();
//		resetResponse();
	}
	private static void resetRequest(){
//		REQUEST_HOLDER.remove();
	}
	/*private static void resetResponse(){
		RESPONSE_HOLDER.remove();
	}*/
	
	public static void initHolder(HttpServletRequest request, HttpServletResponse response){
		/*if(request==null){
			resetRequest();
		}else{
			Optional<HttpServletRequest> opt = getSpringContextHolderRequest();
			if(!opt.isPresent()){
				REQUEST_HOLDER.set(request);
			}
		}*/
		/*if(response==null){
			resetResponse();
		}else{
			RESPONSE_HOLDER.set(response);
		}*/
		Boolean inited = (Boolean)request.getAttribute(INIT_TAG);
		if(inited!=null && inited){
			return ;
		}
//		LocaleContextHolder.getLocaleContext();
		getRequest().ifPresent(req->{
			req.setAttribute(RESPONSE_TAG, response);
		});
	}
	
	public static Optional<HttpServletRequest> getRequest(){
		Optional<HttpServletRequest> req = getSpringContextHolderRequest();
//		return req.isPresent()?req:Optional.ofNullable(REQUEST_HOLDER.get());
		return req;
	}
	
	public static Optional<HttpServletResponse> getResponse(){
		return getRequest().map(req->(HttpServletResponse)req.getAttribute(RESPONSE_TAG));
	}
	
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
