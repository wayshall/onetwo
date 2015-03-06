package org.onetwo.common.spring.web.authentic;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.web.s2.security.AuthenticUtils;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.onetwo.plugins.security.common.SsoConfig;
import org.springframework.web.method.HandlerMethod;

public class SsoSpringSecurityInterceptor extends SpringSecurityInterceptor {

	@Resource
	private SsoConfig ssoConfig;
	
	public SsoSpringSecurityInterceptor(){
	}
	
	protected SecurityTarget createSecurityTarget(HttpServletRequest request, HttpServletResponse response, Object handler){
		SecurityTarget target = null;
		if(handler instanceof HandlerMethod){
			if(ssoConfig.isServerSide()){
				target = new SpringSsoSecurityTarget(getSessionStorer(), request, response, (HandlerMethod)handler);
			}else{
				target = new SpringSecurityTarget(getSessionStorer(), request, response, (HandlerMethod)handler);
			}
			AuthenticUtils.setIntoRequest(request, target);
		}
		return target;
	}

}
