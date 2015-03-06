package org.onetwo.common.spring.web.authentic;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.web.s2.security.AuthenticUtils;
import org.onetwo.common.web.s2.security.AuthenticationContext;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.slf4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SpringTargetArgumentResolver implements HandlerMethodArgumentResolver {
	
	protected final Logger logger = MyLoggerFactory.getLogger(SpringTargetArgumentResolver.class);

	public SpringTargetArgumentResolver(){
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return SecurityTarget.class.isAssignableFrom(parameter.getParameterType()) || AuthenticationContext.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//		SecurityTarget result = AuthenticUtils.getSecurityTargetFromRequest((HttpServletRequest)webRequest.getNativeRequest());
		Object result = null;
		if(AuthenticationContext.class.isAssignableFrom(parameter.getParameterType())){
			result = AuthenticUtils.getContextFromRequest((HttpServletRequest)webRequest.getNativeRequest());
		}else{
			result = AuthenticUtils.getSecurityTargetFromRequest((HttpServletRequest)webRequest.getNativeRequest());
			if(result==null){
				AuthenticationContext context = AuthenticUtils.getContextFromRequest((HttpServletRequest)webRequest.getNativeRequest());
				result = context!=null?context.getTarget():null;
			}
		}
		return result;
	}

}
