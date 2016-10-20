package org.onetwo.boot.core.web.view;

import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.data.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResultBodyAdvice implements ResponseBodyAdvice<Object>{

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
	}

	@Override
	public Object beforeBodyWrite(
			Object body, MethodParameter returnType,
			MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		if(Result.class.isInstance(body)){
			return body;
		}
		SimpleDataResult<Object> dataResult = SimpleDataResult.success("", body);
		return dataResult;
	}
	
	

}
