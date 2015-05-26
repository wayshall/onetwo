package org.onetwo.common.spring.web.mvc.args;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.web.mvc.annotation.AsyncProcessor;
import org.onetwo.common.web.asyn.AsyncWebProcessor;
import org.onetwo.common.web.asyn.AsyncWebProcessorBuilder;
import org.slf4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AsyncWebProcessorArgumentResolver implements HandlerMethodArgumentResolver {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		//parameter.hasParameterAnnotation(AsyncProcessor.class) ||
		return parameter.hasParameterAnnotation(AsyncProcessor.class) && AsyncWebProcessor.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public AsyncWebProcessor<?> resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		AsyncWebProcessor<?> processor = null;
//		Class<?> processorClass = parameter.getParameterType();
		AsyncProcessor p = parameter.getParameterAnnotation(AsyncProcessor.class);
		AsyncWebProcessorBuilder builder = AsyncWebProcessorBuilder.newBuilder(webRequest.getNativeRequest(HttpServletRequest.class), webRequest.getNativeResponse(HttpServletResponse.class));
		processor = builder.flushInSecond(p.flushInSecond())
				.contentType(p.contentType())
				.progressProcessor(p.progressProcessor())
//				.messageTunnel((AsyncMessageTunnel<?>)ReflectUtils.newInstance(p.messageTunnel()))
				.build();
		return processor;
	}

}
