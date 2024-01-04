package org.onetwo.boot.module.security.cookies;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 主要修复有时会出现多个同名(sid)但path不同的cookies，导致程序无法正确读取session的情况
 * @author weishao zeng
 * <br/>
 */
public class FilteredCookieSerializer extends DefaultCookieSerializer {

	private String cookiePath;

	@Override
	public List<String> readCookieValues(HttpServletRequest request) {
		return super.readCookieValues(request);
	}
	

	public String getRequestCookiePath(HttpServletRequest request) {
		if (this.cookiePath == null) {
			return request.getContextPath() + "/";
		}
		return this.cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
		super.setCookiePath(cookiePath);
	}
	
	public class FilterCookieHttpServletRequest extends HttpServletRequestWrapper {

		public FilterCookieHttpServletRequest(HttpServletRequest request) {
			super(request);
		}
		
	    @Override
	    public Cookie[] getCookies() {
	    	Cookie[] cookies = super.getCookies();
	    	String cookiePath = getRequestCookiePath((HttpServletRequest)getRequest());
	    	List<Cookie> filteredCookies = Stream.of(cookies).filter(ck -> {
	    		return cookiePath.equals(ck.getPath());
	    	}).collect(Collectors.toList());
	    	return filteredCookies.toArray(new Cookie[0]);
	    }


	}
}
