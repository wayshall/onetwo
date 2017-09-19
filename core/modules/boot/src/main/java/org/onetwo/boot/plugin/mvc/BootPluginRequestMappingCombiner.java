package org.onetwo.boot.plugin.mvc;

import java.lang.reflect.Method;
import java.util.Optional;

import org.onetwo.boot.core.web.mvc.ExtRequestMappingHandlerMapping.RequestMappingCombiner;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.core.WebPlugin;
import org.onetwo.boot.plugin.mvc.annotation.WebPluginContext;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/**********
 * 
 * @author wayshall
 *
 */
public class BootPluginRequestMappingCombiner implements RequestMappingCombiner {

	@Autowired
	private PluginManager pluginManager;
	@Autowired
	private ApplicationContext applicationContext;
	
	
	@Override
	public RequestMappingInfo combine(Method method, Class<?> handlerType, RequestMappingInfo info) {
		if(info!=null){
			String contextPath = this.getPluginContextPath(method, handlerType);
			if(StringUtils.isNotBlank(contextPath)){
				info = createPluginRequestMappingInfo(contextPath, method, handlerType).combine(info);
			}
		}
		return info;
	}
	
	private String getPluginContextPath(Method method, Class<?> handlerType){
		WebPluginContext pluginContext = AnnotationUtils.findAnnotationWithStopClass(handlerType, method, WebPluginContext.class);
		if(pluginContext!=null){
			return resolvePluginContextPath(pluginContext.contextPath());
		}
		Optional<WebPlugin> plugin = pluginManager.findPluginByElementClass(handlerType);
		if(plugin.isPresent()){
			return resolvePluginContextPath(plugin.get().getContextPath());
		}
		return null;
	}

	private RequestMappingInfo createPluginRequestMappingInfo(String rootPath, Method method, Class<?> handlerType) {
		return RequestMappingCombiner.createRequestMappingInfo(rootPath, method, handlerType);
	}
	
	private String resolvePluginContextPath(final String pluginContextPath){
		String path = SpringUtils.resolvePlaceholders(applicationContext, pluginContextPath);
		return path;
	}

}
