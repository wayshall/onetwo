package org.onetwo.common.spring.web.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.BaseController;
import org.onetwo.common.spring.web.WebHelper;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;


@SuppressWarnings("unchecked")
public final class JFishWebUtils {

	public static final String REQUEST_EXTENSION_KEY = "__JFISH_REQUEST_EXTENSION__";
	public static final String REQUEST_URI_KEY = "__JFISH_REQUEST_URI__";
	public static final String REQUEST_PARAMETER_KEY = "__JFISH_REQUEST_PARAMETER__";
	public static final String REQUEST_HANDLER_KEY = "__JFISH_REQUEST_HANDLER__";
	public static final String REQUEST_HELPER_KEY = "__JFISH_REQUEST_HELPER__";

	public static final String DEFAULT_TOKEN_NAME = "__JFISH_FORM_TOKEN__";
	public static final String DEFAULT_TOKEN_FIELD_NAME = "org.onetwo.jfish.form.token";
	
	public static final Locale DEFAULT_LOCAL = Locale.CHINA;
	

	public static final String REDIRECT_KEY = "redirect:";
	
	private JFishWebUtils(){
	}
	
	public static Locale getLocale(){
		Locale local = null;
		try {
			HttpServletRequest request = request();
			if(request!=null)
				local = request().getLocale();
			else
				local = DEFAULT_LOCAL;
		} catch (Exception e) {
			local = DEFAULT_LOCAL;
		}
		return local;
	}
	
	public static boolean isRedirect(ModelAndView mv){
		return mv.getViewName().startsWith(REDIRECT_KEY);
	}
	
	public static void req(String name, Object value){
		RequestContextHolder.getRequestAttributes().setAttribute(name, value, RequestAttributes.SCOPE_REQUEST);
	}
	
	public static <T> T req(String name){
		return (T)RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}
	
	public static void currentHandler(Object handler){
		req(REQUEST_HANDLER_KEY, handler);
	}
	
	public static Object currentHandler(){
		return req(REQUEST_HANDLER_KEY);
	}
	
	public static HandlerMethod currentHandlerMethod(){
		Object handler = currentHandler();
		if(HandlerMethod.class.isInstance(handler))
			return (HandlerMethod)handler;
		return null;
	}
	
	public static <T> T currentController(){
		HandlerMethod handler = currentHandlerMethod();
		if(handler!=null){
			return (T)handler.getBean();
		}
		return null;
	}
	
	public static <T> T currentTypeController(Class<T> clazz){
		HandlerMethod handler = currentHandlerMethod();
		if(handler!=null && clazz.isInstance(handler.getBean())){
			return (T)handler.getBean();
		}
		return null;
	}

	
	public static void session(String name, Object value){
		RequestContextHolder.getRequestAttributes().setAttribute(name, value, RequestAttributes.SCOPE_SESSION);
	}
	
	public static <T> T session(String name){
		return (T)RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_SESSION);
	}
	
	/*public static void parameters(Map<?, ?> params){
		RequestContextHolder.getRequestAttributes().setAttribute(REQUEST_PARAMETER_KEY, params, RequestAttributes.SCOPE_REQUEST);
	}*/
	
	public static void removeSession(String name){
		RequestContextHolder.getRequestAttributes().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
	}
	

	public static String getTokenName(HttpServletRequest request){
		String tokenName = request.getParameter(DEFAULT_TOKEN_FIELD_NAME);
		return StringUtils.isBlank(tokenName)?DEFAULT_TOKEN_NAME:tokenName;
	}

	public static boolean validateToken(HttpServletRequest request){
		String tokenName = getTokenName(request);
		return validateToken(request, tokenName);
	}
	
	public static boolean validateToken(HttpServletRequest request, String tokenName){
		String reqToken = request.getParameter(tokenName);
		String sessionToken = session(tokenName);
		if(StringUtils.isBlank(reqToken) && StringUtils.isBlank(sessionToken))
			return true;
		
		try {
			if(StringUtils.isBlank(reqToken)){
				return false;
			}else{
				return reqToken.equalsIgnoreCase(sessionToken);
			}
		} finally{
			removeSession(tokenName);
		}
	}
	
	public static <T extends UserDetail> T currentLoginUser(){
		return session(UserDetail.USER_DETAIL_KEY);
	}
	
	public static <T extends UserDetail> T getUserDetail(){
		return session(UserDetail.USER_DETAIL_KEY);
	}
	
	public static void setUserDetail(UserDetail userDetail){
		session(UserDetail.USER_DETAIL_KEY, userDetail);
	}
	
	public static <T extends UserDetail> T removeUserDetail(){
		UserDetail user = session(UserDetail.USER_DETAIL_KEY);
		removeSession(UserDetail.USER_DETAIL_KEY);
		return (T)user;
	}
	
	public static HttpServletRequest request(){
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	public static void requestExtension(String ext){
		req(REQUEST_EXTENSION_KEY, ext);
	}
	
	public static String requestExtension(){
		Object val = req(REQUEST_EXTENSION_KEY);
		return val==null?"":val.toString();
	}
	
	public static boolean isJsonFormat(){
		return "json".equalsIgnoreCase(requestExtension());
	}
	
	public static boolean isXmlFormat(){
		return "xml".equalsIgnoreCase(requestExtension());
	}
	
	public static void requestUri(String uri){
		req(REQUEST_URI_KEY, uri);
	}
	
	public static String requestUri(){
		Object val = req(REQUEST_URI_KEY);
		return val==null?"":val.toString();
	}
	
	public static void webHelper(WebHelper helper){
		req(REQUEST_HELPER_KEY, helper);
	}
	
	public static WebHelper webHelper(){
		WebHelper help = req(REQUEST_HELPER_KEY);
		return help;
	}
	
	public static WebHelper webHelper(HttpServletRequest request){
		WebHelper helper = webHelper();
		if(helper==null){
			helper = WebHelper.newHelper(request);
			webHelper(helper);
		}
		return helper;
	}
	
	public static boolean isRedirect(String viewName){
		return viewName!=null && viewName.startsWith(BaseController.REDIRECT);
	}

	public static String redirectUrl(String redirectUrl, String defaultUrl){
		if(StringUtils.isBlank(redirectUrl)){
			return "redirect:" + defaultUrl;
		}else{
			return "redirect:" + redirectUrl;
		}
	}
}
