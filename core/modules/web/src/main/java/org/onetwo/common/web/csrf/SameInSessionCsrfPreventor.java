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
	public Token generateToken(String ptokenFieldName, HttpServletRequest request, HttpServletResponse response){
		Token token = WebContextUtils.getAttr(request.getSession(), fieldOfTokenFieldName);
		if(token!=null)
			return token;
		
		token = super.generateToken(ptokenFieldName, request, response);
		return token;
	}
	
//	protected void handleInvalidToken(Token token){
//		throw new IllegalRequestException();
//	}
	
	@Override
	protected Token getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response){
		return WebContextUtils.getAttr(request.getSession(), fieldOfTokenFieldName);
	}
	@Override
	protected void cleanStoredTokenValue(Token token, HttpServletRequest request, HttpServletResponse response){
//		WebContextUtils.remove(request.getSession(), fieldOfTokenFieldName);
	}

	@Override
	protected void storeToken(Token token, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.attr(request.getSession(), fieldOfTokenFieldName, token);
	}
}
