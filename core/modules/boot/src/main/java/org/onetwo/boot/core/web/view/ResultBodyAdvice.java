package org.onetwo.boot.core.web.view;

import java.util.Optional;

import org.onetwo.boot.core.web.utils.BootWebHelper;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResultBodyAdvice implements ResponseBodyAdvice<Object>{

	protected transient final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired(required=false)
	private XResponseViewManager xresponseViewManager;
	
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
//		return dataResultWrapper.wrapResult(body);
		if(xresponseViewManager!=null){
			/*if(logger.isInfoEnabled()){
				logger.info("wrap body to data result");
			}*/
			Optional<Object> bodyOpt = this.xresponseViewManager.getResponseViewByPredicate(body);
			if(bodyOpt.isPresent()){
				return bodyOpt.get();
			}
			if(request instanceof ServletServerHttpRequest){
				ServletServerHttpRequest sshr = (ServletServerHttpRequest) request;
				BootWebHelper helper = BootWebUtils.webHelper(sshr.getServletRequest());
				body = xresponseViewManager.getHandlerMethodResponseView(helper.getControllerHandler(), body, false);
			}else{
				BootWebHelper helper = BootWebUtils.webHelper();
				if(helper!=null){
					body = xresponseViewManager.getHandlerMethodResponseView(helper.getControllerHandler(), body, false);
				}else{
					logger.info("BootWebHelper not found, can not wrap data result");
				}
			}
		}
		return body;
	}
	
	

}
