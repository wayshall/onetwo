package org.onetwo.common.web.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ParamUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.ParamMap;
import org.onetwo.common.web.utils.Browsers.BrowserMeta;
import org.onetwo.common.web.utils.RequestTypeUtils.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.UrlPathHelper;

@SuppressWarnings("rawtypes")
public final class RequestUtils {
	
	final static private Logger logger = LoggerFactory.getLogger(RequestUtils.class);
	
	public static final String UNKNOWN = "unknown";
	public static final String HTTP_KEY = "http://";
	public static final String HTTPS_KEY = "https://";
	public static final String REQUEST_URI = "org.onetwo.web.requestUri";
	
    public static final String CONTENT_LENGTH = "Content-length";
	private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();
	
	
	@SuppressWarnings("serial")
	private static final Map<String, String> AGENT_OS = new LinkedHashMap<String, String>(){
		{
			put("android 2.2", "Android 2.2");
			put("android 2.3", "Android 2.3");
			put("android 4", "Android 4");
			put("windows nt 6.0", "Womdows Vista");
			put("windows nt 6.1", "Womdows 7");
			put("windows nt 5.2", "Womdows 2003");
			put("windows nt 5.1", "Windows xp");
			put("windows nt 5.0", "Windows 2000");
			put("windows nt 4.0", "Windows nt");
			put("windows 98", "Windows 98");
			put("sunos", "SunOS");
			put("android", "Android");
			put("linux", "Linux");
			put("mac os", "Mac OS");
			put("iphone os", "iPhone OS");
		}
	};

	/**
	 * 通过User-Agent 取到 浏览器的信息
	 * @param userAgent
	 * @return
	 * @throws Exception
	 */
	public static String getBrowerByAgent(String userAgent){
		return getBrowerMetaByAgent(userAgent).getName();
	}

	public static BrowserMeta getBrowerMetaByAgent(HttpServletRequest request){
		return getBrowerMetaByAgent(getUserAgent(request));
	}
	public static BrowserMeta getBrowerMetaByAgent(String userAgent){
		if(StringUtils.isBlank(userAgent)){
			return Browsers.UNKNOW;
		}
		userAgent = userAgent.toLowerCase();
		for(String agent : Browsers.getAgentBrowsers().keySet()){
			if(userAgent.indexOf(agent)!=-1){
				return Browsers.getBrowser(agent);
			}
		}
		return Browsers.UNKNOW;
	}
	public static String getBrowerByAgent(HttpServletRequest request){
		return getBrowerByAgent(getUserAgent(request));
	}
	public static String getUserAgent(HttpServletRequest request){
		return request.getHeader("User-Agent");
	}
	
	public static boolean isAaXmlRequest(HttpServletRequest request){
//		return AAUtils.isAjaxRequest(request);
		return false;
	}
	
	public static String getJsonContextTypeByUserAgent(HttpServletRequest request){
		BrowserMeta meta = RequestUtils.getBrowerMetaByAgent(request);
		String contextType = ResponseUtils.JSON_TYPE;
		//如果是ie某些低版本，必须设置为html，否则会导致json下载
		if(meta.isFuckingBrowser()){
			contextType = ResponseUtils.HTML_TYPE;
		}
		return contextType;
	}
	
	public static String getOSByAgent(String userAgent){
		userAgent = userAgent.toLowerCase();
		for(String agent : AGENT_OS.keySet()){
			if(userAgent.indexOf(agent)!=-1){
				return AGENT_OS.get(agent);
			}
		}
		return "Other";
	}
	
	/**
	 * 获取客户端的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request) {

		String ip = request.getHeader("X-Real-IP");
		
		if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		
		if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-Host");//
		}

		if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (StringUtils.isBlank(ip)|| UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}
	
	public static String noContextPath(HttpServletRequest request, String path){
		String contextPath = request.getContextPath();
		int index = path.indexOf(contextPath);
		if(index!=-1){
			path = path.substring(index+contextPath.length());
		}
		return path;
	}
	
	public static Optional<String> getCurrentServletPath() {
		return WebHolder.getRequest().map(req->getServletPath(req));
	}
	public static String getServletPath(HttpServletRequest request) {
        String servletPath = (String)request.getAttribute(REQUEST_URI);
        if(StringUtils.isNotBlank(servletPath))
        	return servletPath;
        
        servletPath = request.getServletPath();
        
        String requestUri = request.getRequestURI();
        // Detecting other characters that the servlet container cut off (like anything after ';')
        if (requestUri != null && servletPath != null && !requestUri.endsWith(servletPath)) {
            int pos = requestUri.indexOf(servletPath);
            if (pos > -1) {
                servletPath = requestUri.substring(requestUri.indexOf(servletPath));
            }
        }
        
        if (null != servletPath && !"".equals(servletPath)) {
            return servletPath;
        }
        
        int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
        int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.lastIndexOf(request.getPathInfo());

        if (startIndex > endIndex) { // this should not happen
            endIndex = startIndex;
        }

        servletPath = requestUri.substring(startIndex, endIndex);
        return requestUri;
    }

	public static String getContextRequestPath(HttpServletRequest request) {
		return request.getContextPath() + getServletPath(request);
	}
	
	public static String getRequestFullURI(HttpServletRequest request){
		String uri = request.getRequestURI();
		uri += request.getQueryString();
		return uri;
	}

	public static ParamMap getPostParametersWithout(HttpServletRequest request, String... prefix){
		return getParametersWithout(request, prefix).subtract(getGetParameter(request));
	}
	
	public static ParamMap getParametersWithout(HttpServletRequest request, String... prefix){
		return new ParamMap().addMapWithFilter(getParameters(request), prefix);
	}
	
	public static Map getParameters(HttpServletRequest request){
		Assert.notNull(request);
		return request.getParameterMap();
	}

	public static Map<Object, Collection<Object>> getGetParameter(HttpServletRequest request){
		String q = request.getQueryString();
		return new ParamMap(q);
	}
	

	/**
	 * 获取cookie的值
	 * 匹配名称相等的最后一个cookie，避免某些情况下（登录redirect）带有同一个名称的两个cookies时，匹配了旧的而忽略了新的
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookieName.equals(cookies[i].getName())) {
					cookie = cookies[i];
					// 匹配了也不返回，匹配名称相等的最后一个cookie
//					return cookie.getValue();
				}
			}
		}
		return cookie!=null?cookie.getValue():null;
	}
	
	public static String getUnescapeCookieValue(HttpServletRequest request, String cookieName) {
		String cookieValue = getCookieValue(request, cookieName);
		if(StringUtils.isNotBlank(cookieValue))
			cookieValue = Escape.unescape(cookieValue);
		return cookieValue;
	}

	public static String getRefereURL(HttpServletRequest request){
		return request.getHeader("referer");
	}
	public static String appendParam(String action, String name, String value){
		return ParamUtils.appendParam(action, name, value);
	}
	
	public static String appendParamString(String action, String paramstr){
		return ParamUtils.appendParamString(action, paramstr);
	}
	
	public static boolean isServlet3(){
		return ClassUtils.isPresent("javax.servlet.SessionCookieConfig", ClassUtils.getDefaultClassLoader());
	}
	
	public static MediaType getAcceptAsMediaType(HttpServletRequest request){
		String accept = request.getHeader("Accept");
		try {
			MediaType mtype = MediaType.valueOf(accept);
			return mtype;
		} catch (Exception e) {
			logger.error("parse [{}] as MediaType error: " + e.getMessage(), accept);
			return null;
		}
	}
	

	public static boolean isAjaxRequest(HttpServletRequest request){
		MediaType mtype = getAcceptAsMediaType(request);
		return MediaType.APPLICATION_JSON.isCompatibleWith(mtype) || 
				MediaType.APPLICATION_ATOM_XML.isCompatibleWith(mtype) || 
				RequestType.Ajax.equals(RequestTypeUtils.getRequestType(request)) || 
				"json".equalsIgnoreCase(getRequestExtension(request)) || 
				RequestType.Flash.equals(RequestTypeUtils.getRequestType(request)) || 
				"true".equalsIgnoreCase(request.getParameter("ajaxRequest"));
	}
	
	public static boolean isAjaxHandlerMethod(Object handlerMethod){
		HandlerMethod hm = getHandlerMethod(handlerMethod);
		if(hm==null){
			return false;
		}
		return hm.hasMethodAnnotation(ResponseBody.class);
	}
	
	public static HandlerMethod getHandlerMethod(Object handler){
		if(handler instanceof HandlerMethod){
			return (HandlerMethod)handler;
		}
		return null;
	}

	public static String getRequestExtension(HttpServletRequest request) {
		String requestUri = getUrlPathHelper().getLookupPathForRequest(request);
//		String reqUri = WebUtils.extractFullFilenameFromUrlPath(requestUri);
//		String extension = FileUtils.getExtendName(reqUri);
		String extension = UriUtils.extractFileExtension(requestUri);
		return extension;
	}

	public static ResponseType getResponseType(HttpServletRequest request){
		String ext = getRequestExtension(request);
		if (StringUtils.isBlank(ext)) {
			MediaType  mtype = getAcceptAsMediaType(request);
			if (MediaType.APPLICATION_JSON.isCompatibleWith(mtype)) {
				return ResponseType.JSON;
			}
		}
		return ResponseType.of(ext);
	}

	public static UrlPathHelper getUrlPathHelper() {
		return URL_PATH_HELPER;
	}
	
	public static long getContentLength(HttpServletRequest request) {
        long size;
        try {
            size = Long.parseLong(request.getHeader(CONTENT_LENGTH));
        } catch (NumberFormatException e) {
            size = request.getContentLength();
        }
        return size;
    }
	

	public static String getReuqestFullUrl(HttpServletRequest request){
		String url = RequestUtils.buildFullRequestUrl(request.getScheme(), request.getServerName(), request.getServerPort(), request.getRequestURI(), request.getQueryString());
		return url;
	}
	

	public static String buildFullRequestUrl(String scheme, String serverName, int serverPort, String requestURI, String queryString) {

		scheme = scheme.toLowerCase();

		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);

		// Only add port if not default
		if ("http".equals(scheme)) {
			if (serverPort != 80) {
				url.append(":").append(serverPort);
			}
		}
		else if ("https".equals(scheme)) {
			if (serverPort != 443) {
				url.append(":").append(serverPort);
			}
		}

		// Use the requestURI as it is encoded (RFC 3986) and hence suitable for
		// redirects.
		url.append(requestURI);

		if (queryString != null) {
			url.append("?").append(queryString);
		}

		return url.toString();
	}
	
	public static Optional<HttpSession> getSession(HttpServletRequest request){
		if(request==null){
			return Optional.empty();
		}
		HttpSession session = request.getSession();
		return Optional.ofNullable(session);
	}
	

	public static boolean isHttpPath(String path){
		if (StringUtils.isBlank(path)) {
			return false;
		}
		final String subPath = path.toLowerCase();
		return subPath.startsWith(HTTP_KEY) || subPath.startsWith(HTTPS_KEY);
	}
	
	private RequestUtils(){
	}
}
