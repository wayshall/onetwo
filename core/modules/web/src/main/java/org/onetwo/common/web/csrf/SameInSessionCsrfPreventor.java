package org.onetwo.common.web.csrf;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.onetwo.common.web.preventor.PreventRequestInfoManager;
import org.onetwo.common.web.preventor.RequestToken;
import org.onetwo.common.web.preventor.SessionStoreRequestPreventor;
import org.onetwo.common.web.utils.WebContextUtils;

/***
 * same session is same fieldName and value
 * @author wayshall
 *
 */
public class SameInSessionCsrfPreventor extends SessionStoreRequestPreventor {

	public static final String DEFAULT_CSRF_TOKEN_FIELD = "_jfish_token";

	private PreventRequestInfoManager csrfAnnotationManager;
	
	public SameInSessionCsrfPreventor(PreventRequestInfoManager csrfAnnotationManager) {
		super(DEFAULT_CSRF_TOKEN_FIELD);
		this.csrfAnnotationManager = csrfAnnotationManager;
	}
	
	public PreventRequestInfoManager getCsrfAnnotationManager() {
		return csrfAnnotationManager;
	}

	public boolean isValidateToken(Method controller, HttpServletRequest request){
		return csrfAnnotationManager.getRequestPreventInfo(controller, request).isCsrfValidate();
	}
	
	@Override
	public RequestToken generateToken(HttpServletRequest request, HttpServletResponse response){
		RequestToken token = WebContextUtils.getAttr(request.getSession(), getTokenFieldName());
		if(token!=null)
			return token;
		
		synchronized (request.getSession()) {
			token = WebContextUtils.getAttr(request.getSession(), getTokenFieldName());
			if(token==null)
				token = super.generateToken(request, response);
		}
		return token;
	}
	
//	protected void handleInvalidToken(Token token){
//		throw new IllegalRequestException();
//	}
	
	@Override
	protected RequestToken getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response){
		return WebContextUtils.getAttr(request.getSession(), tokenFieldName);
	}
	@Override
	protected void cleanStoredTokenValue(boolean invalid, RequestToken token, HttpServletRequest request, HttpServletResponse response){
		if(invalid && token!=null)
			WebContextUtils.remove(request.getSession(), token.getFieldName());
//		super.cleanStoredTokenValue(token, request, response);
	}

	@Override
	protected void storeToken(RequestToken token, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.attr(request.getSession(), token.getFieldName(), token);
	}
}
