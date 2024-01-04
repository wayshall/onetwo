package org.onetwo.common.web.preventor;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RequestPreventor {

	public String MEHTOD_GET = "get";
//	public static final String DEFAULT_CSRF_TOKEN_FIELD = "_jfish_token";
	
//	public CsrfAnnotationManager getCsrfAnnotationManager();
//	public boolean isValidCsrf(Object controller, HttpServletRequest request);
	
//	public String getFieldOfTokenFieldName();
	public String getTokenFieldName();
	public TokenValueGenerator getTokenValueGenerator();
	
	public abstract void validateToken(Method controller,
			HttpServletRequest request, HttpServletResponse response);

//	public abstract Token generateToken(HttpServletRequest request, HttpServletResponse response);

	/****
	 * 生产token
	 * @param ptokenFieldName
	 * @param request
	 * @param response
	 * @return
	 */
	public abstract RequestToken generateToken(HttpServletRequest request, HttpServletResponse response);
	/***
	 * for t:url
	 * @param url
	 * @param request
	 * @param response
	 * @return
	 */
	public String processSafeUrl(String url, HttpServletRequest request, HttpServletResponse response);

}