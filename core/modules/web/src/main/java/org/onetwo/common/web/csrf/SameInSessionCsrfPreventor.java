package org.onetwo.common.web.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.web.utils.WebContextUtils;

/***
 * same session is same fieldName and value
 * @author wayshall
 *
 */
public class SameInSessionCsrfPreventor extends SessionStoreCsrfPreventor {

	
	@Override
	public CsrfToken generateToken(HttpServletRequest request, HttpServletResponse response){
		CsrfToken token = WebContextUtils.getAttr(request.getSession(), getTokenFieldName());
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
	protected CsrfToken getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response){
		return WebContextUtils.getAttr(request.getSession(), tokenFieldName);
	}
	@Override
	protected void cleanStoredTokenValue(boolean invalid, CsrfToken token, HttpServletRequest request, HttpServletResponse response){
		if(invalid)
			WebContextUtils.remove(request.getSession(), token.getFieldName());
//		super.cleanStoredTokenValue(token, request, response);
	}

	@Override
	protected void storeToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.attr(request.getSession(), token.getFieldName(), token);
	}
}
