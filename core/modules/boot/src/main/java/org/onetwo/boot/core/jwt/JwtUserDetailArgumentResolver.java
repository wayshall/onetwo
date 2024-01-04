package org.onetwo.boot.core.jwt;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.mvc.annotation.BootMvcArgumentResolver;
import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.ext.security.jwt.JwtUserDetail;
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
	private String authHeaderName = JwtUtils.DEFAULT_HEADER_KEY;
	
	public JwtUserDetailArgumentResolver(String authHeaderName) {
		super();
		this.authHeaderName = authHeaderName;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return GenericUserDetail.class.isAssignableFrom(parameter.getParameterType());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//		JwtUserDetail jwtUserDetail = (JwtUserDetail)webRequest.getNativeRequest(HttpServletRequest.class).getAttribute(JwtUtils.AUTH_ATTR_KEY);
		Optional<JwtUserDetail> userOpt = JwtUtils.getOrSetJwtUserDetail(webRequest.getNativeRequest(HttpServletRequest.class), jwtTokenService, authHeaderName);
//		JwtUserDetail jwtUserDetail = userOpt.orElse(null);
		Class<? extends GenericUserDetail<?>> parameterType = (Class<? extends GenericUserDetail<?>>)parameter.getParameterType();
		if(JwtUserDetail.class.isAssignableFrom(parameterType)){
			return userOpt.orElse(null);
		}else if (parameterType.isInterface()){
			throw new BaseException("the type of login user objet can not be defined as a interface!");
		}else {
			if(!userOpt.isPresent()){
				return null;
			}
			GenericUserDetail<?> userDetail = JwtUtils.createUserDetail(userOpt.get(), parameterType);
			return userDetail;
		}
	}

}
