package org.onetwo.common.web.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

abstract public class AbstractCsrfPreventor implements CsrfPreventor {

	
	public static final String MEHTOD_GET = "get";
	
	protected String fieldOfTokenFieldName = DEFAULT_CSRF_TOKEN_FIELD;
//	protected boolean force;
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
	abstract protected CsrfToken getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response);
	
	abstract protected void cleanStoredTokenValue(boolean invalid, CsrfToken token, HttpServletRequest request, HttpServletResponse response);
	
	protected boolean isForceValid(Object controller){
		return true;
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
		CsrfToken token = getStoredTokenValue(tokenFieldName, request, response);
//		String storedTokenValue = token.getValue();
		
		try {
			if(token==null){
				if(force)
					handleInvalidToken(token, request, response);
				else
					return ;
			}else if(StringUtils.isBlank(reqTokenValue)){
				handleInvalidToken(token, request, response);
			}else{
				if(!reqTokenValue.equalsIgnoreCase(token.getValue()))
					handleInvalidToken(token, request, response);
			}
		} finally{
			if(token!=null)
				cleanStoredTokenValue(false, token, request, response);
		}
	}
	
	protected void handleInvalidToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response){
		cleanStoredTokenValue(true, token, request, response);
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
	public CsrfToken generateToken(HttpServletRequest request, HttpServletResponse response){
		String tokenFieldName = LangUtils.generateToken();
		String tokenValue = LangUtils.generateToken(tokenFieldName);
		CsrfToken token = new CsrfToken(tokenFieldName, tokenValue);
		storeToken(token, request, response);
		return token;
	}
	

	public String processSafeUrl(String url, HttpServletRequest request, HttpServletResponse response){
		String safeUrl = url;
		CsrfToken token = generateToken(request, response);
		String param = token.getFieldName()+"="+token.getValue()+"&"+fieldOfTokenFieldName+"="+token.getFieldName();
		if(safeUrl.indexOf('?')!=-1){
			safeUrl += "&" + param;
		}else{
			safeUrl += "?" + param;
		}
		return safeUrl;
	}
	
	abstract protected void storeToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response);
	
	
	
	protected static class CsrfValidInfo {
		private boolean valid;

		protected CsrfValidInfo(boolean valid) {
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
