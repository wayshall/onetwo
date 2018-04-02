package org.onetwo.boot.core.web.api;

import java.lang.reflect.Method;
import java.util.Optional;

import org.onetwo.boot.core.web.mvc.ExtRequestMappingHandlerMapping.RequestMappingCombiner;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/**
 * 优先级最低，加在最前面
 * @author wayshall
 * <br/>
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class WebApiRequestMappingCombiner implements RequestMappingCombiner {
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public RequestMappingInfo combine(Method method, Class<?> handlerType, RequestMappingInfo info) {
		if(info==null){
			return info;
		}
		Optional<AnnotationAttributes> webApiOpt = findWebApiAttrs(method, handlerType);
		if(!webApiOpt.isPresent()){
			return info;
		}
		AnnotationAttributes webApi = webApiOpt.get();
		String prefixPath = webApi.getString("prefixPath");
		if(StringUtils.isBlank(prefixPath)){
			return info;
		}
		prefixPath = SpringUtils.resolvePlaceholders(applicationContext, prefixPath);
		if(StringUtils.isBlank(prefixPath)){
			return info;
		}
		RequestMappingInfo combinerInfo = RequestMappingCombiner.createRequestMappingInfo(prefixPath, method, handlerType, info)
																.combine(info);
		return combinerInfo;
	}
	

	
	private Optional<AnnotationAttributes> findWebApiAttrs(Method method, Class<?> handlerType){
		AnnotationAttributes attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(method, WebApi.class);
		if(attrs==null){
			attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(handlerType, WebApi.class);
		}
		return Optional.ofNullable(attrs);
	}

}
