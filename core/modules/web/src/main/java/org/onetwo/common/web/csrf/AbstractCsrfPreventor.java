package org.onetwo.common.web.csrf;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

abstract public class AbstractCsrfPreventor implements CsrfPreventor {

	
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
	protected String getTokenFieldName(HttpServletRequest request, HttpServletResponse response){
		String tokenName = request.getParameter(fieldOfTokenFieldName);
		return StringUtils.isBlank(tokenName)?fieldOfTokenFieldName:tokenName;
	}
	/***
	 * 获取已保存的token值
	 * @param tokenFieldName
	 * @param request
	 * @param response
	 * @return
	 */
	abstract protected Token getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response);
	
	abstract protected void cleanStoredTokenValue(Token token, HttpServletRequest request, HttpServletResponse response);
	
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
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.web.csrf.CsrfPreventor#validateToken(java.lang.Object, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void validateToken(Object controller, HttpServletRequest request, HttpServletResponse response){
		if(MEHTOD_GET.equalsIgnoreCase(request.getMethod()))
			return ;
		
		boolean force = isForceValid(controller);
		String tokenFieldName = getTokenFieldName(request, response);
		String reqTokenValue = request.getParameter(tokenFieldName);
		Token token = getStoredTokenValue(tokenFieldName, request, response);
		String storedTokenValue = token.getValue();
		
		try {
			if(StringUtils.isBlank(reqTokenValue) && StringUtils.isBlank(storedTokenValue)){
				if(force)
					handleInvalidToken(token);
				else
					return ;
			}else if(StringUtils.isBlank(reqTokenValue)){
				handleInvalidToken(token);
			}else{
				if(!reqTokenValue.equalsIgnoreCase(storedTokenValue))
					handleInvalidToken(token);
			}
		} finally{
			if(StringUtils.isNotBlank(storedTokenValue))
				cleanStoredTokenValue(token, request, response);
		}
	}
	
	protected void handleInvalidToken(Token token){
		throw new IllegalRequestException();
	}

	public void setFieldOfTokenFieldName(String fieldOfTokenFieldName) {
		this.fieldOfTokenFieldName = fieldOfTokenFieldName;
	}


	public String getFieldOfTokenFieldName() {
		return fieldOfTokenFieldName;
	}
	/* (non-Javadoc)
	 * @see org.onetwo.common.web.csrf.CsrfPreventor#generateToken(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
//	@Override
//	public Token generateToken(HttpServletRequest request, HttpServletResponse response){
//		return generateToken("", request, response);
//	}
	/* (non-Javadoc)
	 * @see org.onetwo.common.web.csrf.CsrfPreventor#generateToken(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Token generateToken(String ptokenFieldName, HttpServletRequest request, HttpServletResponse response){
		String tokenFieldName = StringUtils.isBlank(ptokenFieldName)?LangUtils.generateToken():ptokenFieldName;
		String tokenValue = LangUtils.generateToken(tokenFieldName);
		Token token = new Token(tokenFieldName, tokenValue);
		storeToken(token, request, response);
		return token;
	}
	abstract protected void storeToken(Token token, HttpServletRequest request, HttpServletResponse response);
	
	
	
	protected static class CsrfValidInfo {
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
