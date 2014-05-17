package org.onetwo.common.web.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CsrfPreventor {

	public String MEHTOD_GET = "get";
	public static final String DEFAULT_CSRF_TOKEN_FIELD = "_jfish_token";
	
//	public CsrfAnnotationManager getCsrfAnnotationManager();
//	public boolean isValidCsrf(Object controller, HttpServletRequest request);
	
	public String getFieldOfTokenFieldName();

	public abstract void validateToken(Object controller,
			HttpServletRequest request, HttpServletResponse response);

//	public abstract Token generateToken(HttpServletRequest request, HttpServletResponse response);

	/****
	 * 生产token
	 * @param ptokenFieldName
	 * @param request
	 * @param response
	 * @return
	 */
	public abstract CsrfToken generateToken(HttpServletRequest request, HttpServletResponse response);
	/***
	 * for t:url
	 * @param url
	 * @param request
	 * @param response
	 * @return
	 */
	public String processSafeUrl(String url, HttpServletRequest request, HttpServletResponse response);

}