package org.onetwo.common.web.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.CasualMap;

@SuppressWarnings("rawtypes")
public abstract class RequestUtils {
	
	public static final String HTTP_KEY = "http://";
	public static final String HTTPS_KEY = "https://";
	public static final String REQUEST_URI = "org.onetwo.web.requestUri";
	
	@SuppressWarnings("serial")
	private static final Map<String, String> AGENT_BROWSER = new LinkedHashMap<String, String>(){
		{
			put("firefox", "Firefox");
			put("chrome", "Chrome");
			put("msie 8.0", "MSIE 8.0");
			put("msie 7.0", "MSIE 7.0");
			put("msie 6.0", "MSIE 6.0");
			put("msie 5.0", "MSIE 5.0");
			put("msie 4.0", "MSIE 4.0");
			put("opera", "Opera");
			put("mozilla/5.0", "Netscape");
		}
	};
	
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
		userAgent = userAgent.toLowerCase();
		for(String agent : AGENT_BROWSER.keySet()){
			if(userAgent.indexOf(agent)!=-1){
				return AGENT_BROWSER.get(agent);
			}
		}
		return "Other";
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

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
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
	
	public static String getRequestFullURI(HttpServletRequest request){
		String uri = request.getRequestURI();
		uri += request.getQueryString();
		return uri;
	}

	public static CasualMap getPostParametersWithout(HttpServletRequest request, String... prefix){
		return getParametersWithout(request, prefix).subtract(getGetParameter(request));
	}
	
	public static CasualMap getParametersWithout(HttpServletRequest request, String... prefix){
		return new CasualMap().addMapWithFilter(getParameters(request), prefix);
	}
	
	public static Map getParameters(HttpServletRequest request){
		Assert.notNull(request);
		return request.getParameterMap();
	}

	public static Map getGetParameter(HttpServletRequest request){
		String q = request.getQueryString();
		return new CasualMap(q);
	}
	

	/**
	 * 获取cookie的值
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookieName.equals(cookie.getName())) {
					return (cookie.getValue());
				}
			}
		}
		return null;
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
}
