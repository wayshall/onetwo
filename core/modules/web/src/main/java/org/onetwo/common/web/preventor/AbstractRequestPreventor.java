package org.onetwo.common.web.preventor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.TagUtils;

abstract public class AbstractRequestPreventor implements RequestPreventor {

	
//	public static final String MEHTOD_GET = "get";
	
	protected final String tokenFieldName;// = DEFAULT_CSRF_TOKEN_FIELD;
//	protected MDEncrypt encrypt = MDFactory.MD5;
	protected TokenValueGenerator tokenValueGenerator = new Md5TokenValueGenerator();
	
	
	public AbstractRequestPreventor(String tokenFieldName) {
		super();
		this.tokenFieldName = tokenFieldName;
	}
//	protected boolean force;
	/***
	 * 获取token域的名称
	 * @param request
	 * @param response
	 * @return
	 */
	public String getTokenFieldName(){
		/*String tokenName = request.getParameter(tokenFieldName);
		return StringUtils.isBlank(tokenName)?tokenFieldName:tokenName;*/
		return tokenFieldName;
	}
	/***
	 * 获取已保存的token值
	 * @param tokenFieldName
	 * @param request
	 * @param response
	 * @return
	 */
	abstract protected RequestToken getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response);
	
	abstract protected void cleanStoredTokenValue(boolean invalid, RequestToken token, HttpServletRequest request, HttpServletResponse response);
	
	public boolean isValidateToken(Method controller, HttpServletRequest request){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.web.csrf.CsrfPreventor#validateToken(java.lang.Object, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void validateToken(Method controller, HttpServletRequest request, HttpServletResponse response){
		/*if(MEHTOD_GET.equalsIgnoreCase(request.getMethod()))
			return ;*/
		
		if(!isValidateToken(controller, request))
			return ;
		
		String tokenFieldName = getTokenFieldName();
		String reqTokenValue = request.getParameter(tokenFieldName);
		
		HttpSession session = request.getSession();
		synchronized (session) {
			RequestToken token = getStoredTokenValue(tokenFieldName, request, response);
//			String storedTokenValue = token.getValue();
			
			try {
				if(token==null){
					handleInvalidToken(token, request, response);
				}else if(StringUtils.isBlank(reqTokenValue)){
					handleInvalidToken(token, request, response);
				}else{
					/*if(!reqTokenValue.equalsIgnoreCase(token.getValue()))
						handleInvalidToken(token, request, response);*/
//					if(!encrypt.checkEncrypt(token.getValue(), reqTokenValue))
					if(!getTokenValueGenerator().validateToken(token, reqTokenValue))
						handleInvalidToken(token, request, response);
				}
			} finally{
				if(token!=null)
					cleanStoredTokenValue(false, token, request, response);
			}
		}
	}
	
	protected void handleInvalidToken(RequestToken token, HttpServletRequest request, HttpServletResponse response){
		cleanStoredTokenValue(true, token, request, response);
		throw new IllegalRequestException();
	}

	/*public void setFieldOfTokenFieldName(String fieldOfTokenFieldName) {
		this.fieldOfTokenFieldName = fieldOfTokenFieldName;
	}


	public String getFieldOfTokenFieldName() {
		return fieldOfTokenFieldName;
	}*/
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
	public RequestToken generateToken(HttpServletRequest request, HttpServletResponse response){
		String tokenValue = LangUtils.generateToken(getTokenFieldName());
		RequestToken token = new RequestToken(getTokenFieldName(), tokenValue);
		storeToken(token, request, response);
		return token;
	}


	public TokenValueGenerator getTokenValueGenerator(){
		return tokenValueGenerator;
	}

	/*protected String generateToken(String tokenFieldName) {
		String s = encrypt.encryptWithSalt(tokenFieldName + System.currentTimeMillis() + LangUtils.getRadomString(6));
		return s;
	}*/
	public String processSafeUrl(String url, HttpServletRequest request, HttpServletResponse response){
		String safeUrl = url;
		RequestToken token = generateToken(request, response);
		safeUrl = TagUtils.appendParam(safeUrl, token.getFieldName(), token.getGeneratedValue(getTokenValueGenerator()));
		return safeUrl;
	}
	
	abstract protected void storeToken(RequestToken token, HttpServletRequest request, HttpServletResponse response);
	
	
	
	/*public static class RequestValidateInfo {
		public static final RequestValidateInfo TRUE = new RequestValidateInfo(true);
		public static final RequestValidateInfo FALSE = new RequestValidateInfo(false);
		
		private final boolean validateRequest;

		public RequestValidateInfo(boolean valid) {
			super();
			this.validateRequest = valid;
		}

		public boolean isValidateRequest() {
			return validateRequest;
		}
		
	}*/
}
