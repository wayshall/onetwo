package org.onetwo.common.web.s2.security;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.web.utils.WebContextUtils;

final public class AuthenticUtils {

	public static final String SECURITY_TARGET_KEY = "__security_target_key__";
	
	private AuthenticUtils(){
	}
	
	public static void setIntoRequest(HttpServletRequest request, SecurityTarget target){
		WebContextUtils.attr(request, SECURITY_TARGET_KEY, target);
	}
	
	public static SecurityTarget getSecurityTargetFromRequest(HttpServletRequest request){
		SecurityTarget target = WebContextUtils.getAttr(request, SECURITY_TARGET_KEY);
		return target;
	}
	
	public static void setIntoRequest(HttpServletRequest request, AuthenticationContext context){
		WebContextUtils.attr(request, AuthenticationContext.AUTHENTICATION_CONTEXT_KEY, context);
	}
	
	public static AuthenticationContext getContextFromRequest(HttpServletRequest request){
		AuthenticationContext context = WebContextUtils.getAttr(request, AuthenticationContext.AUTHENTICATION_CONTEXT_KEY);
		return context;
	}
}
