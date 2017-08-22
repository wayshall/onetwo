package org.onetwo.boot.core.jwt;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.spring.mvc.annotation.BootMvcArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author wayshall
 * <br/>
 */
@BootMvcArgumentResolver
public class JwtUserDetailArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private JwtTokenService jwtTokenService;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return JwtUserDetail.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		JwtUserDetail userDetail = (JwtUserDetail)webRequest.getNativeRequest(HttpServletRequest.class).getAttribute(JwtUtils.AUTH_ATTR_KEY);
		return userDetail;
	}
	
}
