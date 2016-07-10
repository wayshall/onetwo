package org.onetwo.boot.core.web.mvc;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.onetwo.boot.plugin.web.annotation.PluginContext;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.utils.CUtils;
import org.springframework.core.OrderComparator;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**********
 * 
 * @author wayshall
 *
 */
public class BootPluginRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
		PluginContext pluginContext = this.getPluginContext(method, handlerType);
		if(info!=null && pluginContext!=null){
			info = createPluginRequestMappingInfo(pluginContext, method, handlerType).combine(info);
		}
		return info;
	}
	
	private PluginContext getPluginContext(Method method, Class<?> handlerType){
		PluginContext pluginContext = AnnotationUtils.findAnnotationWithStopClass(handlerType, method, PluginContext.class);
		return pluginContext;
	}

	private RequestMappingInfo createPluginRequestMappingInfo(PluginContext pluginContext, Method method, Class<?> handlerType) {
		String rootPath = pluginContext.contextPath();
		return new RequestMappingInfo(
				new PatternsRequestCondition(rootPath),
				null,
				null,
				null,
				null,
				null, 
				null);
	}


	@Override
	protected void handlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		super.handlerMethodsInitialized(handlerMethods);
	}

	protected void detectMappedInterceptors(List<HandlerInterceptor> mappedInterceptors) {
		super.detectMappedInterceptors(mappedInterceptors);
		CUtils.stripNull(mappedInterceptors);
		Collections.sort(mappedInterceptors, new Comparator<HandlerInterceptor>() {

			@Override
			public int compare(HandlerInterceptor o1, HandlerInterceptor o2) {
				return OrderComparator.INSTANCE.compare(o1, o2);
			}
			
		});
	}

}
