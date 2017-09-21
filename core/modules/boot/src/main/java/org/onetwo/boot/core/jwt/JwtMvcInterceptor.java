package org.onetwo.boot.core.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorAdapter;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.web.userdetails.UserDetail;
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

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		Object data = request.getAttribute(JwtUtils.AUTH_ATTR_KEY);
		if(data instanceof UserDetail){
			return true;
		}
		String token = request.getHeader(authHeaderName);

		if(logger.isDebugEnabled()){
			logger.debug("load context user token : {}", token);
		}
		
		if(StringUtils.isBlank(token)){
			throw new ServiceException(JwtErrors.CM_NOT_LOGIN);
		}
		
		UserDetail userDetail = jwtTokenService.createUserDetail(token);
		request.setAttribute(JwtUtils.AUTH_ATTR_KEY, userDetail);
		return true;
	}

}
