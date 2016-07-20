package org.onetwo.ext.security.mvc.args;

import org.onetwo.common.spring.web.mvc.annotation.BootMvcArgs;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@BootMvcArgs
public class SecurityArgumentResolver implements HandlerMethodArgumentResolver {
	
//	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Authentication.class.isAssignableFrom(parameter.getParameterType()) || 
				User.class.isAssignableFrom(parameter.getParameterType()) || 
				UserDetail.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object result = null;
		if(Authentication.class.isAssignableFrom(parameter.getParameterType())){
			result = SecurityContextHolder.getContext().getAuthentication();
		}else if(User.class.isAssignableFrom(parameter.getParameterType())){
			result = SecurityUtils.getCurrentLoginUser();
		}else if(UserDetail.class.isAssignableFrom(parameter.getParameterType())){
			result = (UserDetail)SecurityUtils.getCurrentLoginUser();
		}
		return result;
	}

}
