package org.onetwo.common.web.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.SiteConfig;

public class CookieUtil {

//	public static final String COOKIE_TOKENNAME = "ciipptoken";
	public static final String COOKIE_LAST_ACTIVIE_NNAME = "last_activie";
	
	public static final String COOKIE_PATH = "/";
	public static final String DOMAIN_CONFIGNAME = "cookie.domain";
	public static final String LANGUAGE = "cookie.language";
	public static final String CURRENT_LANGUAGE = "cookie.current.language";

	public static final int WEEK = 60 * 60 * 24 * 7;

	/**
	 * 把cookie放到 response
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param path
	 * @param maxage
	 *            默认-1，表示浏览器关闭cookie关闭
	 * @param domain
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxage, String domain, boolean escape) {
		ResponseUtils.setCookie(response, name, value, path, maxage, domain, escape);
	}

	/**
	 * 设置HttpOnly Cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param path
	 * @param maxage 小时数
	 * @param domain
	 */
	public static void setHttpOnlyCookie(HttpServletResponse response, String name, String value, String path, int maxage, String domain) {
		ResponseUtils.setHttpOnlyCookie(response, name, value, path, maxage, domain);
	}
	
	public static void setCookie(String name, String value) {
		setCookie(StrutsUtils.getResponse(), name, value, Integer.MIN_VALUE);
	}

	public static void setCookie(String name, String value, int maxage) {
		setCookie(StrutsUtils.getResponse(), name, value, maxage);
	}

	public static void setCookie(HttpServletResponse response, String name, String value, int maxage) {
		setCookie(response, name, value, COOKIE_PATH, maxage, SiteConfig.getConfig(DOMAIN_CONFIGNAME), false);
	}

	//当前语言
	public static void setCookieCurrentLanguage(HttpServletResponse response, String value) {
		setCookie(response, CURRENT_LANGUAGE, value, COOKIE_PATH, -1, SiteConfig.getConfig(DOMAIN_CONFIGNAME), true);
	}
	
	public static String getCookieCurrentLanguage(HttpServletRequest request) {
		return getUnescapeCookieValue(request, CURRENT_LANGUAGE);
	}

	//设定语言
	public static void setCookieLanguage(HttpServletResponse response, String value) {
		setCookie(response, LANGUAGE, value, COOKIE_PATH, SiteConfig.getInstance().getCookieLanguageAge(), SiteConfig.getConfig(DOMAIN_CONFIGNAME), true);
	}
	
	public static String getCookieLanguage(HttpServletRequest request) {
		return getUnescapeCookieValue(request, LANGUAGE);
	}
	
	//set time
	public static void setLastLogTime(Date date){
		Long now = System.currentTimeMillis();
		if(date!=null)
			now = date.getTime();
		setCookie(StrutsUtils.getResponse(), COOKIE_LAST_ACTIVIE_NNAME, now.toString(), COOKIE_PATH, -1, SiteConfig.getConfig(DOMAIN_CONFIGNAME), true);
	}
	
	public static Date getLastLogTime(){
		String value = getCookieValue(StrutsUtils.getRequest(), COOKIE_LAST_ACTIVIE_NNAME);
		Date lastLog = null;
		try {
			Long l = Long.parseLong(value);
			lastLog = new Date(l);
		} catch (Exception e) {
			lastLog = new Date();
		}
		return lastLog;
	}

	public static void removeLastLog(){
		removeCookie(COOKIE_LAST_ACTIVIE_NNAME, COOKIE_PATH, SiteConfig.getConfig(DOMAIN_CONFIGNAME));
	}
	
	//token
	public static String getCookieToken(){
		String key = UserKeyManager.getCurrentUserKeyManager().getCurrentTokenKey();
		return getCookieValue(StrutsUtils.getRequest(), key);
	}

	public static void setCookieToken(String token){
		String key = UserKeyManager.getCurrentUserKeyManager().getCurrentTokenKey();
		setCookie(StrutsUtils.getResponse(), key, token, CookieUtil.COOKIE_PATH, -1, SiteConfig.getConfig(CookieUtil.DOMAIN_CONFIGNAME), true);
	}

	public static void removeCookieToken(){
		String key = UserKeyManager.getCurrentUserKeyManager().getCurrentTokenKey();
		removeCookie(key, COOKIE_PATH, SiteConfig.getConfig(DOMAIN_CONFIGNAME));
	}
	
	public static void setAllCookies(UserDetail user){
		setCookieToken(user.getToken());
	}
	
	public static void removeAllCookies(){
		if(StrutsUtils.getResponse()==null)
			return ;
		removeCookieToken();
//		removeLastLog();
	}

	/**
	 * 获取cookie的值
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		return ResponseUtils.getCookieValue(request, cookieName);
	}

	public static String getCookieValue(String cookieName) {
		return getCookieValue(StrutsUtils.getRequest(), cookieName);
	}
	
	public static String getUnescapeCookieValue(HttpServletRequest request, String cookieName) {
		return ResponseUtils.getUnescapeCookieValue(request, cookieName);
	}

	/**
	 * 删除cookie
	 * 
	 * @param response
	 * @param name
	 * @param path
	 */

	public static void removeCookie(String name, String path, String domain) {
		ResponseUtils.removeCookie(StrutsUtils.getResponse(), name, path, domain);
	}
	
	
	public static void removeCookie(String name) {
		String domain = SiteConfig.getConfig(DOMAIN_CONFIGNAME);
		removeCookie(name, COOKIE_PATH, domain);
	}

	public static void main(String[] args){
		Long l = System.currentTimeMillis();
		Date now = new Date(l);
		System.out.println(now.toLocaleString());
	}
}
