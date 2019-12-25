package org.onetwo.boot.core.jwt;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorAdapter;
import org.onetwo.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

/**
 * @author wayshall
 * <br/>
 */

public class JwtMvcInterceptor extends MvcInterceptorAdapter {
	private String authHeaderName = JwtUtils.DEFAULT_HEADER_KEY;
	@Autowired
	private JwtTokenService jwtTokenService;
	private boolean canBeAnonymous;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		String token = request.getHeader(authHeaderName);
		if(StringUtils.isBlank(token) && canBeAnonymous){
			return true;
		}
		Optional<JwtUserDetail> userOpt = Optional.empty();
		try {
			userOpt = JwtUtils.getOrSetJwtUserDetail(request, jwtTokenService, authHeaderName);
		}/* catch (ServiceException e) {
			if(e.getExceptionType() instanceof JwtErrors && e.getExceptionType()!=JwtErrors.CM_NOT_LOGIN){
				throw new ServiceException(JwtErrors.CM_NOT_LOGIN, e);
			}
		}*/ catch (Exception e){
			if(e instanceof ServiceException){
				throw (ServiceException)e;
			}
			throw new ServiceException(JwtErrors.CM_LOGIN_UNKNOW_ERR, e);
		}
		
		if(!userOpt.isPresent()){
			throw new ServiceException(JwtErrors.CM_NOT_LOGIN);
		}
		JwtUserDetail userDetail = userOpt.get();
		if (userDetail.isAnonymousLogin() && !canBeAnonymous) {
			throw new ServiceException(JwtErrors.CM_NOT_LOGIN);
		}
		return true;
	}

	public void setAuthHeaderName(String authHeaderName) {
		this.authHeaderName = authHeaderName;
	}

	public void setCanBeAnonymous(boolean canBeAnonymous) {
		this.canBeAnonymous = canBeAnonymous;
	}

	@Override
	public int getOrder() {
		return afterFirst(ORDER_STEP*2);
	}

	@Override
	public String toString() {
		return "JwtMvcInterceptor [authHeaderName=" + authHeaderName
				+ ", canBeAnonymous=" + canBeAnonymous + "]";
	}

}
