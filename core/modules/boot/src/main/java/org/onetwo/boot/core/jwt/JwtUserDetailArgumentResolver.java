package org.onetwo.boot.core.jwt;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.mvc.annotation.BootMvcArgumentResolver;
import org.onetwo.common.web.userdetails.UserDetail;
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
		return UserDetail.class.isAssignableFrom(parameter.getParameterType());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		JwtUserDetail jwtUserDetail = (JwtUserDetail)webRequest.getNativeRequest(HttpServletRequest.class).getAttribute(JwtUtils.AUTH_ATTR_KEY);
		Class<? extends UserDetail> parameterType = (Class<? extends UserDetail>)parameter.getParameterType();
		if(JwtUserDetail.class.isAssignableFrom(parameterType)){
			return jwtUserDetail;
		}else if (parameterType.isInterface()){
			throw new BaseException("the type of login user objet can not be defined as a interface!");
		}else {
			UserDetail userDetail = JwtUtils.createUserDetail(jwtUserDetail, parameterType);
			return userDetail;
		}
	}
	
}
