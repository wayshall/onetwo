package org.onetwo.common.spring.web.utils;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.WebHelper;
import org.onetwo.common.spring.web.mvc.SingleReturnWrapper;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.csrf.AbstractCsrfPreventor;
import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;


@SuppressWarnings("unchecked")
public final class JFishWebUtils {

	private static final Logger logger = MyLoggerFactory.getLogger(JFishWebUtils.class);
	
	public static final String REQUEST_PARAMETER_KEY = "__JFISH_REQUEST_PARAMETER__";
	public static final String REQUEST_HELPER_KEY = WebHelper.WEB_HELPER_KEY;

	public static final String DEFAULT_TOKEN_NAME = "__JFISH_FORM_TOKEN__";
	public static final String DEFAULT_TOKEN_FIELD_NAME = AbstractCsrfPreventor.DEFAULT_CSRF_TOKEN_FIELD;
	
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
	
	public static Object currentHandler(){
		return webHelper()==null?null:webHelper().getControllerHandler();
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
	
	public static <T extends UserDetail> T currentLoginUser(Class<T> clazz){
		return clazz.cast(session(UserDetail.USER_DETAIL_KEY));
	}
	
	public static <T extends UserDetail> T getUserDetail(Class<T> clazz){
		return clazz.cast(session(UserDetail.USER_DETAIL_KEY));
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
		removeAllSessionAttributes();
		return (T)user;
	}
	
	public static void removeAllSessionAttributes(){
		String[] attrNames = RequestContextHolder.getRequestAttributes().getAttributeNames(RequestAttributes.SCOPE_SESSION);
		if(!LangUtils.isEmpty(attrNames)){
			for(String attr : attrNames)
				removeSession(attr);
		}
	}
	
	public static HttpServletRequest request(){
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	public static String requestExtension(){
		Object val = webHelper()==null?null:webHelper().getRequestExtension();
		return val==null?"":val.toString();
	}
	
	public static boolean isJsonFormat(){
		return "json".equalsIgnoreCase(requestExtension());
	}
	
	public static boolean isXmlFormat(){
		return "xml".equalsIgnoreCase(requestExtension());
	}
	
	public static String requestUri(){
		Object val = webHelper().getRequestURI();
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
		return viewName!=null && viewName.startsWith(AbstractBaseController.REDIRECT);
	}

	public static String redirectUrl(String redirectUrl, String defaultUrl){
		if(StringUtils.isBlank(redirectUrl)){
			return "redirect:" + defaultUrl;
		}else{
			return "redirect:" + redirectUrl;
		}
	}
	
	public static String getMessage(MessageSource exceptionMessage, String code, Object[] args, String defaultMessage, Locale locale){
		if(exceptionMessage==null)
			return "";
		try {
			return exceptionMessage.getMessage(code, args, defaultMessage, locale);
		} catch (Exception e) {
			logger.error("getMessage ["+code+"] error :" + e.getMessage(), e);
		}
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}
	
	public static Locale getDefaultLocale(){
		return JFishWebUtils.DEFAULT_LOCAL;
	}

	public static String getMessage(MessageSource exceptionMessage, String code, Object[] args) {
		if(exceptionMessage==null)
			return "";
		try {
			return exceptionMessage.getMessage(code, args, getDefaultLocale());
		} catch (Exception e) {
			logger.error("getMessage ["+code+"] error :" + e.getMessage(), e);
		}
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}
	
	public static ModelAndView mv(String viewName, Object... models){
		ModelAndView mv = new ModelAndView(viewName);
//		mv.getModel().put(UrlHelper.MODEL_KEY, getUrlMeta());
		if(LangUtils.isEmpty(models)){
			return mv;
		}
		
		if(models.length==1){
			if(Map.class.isInstance(models[0])){
				mv.addAllObjects((Map<String, ?>)models[0]);
			}else{
				mv.addObject(models[0]);
				mv.addObject(SingleReturnWrapper.wrap(models[0]));
//				mv.addObject(SINGLE_MODEL_FLAG_KEY, true);
			}
		}else{
			Map<String, ?> modelMap = LangUtils.asMap(models);
			mv.addAllObjects(modelMap);
		}
		return mv;
	}
	

	public static String getDownloadFileName(Map<String, Object> model, String defaultFileName) throws Exception{
		return getDownloadFileName(request(), model, defaultFileName);
	}
	
	public static String getDownloadFileName(HttpServletRequest request, Map<String, Object> model, String defaultFileName) throws Exception{
		String downloadFileName = request.getParameter("fileName");
		if(StringUtils.isBlank(downloadFileName)){
			downloadFileName = (model!=null && model.containsKey("fileName"))?model.get("fileName").toString():defaultFileName;
		}
		downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO8859-1");
		return downloadFileName;
	}
}
