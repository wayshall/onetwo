package org.onetwo.common.web.s2.security;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.web.utils.WebContextUtils;

final public class AuthenticUtils {

	private AuthenticUtils(){
	}
	
	public static void setIntoRequest(HttpServletRequest request, AuthenticationContext context){
		WebContextUtils.attr(request, AuthenticationContext.AUTHENTICATION_CONTEXT_KEY, context);
	}
	
	public static AuthenticationContext getContextFromRequest(HttpServletRequest request){
		AuthenticationContext context = WebContextUtils.getAttr(request, AuthenticationContext.AUTHENTICATION_CONTEXT_KEY);
		return context;
	}
}
