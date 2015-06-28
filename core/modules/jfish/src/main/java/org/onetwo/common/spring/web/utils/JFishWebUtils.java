package org.onetwo.common.spring.web.utils;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.fish.JFishUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.WebHelper;
import org.onetwo.common.spring.web.mvc.SingleReturnWrapper;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.SsoTokenable;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.csrf.SameInSessionCsrfPreventor;
import org.onetwo.common.web.utils.Escape;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.utils.WebHolder;
import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@SuppressWarnings("unchecked")
public final class JFishWebUtils {

	private static final Logger logger = JFishLoggerFactory.getLogger(JFishWebUtils.class);
	
	public static final String REQUEST_PARAMETER_KEY = "__JFISH_REQUEST_PARAMETER__";
	public static final String REQUEST_HELPER_KEY = WebHelper.WEB_HELPER_KEY;

	@Deprecated
	public static final String DEFAULT_TOKEN_NAME = "__JFISH_FORM_TOKEN__";
	@Deprecated
	public static final String DEFAULT_TOKEN_FIELD_NAME = SameInSessionCsrfPreventor.DEFAULT_CSRF_TOKEN_FIELD;
	
	

	public static final String REDIRECT_KEY = "redirect:";
	public static final String GRID_SEARCH_FORM_SUBMIT = "submitTag";

	public static final String COOKIE_PATH;
	public static final String COOKIE_DOMAIN;
	
	static {
		String domain = "";
		String path = "";
		try {
			domain = BaseSiteConfig.getInstance().getCookieDomain();
			path = BaseSiteConfig.getInstance().getCookiePath();
			path = StringUtils.appendEndWith(path, "/");
		} catch (Exception e) {
			logger.error("use default domain,  because read domain path error : "+e.getMessage());
		}
		COOKIE_DOMAIN = domain;
		COOKIE_PATH = path;
	}
	
	private JFishWebUtils(){
	}
	
	public static Locale getLocale(){
		Locale local = null;
		try {
			HttpServletRequest request = request();
			if(request!=null)
				local = request().getLocale();
			else
				local = JFishUtils.getDefaultLocale();
		} catch (Exception e) {
			local = JFishUtils.getDefaultLocale();
		}
		return local;
	}
	
	public static boolean isRedirect(ModelAndView mv){
		return mv.getViewName().startsWith(REDIRECT_KEY);
	}
	
	public static void req(String name, Object value){
//		RequestContextHolder.getRequestAttributes().setAttribute(name, value, RequestAttributes.SCOPE_REQUEST);
		WebHolder.getRequest().setAttribute(name, value);
	}
	
	public static <T> T req(String name){
//		return (T)RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_REQUEST);
		return (T)WebHolder.getRequest().getAttribute(name);
	}
	
	public boolean isGridSearchFormSubmit(){
		return "1".equals(req(GRID_SEARCH_FORM_SUBMIT));
	}
	
	public static String getRemoteAddr(){
		return RequestUtils.getRemoteAddr(request());
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
//		RequestContextHolder.getRequestAttributes().setAttribute(name, value, RequestAttributes.SCOPE_SESSION);
		WebHolder.getSession().setAttribute(name, value);
	}
	
	public static <T> T session(String name){
//		return (T)RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_SESSION);
		return (T)WebHolder.getSession().getAttribute(name);
	}
	
	/*public static void parameters(Map<?, ?> params){
		RequestContextHolder.getRequestAttributes().setAttribute(REQUEST_PARAMETER_KEY, params, RequestAttributes.SCOPE_REQUEST);
	}*/
	
	public static void removeSession(String name){
//		RequestContextHolder.getRequestAttributes().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
		WebHolder.getSession().removeAttribute(name);
	}
	

	public static String getTokenName(HttpServletRequest request){
		String tokenName = request.getParameter(DEFAULT_TOKEN_FIELD_NAME);
		return StringUtils.isBlank(tokenName)?DEFAULT_TOKEN_NAME:tokenName;
	}

	@Deprecated
	public static boolean validateToken(HttpServletRequest request){
		String tokenName = getTokenName(request);
		return validateToken(request, tokenName);
	}

	@Deprecated
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
		return removeUserDetail(UserDetail.USER_DETAIL_KEY);
	}
	
	public static <T extends UserDetail> T removeUserDetail(String key){
		UserDetail user = session(key);
		removeSession(key);
		removeAllSessionAttributes();
		return (T)user;
	}
	
	public static void removeAllSessionAttributes(){
//		String[] attrNames = RequestContextHolder.getRequestAttributes().getAttributeNames(RequestAttributes.SCOPE_SESSION);
		String[] attrNames = org.springframework.util.StringUtils.toStringArray(WebHolder.getSession().getAttributeNames());
		if(!LangUtils.isEmpty(attrNames)){
			for(String attr : attrNames)
				removeSession(attr);
		}
	}
	
	public static HttpServletRequest request(){
//		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return WebHolder.getRequest();
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
			return AbstractBaseController.REDIRECT + defaultUrl;
		}else{
			return AbstractBaseController.REDIRECT + redirectUrl;
		}
	}

	public static String redirect(String url){
		Assert.hasText(url);
		if(isRedirect(url))
			return url;
		return AbstractBaseController.REDIRECT+url;
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


	public static String getDownloadFileName(boolean encode) {
		return getDownloadFileName(request(), null, "default-filename", encode);
	}

	public static String getDownloadFileName(Map<String, Object> model, String defaultFileName) {
		return getDownloadFileName(request(), model, defaultFileName, true);
	}
	

	public static String getDownloadFileName(HttpServletRequest request, Map<String, Object> model, String defaultFileName){
		return getDownloadFileName(request, model, defaultFileName, true);
	}
	
	public static String getDownloadFileName(HttpServletRequest request, Map<String, Object> model, String defaultFileName, boolean encode){
		String downloadFileName = request.getParameter("fileName");
		if(StringUtils.isBlank(downloadFileName)){
			//在model里的，由用户自己转码
			downloadFileName = (model!=null && model.containsKey("fileName"))?model.get("fileName").toString():defaultFileName;
		}else{
			if(encode){
				downloadFileName = LangUtils.changeCharset(downloadFileName, "GBK", "ISO8859-1");
			}
			/*try {
				downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO8859-1");
			} catch (Exception e) {
				throw new BaseException("get down file name error: " +e.getMessage());
			}*/
		}
//		downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO8859-1");
		return downloadFileName;
	}
	
	public static void writeInputStreamTo(MultipartFile attachment, String dir, String fileName){
		try {
			FileUtils.writeInputStreamTo(attachment.getInputStream(), dir, fileName);
		} catch (IOException e) {
			throw new ServiceException("write upload file error: " +e.getMessage(), e);
		}
	}


	public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxage, String domain, boolean escape) {
		if (escape)
			value = Escape.escape(value);
		Cookie cookie = new Cookie(name, value);
		if (StringUtils.isBlank(path))
			cookie.setPath(COOKIE_PATH);
		else
			cookie.setPath(path);
		if (maxage > 0)
			cookie.setMaxAge(maxage);
		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}

	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, COOKIE_PATH, -1, COOKIE_DOMAIN, false);
	}
	/**********
	 * path = / domain siteconfig['cookie.domain']
	 * 
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void setHttpOnlyCookie(HttpServletResponse response, String name, String value) {
		ResponseUtils.setHttpOnlyCookie(response, name, value, COOKIE_PATH, -1, COOKIE_DOMAIN);
	}

	/**********
	 * 
	 * path = / domain siteconfig['cookie.domain']
	 * 
	 * @param response
	 * @param name
	 */
	public static void removeHttpOnlyCookie(HttpServletResponse response, String name) {
		ResponseUtils.setHttpOnlyCookie(response, name, "", COOKIE_PATH, 0, COOKIE_DOMAIN);//删除不能传-1，否则删除不了
	}


	public static void removeCookie(HttpServletResponse response, String name) {
		ResponseUtils.removeCookie(response, name, COOKIE_PATH, COOKIE_DOMAIN);
	}

	public static void removeCookieToken(HttpServletResponse response){
		removeHttpOnlyCookie(response, SsoTokenable.TOKEN_KEY);
	}
	
	public static void setCookieToken(HttpServletResponse response, String token){
		setHttpOnlyCookie(response, SsoTokenable.TOKEN_KEY, token);
	}
	
}
