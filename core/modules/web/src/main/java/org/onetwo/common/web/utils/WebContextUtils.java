package org.onetwo.common.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.UserDetail;

@SuppressWarnings("unchecked")
final public class WebContextUtils {

	private WebContextUtils(){}
	
	public static void attr(HttpServletRequest request, String name, Object value){
		Assert.notNull(request);
		Assert.hasText(name);
		request.setAttribute(name, value);
	}
	
	public static void attr(HttpSession session, String name, Object value){
		Assert.notNull(session);
		Assert.hasText(name);
		session.setAttribute(name, value);
	}
	

	public static <T> T getAttr(HttpServletRequest request, String name){
		return getAttr(request, name, null);
	}

	public static <T> T getAttr(HttpServletRequest request, String name, T def){
		Assert.notNull(request);
		Assert.hasText(name);
		T val = (T) request.getAttribute(name);
		if(val==null)
			val = def;
		return val;
	}

	public static <T> T getAttr(HttpSession session, String name){
		return getAttr(session, name, null);
	}

	public static <T> T getAttr(HttpSession session, String name, T def){
		Assert.notNull(session);
		Assert.hasText(name);
		T val = (T) session.getAttribute(name);
		if(val==null)
			val = def;
		return val;
	}

	public static void remove(HttpSession session, String name){
		Assert.notNull(session);
		Assert.hasText(name);
		session.removeAttribute(name);
	}
	
	public static UserDetail getUserDetail(HttpSession session){
		return getAttr(session, UserDetail.USER_DETAIL_KEY);
	}
	
	public static void setUserDetail(HttpSession session, UserDetail userDetail){
		attr(session, UserDetail.USER_DETAIL_KEY, userDetail);
	}
	
	public static UserDetail removeUserDetail(HttpSession session){
		UserDetail user = getAttr(session, UserDetail.USER_DETAIL_KEY);
		remove(session, UserDetail.USER_DETAIL_KEY);
		return user;
	}
	
	public static void removeCookieToken(HttpServletResponse response){
		ResponseUtils.removeHttpOnlyCookie(response, UserDetail.TOKEN_KEY);
	}
	
	public static void setCookieToken(HttpServletResponse response, String token){
		ResponseUtils.setHttpOnlyCookie(response, UserDetail.TOKEN_KEY, token);
	}
	
	public static String getCookieToken(HttpServletRequest request){
		return ResponseUtils.getCookieValue(request, UserDetail.TOKEN_KEY);
	}
	
}
