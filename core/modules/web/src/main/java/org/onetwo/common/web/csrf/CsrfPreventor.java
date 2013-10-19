package org.onetwo.common.web.csrf;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

abstract public class CsrfPreventor {

	public static final CsrfPreventor SESSION = new SessionStoreCsrfPreventor();
	public static final CsrfPreventor COOKIE = new SessionStoreCsrfPreventor();

	public static final String DEFAULT_CSRF_TOKEN_FIELD = "_jfish_token";
	public static final String MEHTOD_GET = "get";
	
	public static final CsrfValidInfo DEFAULT_CSRF = new CsrfValidInfo(true);
	
	protected String fieldOfTokenFieldName = DEFAULT_CSRF_TOKEN_FIELD;
//	protected boolean force;
	private ConcurrentHashMap<String, CsrfValidInfo> caches = new ConcurrentHashMap<String, CsrfValidInfo>();
	/***
	 * 获取token域的名称
	 * @param request
	 * @param response
	 * @return
	 */
	abstract protected String getTokenFieldName(HttpServletRequest request, HttpServletResponse response);
	/***
	 * 获取已保存的token值
	 * @param tokenFieldName
	 * @param request
	 * @param response
	 * @return
	 */
	abstract protected String getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response);
	
	abstract protected void cleanStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response);
	
	protected boolean isForceValid(Object controller){
		Method method = (Method) controller;
		String key = method.toGenericString();
		CsrfValidInfo csrfInfo = this.caches.get(key);
		if(csrfInfo!=null)
			return csrfInfo.isValid();
		
		CsrfValid csrf = AnnotationUtils.findMethodAnnotationWithStopClass(method, CsrfValid.class);
		if(csrf==null)
			csrfInfo = DEFAULT_CSRF;
		else
			csrfInfo = new CsrfValidInfo(csrf.value());
		this.caches.put(key, csrfInfo);
		return csrfInfo.isValid();
	}
	
	public void validateToken(Object controller, HttpServletRequest request, HttpServletResponse response){
		if(MEHTOD_GET.equalsIgnoreCase(request.getMethod()))
			return ;
		
		boolean force = isForceValid(controller);
		String tokenFieldName = getTokenFieldName(request, response);
		String reqTokenValue = request.getParameter(tokenFieldName);
		String storedTokenValue = getStoredTokenValue(tokenFieldName, request, response);
		
		try {
			if(StringUtils.isBlank(reqTokenValue) && StringUtils.isBlank(storedTokenValue)){
				if(force)
					throw new IllegalRequestException();
				else
					return ;
			}else if(StringUtils.isBlank(reqTokenValue)){
				throw new IllegalRequestException();
			}else{
				if(!reqTokenValue.equalsIgnoreCase(storedTokenValue))
					throw new IllegalRequestException();
			}
		} finally{
			if(StringUtils.isNotBlank(storedTokenValue))
				cleanStoredTokenValue(tokenFieldName, request, response);
		}
	}

	public void setFieldOfTokenFieldName(String fieldOfTokenFieldName) {
		this.fieldOfTokenFieldName = fieldOfTokenFieldName;
	}


	public Token generateToken(HttpServletRequest request, HttpServletResponse response){
		return generateToken("", request, response);
	}
	/****
	 * 生产token
	 * @param ptokenFieldName
	 * @param request
	 * @param response
	 * @return
	 */
	public Token generateToken(String ptokenFieldName, HttpServletRequest request, HttpServletResponse response){
		String tokenFieldName = StringUtils.isBlank(ptokenFieldName)?LangUtils.generateToken():ptokenFieldName;
		String tokenValue = LangUtils.generateToken(tokenFieldName);
		Token token = new Token(fieldOfTokenFieldName, tokenFieldName, tokenValue);
		storeToken(token, request, response);
		return token;
	}
	abstract protected void storeToken(Token token, HttpServletRequest request, HttpServletResponse response);
	
	public static class Token {
		public String fieldOfFieldName;
		public String fieldName;
		public String value;
		private Token(String fieldOfFieldName, String fieldName, String value) {
			super();
			this.fieldOfFieldName = fieldOfFieldName;
			this.fieldName = fieldName;
			this.value = value;
		}
		
	}
	
	public static class CsrfValidInfo {
		private boolean valid;

		private CsrfValidInfo(boolean valid) {
			super();
			this.valid = valid;
		}

		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}
		
	}
}
