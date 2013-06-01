package org.onetwo.common.web.utils;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.CalendarConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.utils.json.JSONUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.common.web.s2.BaseAction;
import org.onetwo.common.web.s2.ext.LocaleUtils;
import org.onetwo.common.web.subdomain.SubDomainFilter;
import org.onetwo.common.web.subdomain.SubdomainInfo;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.ValidationWorkflowAware;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;

@SuppressWarnings("unchecked")
public class StrutsUtils {

	protected static final Logger LOG = Logger.getLogger(StrutsUtils.class);

	//-- content-type 常量定义 --//
	public static final String TEXT_TYPE = "text/plain; charset=UTF-8";
	public static final String JSON_TYPE = "application/json; charset=UTF-8";
	public static final String XML_TYPE = "text/xml; charset=UTF-8";
	public static final String HTML_TYPE = "text/html; charset=UTF-8";
	public static final String JS_TYPE = "text/javascript";
	
	public static final String ACTION_URI_NO_QUERY = "org.onetwo.request.action_uri_no_query";
	public static final String ACTION_URI = "org.onetwo.request.action_uri";
	public static final String LOCALE_SESSION_ATTRIBUTE = LocaleUtils.ATTRIBUTE_KEY;
	public static final String HTTP_START_KEY = "http://";
	public static final String HTTPS_START_KEY = "https://";
	public static final Properties DOWNLOAD_CONFIG = new Properties();
	
	static{
		
		DOWNLOAD_CONFIG.setProperty("doc", "application/msword");
		DOWNLOAD_CONFIG.setProperty("pdf", "application/pdf");
		DOWNLOAD_CONFIG.setProperty("jpeg", "image/jpeg");
		DOWNLOAD_CONFIG.setProperty("jpg", "image/jpeg");
		DOWNLOAD_CONFIG.setProperty("gif", "image/gif");
	}

	
	private StrutsUtils(){
	}

	public static void redirect(String path) {
		ResponseUtils.redirect(getResponse(), path);
	}

	public static void forward(String path) {
		ResponseUtils.forward(getRequest(), getResponse(), path);
	}


 	/**
 	 * 获取客户端的IP地址
 	 * @param request
 	 * @return
 	 */
 	public static String getRemoteAddr(HttpServletRequest request){
        return RequestUtils.getRemoteAddr(request);
 	}
 	
	
	public static Locale getCurrentSessionLocale(){
		return getCurrentSessionLocale(getRequest());
	}
	
	public static Locale getCurrentSessionLocale(HttpServletRequest request){
		return getCurrentSessionLocale(request, getRequestLocale(request));
	}
	
	/***
	 * 遍历request支持的语言
	 * @param request
	 * @return
	 */
	public static Locale getRequestLocale(HttpServletRequest request){
		Locale closestLocale = null;
		Enumeration<Locale> locales = (Enumeration<Locale>) request.getLocales();
		while(locales.hasMoreElements()){
			closestLocale = locales.nextElement();
			if (!LocaleUtils.isSupport(closestLocale.toString()))
				continue;
			closestLocale = LocaleUtils.getClosestLocale(closestLocale.toString());
			break;
		}
		return closestLocale;
	}
	
	/***
	 * 取得当前会话的locale
	 * 如果找不到则返回默认的locale
	 * @param request
	 * @param def 如果找不到时返回的locale
	 * @return
	 */
	public static Locale getCurrentSessionLocale(HttpServletRequest request, Locale def){
		Locale locale = (Locale) request.getSession().getAttribute(LocaleUtils.ATTRIBUTE_KEY);
		if(locale==null){
			locale = def;
			request.getSession().setAttribute(LocaleUtils.ATTRIBUTE_KEY, locale);
		}
		return locale;
	}
	
	/***
	 * 设置locale到session
	 * @param session
	 * @param locale
	 */
	public static void setCurrentSessioniLocale(HttpSession session, Locale locale){
		session.setAttribute(LOCALE_SESSION_ATTRIBUTE, locale);
	}
	
	/****
	 * 以字符串的形式返回当前会话的语言
	 * @return
	 */
	public static String getSessionLanguage(){
		return getCurrentSessionLocale().toString().toLowerCase();
	}
	
	/***
	 * 取得数据语言参数的统一接口
	 * 根据取得参数data_locale的值
	 * @return
	 */
	public static String getDataLanguage(){
		String data_language = getRequest().getParameter(LocaleUtils.DATA_LOCALE_ATTRIBUTE_KEY);
		if(StringUtils.isBlank(data_language))
			data_language = getRequest().getParameter(LocaleUtils.ID_LANGUAGE_ATTRIBUTE_KEY);
		return data_language;
	}
	
	/****
	 * 查找数据的语言版本，如果没有找到，则返回当前session中的语言字段
	 * @return
	 */
	public static String getTheDataLanguage(){
		String language = null;

		language = getRequest().getParameter(LocaleUtils.DATA_LOCALE_ATTRIBUTE_KEY);
		if(StringUtils.isBlank(language))
			language = getRequest().getParameter(LocaleUtils.ID_LANGUAGE_ATTRIBUTE_KEY);
		if(StringUtils.isBlank(language))
			language = getSessionLanguage();
		return language;
	}
	
	/***
	 * 如果language为空或all，则返回界面语言
	 * @return
	 */
	public static String getTheLanguage(){
		String language = null;

		language = getRequest().getParameter(LocaleUtils.DATA_LOCALE_ATTRIBUTE_KEY);
		if(StringUtils.isBlank(language))
			language = getRequest().getParameter(LocaleUtils.ID_LANGUAGE_ATTRIBUTE_KEY);
		if(language == null || !LocaleUtils.isSupport(language))
			language = getSessionLanguage();
		return language;
	}
	
	public static Object getBean(String beanName){
		return SpringApplication.getInstance().getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> clazz, String beanName){
		return (T) getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> clazz){
		return getBean(clazz, StringUtils.uncapitalize(clazz.getSimpleName()));
	}
	
	public static UserDetail getCurrentLoginUser(){
		HttpServletRequest request = getRequest();
		if(request==null)
			return null;
		return SessionUtils.getUserDetail(request.getSession(false));
	}
	
	public static <T> T getCurrentLoginUser(Class<T> clazz){
		UserDetail detail = getCurrentLoginUser();
		if(detail!=null && detail.getClass().equals(clazz))
			return (T) detail;
		return null;
	}
	
	/**
	 * qing
	 */
	public static void removeCurrentLoginUser(){
		if(StrutsUtils.getRequest()==null)
			return ;
		SessionUtils.removeUserDetail(getRequest().getSession(false));
	}
	
	/**
	 * qing
	 */
	public static void setCurrentLoginUser(UserDetail userDetail){
		SessionUtils.setUserDetail(getRequest().getSession(false), userDetail);
	}
	
	public static HttpServletRequest getRequest(){
		if(ActionContext.getContext()==null)
			return null;
		return ServletActionContext.getRequest();
	}
	
	public static HttpServletResponse getResponse(){
		if(ServletActionContext.getContext()==null)
			return null;
		return ServletActionContext.getResponse();
	}
	
	public static boolean isAjaxRequest(){
		return "XMLHttpRequest".equals(getRequest().getHeader("X-Requested-With"));
	}
	
	public static String getMessage(String key, Object... values){
		BaseAction action = (BaseAction)ActionContext.getContext().getActionInvocation().getAction();
		List list = new ArrayList();
		if(values!=null){
			for(Object obj : values)
				CollectionUtils.addIgnoreNull(list, obj);
		}
		return action.getText(key, list);
	}
	
	public static String getRequestURI(){
		return getRequestURI(ServletActionContext.getRequest());
	}
	
	public static String getRequestURI(HttpServletRequest request){
		Object action_uri = request.getAttribute(ACTION_URI);
		if(action_uri!=null)
			return action_uri.toString();
		
		StringBuffer url = new StringBuffer();
		url.append(request.getRequestURI());//.replace(ServletActionContext.getRequest().getContextPath(), ""));
		String q = request.getQueryString();
		if(!StringUtils.isBlank(q))
			url.append("?").append(q);
		return url.toString();
	}
	
	public static Map getGetParameter(){
		String q = getRequest().getQueryString();
		return new CasualMap(q);
	}
	
	public static Map getPostParameters(){
		return getPostParametersWithout();
	}
	
	public static CasualMap getPostParametersWithout(String... prefix){
		CasualMap all = new CasualMap();
		if(prefix!=null)
			all.addHttpParameterWithout(getParameters(), prefix);
		Map get = getGetParameter();
		return all.subtract(get);
	}
	
	public static Map getParameters(){
		return ActionContext.getContext().getParameters();
	}
	
	public static String getServletPath(){
		Object action_uri = ServletActionContext.getRequest().getAttribute(ACTION_URI_NO_QUERY);
		if(action_uri!=null)
			return action_uri.toString();
		return ServletActionContext.getRequest().getRequestURI();
	}
	
	public static String getRefereURL(){
		String url = getRequest().getHeader("referer");
		return url;
	}
	
	public static Properties getPropertiesConfig(String name){
		return (Properties) getBean(name);
	}
	
	public static Properties getDownloadConfig(){
		return DOWNLOAD_CONFIG;
	}
	
	public static String getContentType(String fileName){
		String ext = "";
		int index = fileName.lastIndexOf('.');
		if(index!=-1)
			ext = fileName.substring(index+1);
		return getDownloadConfig().getProperty(ext);
	}

	
	public static void render(String text, String contentType, boolean noCache){
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			if(!StringUtils.isBlank(contentType))
				response.setContentType(contentType);
			else
				response.setContentType(TEXT_TYPE);

			if (noCache) {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
			}
			PrintWriter pr = response.getWriter();
			pr.write(text);
		} catch (Exception e) {
			LOG.error("出错!", e);
			e.printStackTrace();
		}
	}

	public static void renderJSON(String text){
		render(text, JSON_TYPE, true);
	}
	public static void renderHTML(String text){
		render(text, HTML_TYPE, true);
	}
	
	public static void renderScriptMessage(String msg){
		StringBuilder text = new StringBuilder();
		text.append("<script>alert('").append(msg).append("')</script>");
		renderHTML(text.toString());
	}
	
	public static void outJson(Object datas){
		String text = getJsonString(datas);
		render(text, "application/json;charset=UTF-8", true);
	}

	public static String getJsonString(Object datas, String...fieldNames){
		return JSONUtils.getJsonString(datas, fieldNames);
	}
	
	public static JSONObject getJsonData(String jsonString){
		return JSONUtils.getJsonData(jsonString);
	}
	
	public static Map getJsonMap(String jsonString){
		return JSONUtils.getJsonMap(jsonString);
	}
	

	public static String getJsonString(Object datas, JsonConfig jsonconfig){
		String text = getJsonString(datas, jsonconfig, "");
		return text;
	}

	@SuppressWarnings("unchecked")
	public static void renderJsonp(final String callbackName, final Map contentMap) {
		String jsonParam = JSONUtils.getJsonString(contentMap);
		renderJsonp(callbackName, jsonParam);
	}
	
	public static void renderJsonp(final String callbackName, final String jsonParam) {
		StringBuilder result = new StringBuilder().append(callbackName).append("(").append(jsonParam).append(");");
		render(result.toString(), JS_TYPE, true);
	}

	public static String getJsonString(Object datas, JsonConfig jsonconfig, String...fieldNames){
		return JSONUtils.getJsonString(datas, jsonconfig, fieldNames);
	}
	
	public static boolean isHttp(String url){
		return url.startsWith(HTTP_START_KEY) || url.startsWith(HTTPS_START_KEY);
	}
	
	public static String getDomain(HttpServletRequest request){
		return getDomain(request.getRequestURL().toString());
	}
	

	/**
	 * 
	 * 返回域名
	 * 
	 * @param url
	 * @return
	 */
	public static String getDomain(String url){
		String domain = url;
		int start = -1;
		int end = -1;
		if(url.startsWith(HTTP_START_KEY)){
			start = HTTP_START_KEY.length();
			end = url.indexOf("/", start);
		}else if(url.startsWith(HTTPS_START_KEY)){
			start = HTTPS_START_KEY.length();
			end = url.indexOf("/", start);
		}else{
			end = url.indexOf('/');
		}
		
		if(end==-1)
			end = url.length();
		if(start!=-1)
			domain = url.substring(start, end);
		
		end = domain.indexOf(':');
		if(end!=-1)
			domain = domain.substring(0, end);
		/*
		if(domain.startsWith(GlobalKey.WWW))
			domain = domain.substring(GlobalKey.WWW.length());*/
		
		return domain.toLowerCase();
	}
	
	public static SubdomainInfo getCurrentSubDomain(){
		return (SubdomainInfo)getRequest().getAttribute(SubDomainFilter.CURRENT_SUB_DOMAIN);
	}
	
	public static void registerConvertor() {
		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
		ConvertUtils.register(new CalendarConverter(null), Calendar.class);
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
		ConvertUtils.register(new SqlTimeConverter(null), java.sql.Time.class);
		ConvertUtils.register(new SqlTimestampConverter(null), Timestamp.class);
    }
	
	public static String findInputConfig(ActionInvocation invocation, String inputResultName){
        Object action = invocation.getAction();
		String actualResult = inputResultName;

        if (action instanceof ValidationWorkflowAware) {
            actualResult = ((ValidationWorkflowAware) action).getInputResultName();
        }

        try {
            InputConfig annotation = action.getClass().getMethod(invocation.getProxy().getMethod(), ReflectUtils.EMPTY_CLASS_ARRAY).getAnnotation(InputConfig.class);
            if (annotation != null) {
                if (!annotation.methodName().equals("")) {
                    Method method = action.getClass().getMethod(annotation.methodName());
                    actualResult = (String) method.invoke(action);
                } else {
                    actualResult = annotation.resultName();
                }
            }
		} catch (Exception e) {
			LOG.error(e);
		}
        
        return actualResult;
	}
	
	public static String getRealPath(String path){
		return getRequest().getSession().getServletContext().getRealPath(path);
	}
	
	public static void main(String[] args){
		String d = "https://www.cifit.cn/";
		System.out.println(getDomain(d));
	}
}
