package org.onetwo.common.web.utils;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.date.DateUtil;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;

abstract public class ResponseUtils {

	private static final Logger logger = JFishLoggerFactory.getLogger(ResponseUtils.class);

	public static final String TEXT_TYPE = "text/plain; charset=UTF-8";
	public static final String JSON_TYPE = "application/json; charset=UTF-8";
	public static final String XML_TYPE = "text/xml; charset=UTF-8";
	public static final String HTML_TYPE = "text/html; charset=UTF-8";
	public static final String JS_TYPE = "text/javascript";
	
//	public static final String COOKIE_PATH;
//	public static final String COOKIE_DOMAIN;
	// public static final String COOKIE_DOMAIN =
	// "";//BaseSiteConfig.getInstance().getCookieDomain();

	public static final DateFormat COOKIE_DATA_FORMAT;
	private static final JsonMapper JSON_MAPPER = JsonMapper.IGNORE_NULL;
	
	static {
		DateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm:ss z", Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		COOKIE_DATA_FORMAT = df;

		/*String domain = "";
		String path = "";
		try {
			domain = BaseSiteConfig.getInstance().getCookieDomain();
			path = BaseSiteConfig.getInstance().getCookiePath();
			path = StringUtils.appendEndWith(path, "/");
		} catch (Exception e) {
			logger.error("use default domain,  because read domain path error : "+e.getMessage());
		}
		COOKIE_DOMAIN = domain;
		COOKIE_PATH = path;*/
	}

	/****
	 * 重定向
	 * 
	 * @param response
	 * @param path
	 */
	public static void redirect(HttpServletResponse response, String path) {
		try {
			response.sendRedirect(path);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public static void forward(HttpServletRequest request, HttpServletResponse response, String path) {
		RequestDispatcher rd = request.getRequestDispatcher(path);
		try {
			rd.forward(request, response);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 设置HttpOnly Cookie
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param path
	 * @param maxage
	 *            分钟
	 * @param domain
	 */
	public static void setHttpOnlyCookie(HttpServletResponse response, String name, String value, String path, int maxage, String domain) {
		Assert.hasLength(name);
		if (StringUtils.isBlank(value))
			value = "";

		StringBuffer cookie = new StringBuffer();

		cookie.append(name);
		cookie.append("=");
		cookie.append(Escape.escape(value));

		if (StringUtils.isBlank(path)) {
			path = "/";
		}
		cookie.append("; path=").append(path);

		if (maxage > 0) {
			Date expires = new Date((new Date()).getTime() + TimeUnit.MINUTES.toMillis(maxage));
			cookie.append("; expires=");
			cookie.append(COOKIE_DATA_FORMAT.format(expires));
		} else if (maxage == 0) {
			Date expires = new Date((new Date()).getTime() - TimeUnit.MINUTES.toMillis(60));
			cookie.append("; expires=");
			cookie.append(COOKIE_DATA_FORMAT.format(expires));
		}

		//本地时，不需要设置，设置了有些浏览器会读不到cookies
		if (StringUtils.isNotBlank(domain)) {
			cookie.append("; domain=").append(domain);
		}

		cookie.append("; HttpOnly");

		response.addHeader("Set-Cookie", cookie.toString());
	}

	/**
	 * 获取cookie的值
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		return RequestUtils.getCookieValue(request, cookieName);
	}

	public static String getUnescapeCookieValue(HttpServletRequest request, String cookieName) {
		return RequestUtils.getUnescapeCookieValue(request, cookieName);
	}

	/*
	*//**
	 * 删除cookie
	 * 
	 * @param response
	 * @param name
	 * @param path
	 */
	/*
	 * public static void removeCookie(HttpServletRequest request,
	 * HttpServletResponse response, String name) { Cookie[] cookies =
	 * request.getCookies(); if(cookies==null) return ; for(Cookie ck :
	 * cookies){ if(name.equals(ck.getName())){ ck.setMaxAge(0);
	 * response.addCookie(ck); } } }
	 */
	public static void removeCookie(HttpServletResponse response, String name, String path, String domain) {
		Cookie ck = new Cookie(name, "");
		ck.setMaxAge(0);
		if (StringUtils.isNotBlank(path)) {
			ck.setPath(path);
		}
		if (StringUtils.isNotBlank(domain)) {
			ck.setDomain(domain);
		}
		response.addCookie(ck);
	}

	public static void renderScript(PrintWriter out, String content) {
		renderScript(true, out, content);
	}

	public static void renderScript(boolean flush, PrintWriter out, String content) {
		if (StringUtils.isBlank(content))
			return;
		out.println("<script>");
		out.println(content);
		out.println("</script>");
		if (flush)
			out.flush();
	}


	public static void renderJsonp(HttpServletResponse response, final String callbackName, final Object params) {
		String jsonParam = JsonMapper.DEFAULT_MAPPER.toJson(params);
		renderJsonp(response, callbackName, jsonParam);
	}
	
	public static void renderJsonp(HttpServletResponse response, HttpServletRequest request, final String callbackParam) {
		String callback = request.getParameter(callbackParam);
		callback = StringUtils.isBlank(callback)?"callback":callback;
		StringBuilder result = new StringBuilder().append(callback).append("();");
		render(response, result.toString(), JS_TYPE, true);
	}
	
	public static void renderJsonp(HttpServletResponse response, final String callbackName, final String jsonParam) {
		StringBuilder result = new StringBuilder().append(callbackName).append("(").append(jsonParam).append(");");
		render(response, result.toString(), JS_TYPE, true);
	}
	
	public static void renderText(HttpServletResponse response, String text){
		render(response, text, null, false);
	}
	
	public static void renderJs(HttpServletResponse response, String text){
		render(response, text, JS_TYPE, true);
	}
	
	public static void renderJson(HttpServletResponse response, String text){
		render(response, text, JSON_TYPE, true);
	}
	
	public static void renderObjectAsJson(HttpServletResponse response, Object data){
		String text = JSON_MAPPER.toJson(data);
		render(response, text, JSON_TYPE, true);
	}
	
	public static void render(HttpServletResponse response, String text, String contentType, boolean noCache){
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
			pr.flush();
		} catch (Exception e) {
			logger.error("render error: " + e.getMessage(), e);
		}
	}

	public static void addP3PHeader(HttpServletResponse response){
		response.addHeader("P3P", "CP=\"NON DSP COR CURa ADMa DEVa TAIa PSAa PSDa IVAa IVDa CONa HISa TELa OTPa OUR UNRa IND UNI COM NAV INT DEM CNT PRE LOC\"");
//		response.addHeader("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
//		response.addHeader("P3P", "CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");
	}

	public static void main(String[] args) {
		Date now = new Date();
		String str = COOKIE_DATA_FORMAT.format(now);
		System.out.println("str: " + now.toLocaleString());
		System.out.println("str: " + str);

		now = new Date();
		now = DateUtil.addHours(now, 8);
		System.out.println("str3: " + now.toLocaleString());
		System.out.println("str3: " + COOKIE_DATA_FORMAT.format(now));
	}
}
