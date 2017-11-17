package org.onetwo.ext.security.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Builder;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.web.utils.RequestUtils;

/**
 * @author wayshall
 * <br/>
 */
public class CookieStorer {

	private String cookiePath;
	private String cookieDomain;

	@Builder
	public CookieStorer(String cookiePath, String cookieDomain) {
		super();
		this.cookiePath = cookiePath;
		this.cookieDomain = cookieDomain;
	}
	
	public String get(HttpServletRequest request, String cookieName){
		return RequestUtils.getCookieValue(request, cookieName);
	}

	public void save(HttpServletRequest request, HttpServletResponse response, String cookieName, String value) {
        Cookie sessionCookie = new Cookie(cookieName, value);
        configCookie(request, sessionCookie);
        response.addCookie(sessionCookie);
    }
	

	public void clear(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie sessionCookie = new Cookie(cookieName, null);
        configCookie(request, sessionCookie);
        sessionCookie.setMaxAge(0);
		response.addCookie(sessionCookie);
    }
	
	private void configCookie(HttpServletRequest request, Cookie sessionCookie){
        sessionCookie.setSecure(request.isSecure());
        String cookiePath = cookiePath(request);
        sessionCookie.setPath(cookiePath);
        String domain = cookieDomain(request);
        if(StringUtils.isNotBlank(domain)){
        	sessionCookie.setDomain(domain);
        }
	}
	
	private String cookiePath(HttpServletRequest request) {
	   	if(StringUtils.isNotBlank(cookiePath)){
	   		return cookiePath;
	   	}
	   	return request.getContextPath() + "/";
   }

   private String cookieDomain(HttpServletRequest request) {
	   	if(StringUtils.isNotBlank(cookieDomain)){
	   		return cookieDomain;
	   	}
	   	return null;
   }
}
