package org.onetwo.common.spring.web.mvc;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.mvc.annotation.WebAttribute;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class WebAttributeArgumentResolver implements HandlerMethodArgumentResolver {
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(WebAttribute.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object result = null;
		WebAttribute attrAnnotation = parameter.getParameterAnnotation(WebAttribute.class);
		String attrName = attrAnnotation.value();
		if(StringUtils.isBlank(attrName))
			return result;
		result = webRequest.getAttribute(attrName, RequestAttributes.SCOPE_REQUEST);
		result = (result==null)?webRequest.getAttribute(attrName, RequestAttributes.SCOPE_SESSION):result;
		return result;
	}

}
