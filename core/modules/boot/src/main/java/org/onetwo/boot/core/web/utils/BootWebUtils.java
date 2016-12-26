package org.onetwo.boot.core.web.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.utils.BootUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.file.StoringFileContext;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.mvc.utils.MvcUtils;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator.SimpleResultBuilder;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.ParamMap;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.WebHolder;
import org.onetwo.common.web.utils.WebUtils;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;


@SuppressWarnings("unchecked")
public final class BootWebUtils {

	private static final Logger logger = JFishLoggerFactory.getLogger(BootWebUtils.class);
	
	public static final String REQUEST_PARAMETER_KEY = "__JFISH_REQUEST_PARAMETER__";
	public static final String REQUEST_HELPER_KEY = BootWebHelper.WEB_HELPER_KEY;
	

	public static final String REDIRECT_KEY = "redirect:";
	public static final String GRID_SEARCH_FORM_SUBMIT = "submitTag";
	
//	private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();
	
	private BootWebUtils(){
	}

	public static StoringFileContext createStoringFileContext(String module, MultipartFile file){
		try {
			return new StoringFileContext(module, file.getInputStream(), file.getOriginalFilename());
		} catch (IOException e) {
			throw new BaseException("create StoringFileContext error: " + file.getOriginalFilename());
		}
	}
	
	public static Locale getLocale(){
		Locale local = null;
		try {
			HttpServletRequest request = request();
			if(request!=null)
				local = request().getLocale();
			else
				local = BootUtils.getDefaultLocale();
		} catch (Exception e) {
			local = BootUtils.getDefaultLocale();
		}
		return local;
	}
	

	public static String getControllerPath(Class<? extends AbstractBaseController> controllerClass, String methodName, Object...params){
		Assert.notNull(controllerClass);
		String path = "";
		RequestMapping rm = controllerClass.getAnnotation(RequestMapping.class);
		String p = "";
		if(rm!=null){
			p = StringUtils.getFirstNotBlank(rm.value());
			if(StringUtils.isNotBlank(p)){
				p = StringUtils.trimEndWith(p, "/");
				path += p;
			}
		}
		Method method = ReflectUtils.findMethod(controllerClass, methodName);
		rm = method.getAnnotation(RequestMapping.class);
		p = StringUtils.getFirstNotBlank(rm.value());
		if(StringUtils.isNotBlank(p)){
			p = StringUtils.appendStartWith(p, "/");
			path += p;
		}
		/*if(BaseSiteConfig.getInstance().hasAppUrlPostfix()){
			path = BaseSiteConfig.getInstance().appendAppUrlPostfix(path);
		}*/
		if(!LangUtils.isEmpty(params)){
			ParamMap map = new ParamMap(LangUtils.asMap(params));
			path += "?"+map.toParamString();
		}
		return path;
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
	
	public static void webHelper(BootWebHelper helper){
		req(REQUEST_HELPER_KEY, helper);
	}
	
	public static BootWebHelper webHelper(){
		BootWebHelper help = req(REQUEST_HELPER_KEY);
		return help;
	}
	
	public static BootWebHelper webHelper(HttpServletRequest request){
		BootWebHelper helper = webHelper();
		if(helper==null){
			helper = BootWebHelper.newHelper(request);
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
	
	
	
	public static ModelAndView createModelAndView(String viewName, Object... models){
		return MvcUtils.createModelAndView(viewName, models);
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
	

	public static ServletRequestAttributes getServletRequestAttributes(){
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs;
	}


	public static HandlerMethod getHandlerMethod(Object handler){
		if(handler instanceof HandlerMethod){
			return (HandlerMethod)handler;
		}
		return null;
	}
	
	public static boolean isAjaxRequest(HttpServletRequest request){
		return RequestUtils.isAjaxRequest(request);
	}
	
	public static boolean isAjaxHandlerMethod(Object handlerMethod){
		HandlerMethod hm = getHandlerMethod(handlerMethod);
		if(hm==null){
			return false;
		}
		return hm.hasMethodAnnotation(ResponseBody.class);
	}
	
	public static ObjectMapper createObjectMapper(ApplicationContext applicationContext){
		ObjectMapper mapper = JsonMapper.IGNORE_NULL.getObjectMapper();
//		h4m.disable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);
		String clsName = "com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module";
		if(ClassUtils.isPresent(clsName, ClassUtils.getDefaultClassLoader())){
			Object h4m = ReflectUtils.newInstance(clsName);
			
			Class<?> featureCls = ReflectUtils.loadClass(clsName+"$Feature");
			Object field = ReflectUtils.getStaticFieldValue(featureCls, "SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS");
			ReflectUtils.invokeMethod("enable", h4m, field);

			field = ReflectUtils.getStaticFieldValue(featureCls, "FORCE_LAZY_LOADING");
			ReflectUtils.invokeMethod("disable", h4m, field);
			mapper.registerModule((Module)h4m);
		}
		List<Module> modules = SpringUtils.getBeans(applicationContext, Module.class);
		if(LangUtils.isNotEmpty(modules))
			mapper.registerModules(modules);
		return mapper;
	}
	
	public static SimpleResultBuilder buildErrorCode(SimpleResultBuilder builder, HttpServletRequest request, Exception exception){
		return WebUtils.buildErrorCode(builder, request, exception);
	}
}
