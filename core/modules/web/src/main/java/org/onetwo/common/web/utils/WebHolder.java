package org.onetwo.common.web.utils;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.NetUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
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
		return getRequest().map(req->{
			HttpServletResponse resp = (HttpServletResponse)req.getAttribute(RESPONSE_TAG);
			return resp;
		});
	}
	
	public static Optional<ServletRequestAttributes> getServletRequestAttributes(){
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs==null || !isRequestAttributesActive(attrs)) {
			return Optional.empty();
		}
		return Optional.of(attrs);
	}
	
	public static Optional<HttpServletRequest> getSpringContextHolderRequest(){
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs==null || !isRequestAttributesActive(attrs)) {
			return Optional.empty();
		}
		return Optional.of(attrs.getRequest());
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
	
	public static boolean isRequestAttributesActive(RequestAttributes req) {
		if (req==null) {
			return false;
		}
		try {
			return (boolean) SpringUtils.newPropertyAccessor(req, true).getPropertyValue("requestActive");
		} catch (Exception e) {
			JFishLoggerFactory.getCommonLogger().error("isRequestAttributesActive error: " + e.getMessage());
			return true;
		}
	}
	
	/*****
	 * 请求是否通过内网访问
	 * @return
	 */
	public static boolean isRequestFromInternal() {
		Optional<HttpServletRequest> reqOpt = getRequest();
		if (!reqOpt.isPresent()) {
			return false;
		}
		String ip = RequestUtils.getRemoteAddr(reqOpt.get());
		return NetUtils.isInternalIP(ip);
	}
}
