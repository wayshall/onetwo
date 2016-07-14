package org.onetwo.boot.plugin.mvc;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.onetwo.boot.plugin.core.Plugin;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.mvc.annotation.PluginContext;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private PluginManager pluginManager;
	
	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
		if(info!=null){
			String contextPath = this.getPluginContextPath(method, handlerType);
			if(StringUtils.isNotBlank(contextPath)){
				info = createPluginRequestMappingInfo(contextPath, method, handlerType).combine(info);
			}
		}
		return info;
	}
	
	private String getPluginContextPath(Method method, Class<?> handlerType){
		PluginContext pluginContext = AnnotationUtils.findAnnotationWithStopClass(handlerType, method, PluginContext.class);
		if(pluginContext!=null){
			return pluginContext.contextPath();
		}
		Optional<Plugin> plugin = pluginManager.findPluginByElementClass(handlerType);
		if(plugin.isPresent()){
			return plugin.get().getPluginMeta().getContextPath();
		}
		return null;
	}

	private RequestMappingInfo createPluginRequestMappingInfo(String rootPath, Method method, Class<?> handlerType) {
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
