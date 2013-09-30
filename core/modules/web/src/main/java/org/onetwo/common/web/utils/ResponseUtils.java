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

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.slf4j.Logger;
import org.springframework.util.Assert;

abstract public class ResponseUtils {

	private static final Logger logger = MyLoggerFactory.getLogger(ResponseUtils.class);
	
	public static final String COOKIE_PATH = "/";
	public static final String COOKIE_DOMAIN;
	// public static final String COOKIE_DOMAIN =
	// "";//BaseSiteConfig.getInstance().getCookieDomain();

	public static final DateFormat COOKIE_DATA_FORMAT;
	static {
		DateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm:ss z", Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		COOKIE_DATA_FORMAT = df;
		
		String domain = "";
		try {
			domain = BaseSiteConfig.getInstance().getCookieDomain();
		} catch (Exception e) {
			logger.error("use default domain,  because read domain path error : "+e.getMessage());
		}
		COOKIE_DOMAIN = domain;
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

	/**********
	 * path = / domain siteconfig['cookie.domain']
	 * 
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void setHttpOnlyCookie(HttpServletResponse response, String name, String value) {
		setHttpOnlyCookie(response, name, value, COOKIE_PATH, -1, COOKIE_DOMAIN);
	}

	/**********
	 * 
	 * path = / domain siteconfig['cookie.domain']
	 * 
	 * @param response
	 * @param name
	 */
	public static void removeHttpOnlyCookie(HttpServletResponse response, String name) {
		setHttpOnlyCookie(response, name, "", COOKIE_PATH, 0, COOKIE_DOMAIN);
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

	public static void removeCookie(HttpServletResponse response, String name) {
		removeCookie(response, name, COOKIE_PATH, COOKIE_DOMAIN);
	}

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
