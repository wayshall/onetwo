package org.onetwo.common.spring.web.mvc;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginMeta;
import org.onetwo.common.fish.plugin.anno.PluginControllerConf;
import org.onetwo.common.spring.plugin.PluginInfo;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.OrderComparator;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**********
 * 
 * @author wayshall
 *
 */
public class JFishRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	private JFishPluginManager pluginManager;
	
	private List<HandlerMappingListener> listeners;

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		/*try {
			Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
			RequestMapping ann = method.getAnnotation(RequestMapping.class);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
		JFishPluginMeta plugin = pluginManager.getJFishPluginMetaOf(handlerType);
		if(info!=null && plugin!=null){
			info = createPluginRequestMappingInfo(plugin.getPluginInfo(), method, handlerType).combine(info);
		}
		return info;
	}

	private RequestMappingInfo createPluginRequestMappingInfo(PluginInfo plugin, Method method, Class<?> handlerType) {
		String rootPath = plugin.getContextPath();
		PluginControllerConf conf = handlerType.getAnnotation(PluginControllerConf.class);
		if(conf!=null){
			if(StringUtils.isBlank(conf.contextPath()) || conf.contextPath().equals("/")){
				rootPath = "";
			}else{
				rootPath = conf.contextPath(); 
			}
		}
		return new RequestMappingInfo(
				new PatternsRequestCondition(rootPath),
				null,
				null,
				null,
				null,
				null, 
				null);
	}

	public void setPluginManager(JFishPluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}
	

	@Override
	protected void handlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		super.handlerMethodsInitialized(handlerMethods);
		if(listeners!=null){
			for(HandlerMappingListener l : listeners)
				l.onHandlerMethodsInitialized(handlerMethods);
		}
	}

	protected void detectMappedInterceptors(List<MappedInterceptor> mappedInterceptors) {
		super.detectMappedInterceptors(mappedInterceptors);
		CUtils.stripNull(mappedInterceptors);
		Collections.sort(mappedInterceptors, new Comparator<MappedInterceptor>() {

			@Override
			public int compare(MappedInterceptor o1, MappedInterceptor o2) {
				return OrderComparator.INSTANCE.compare(o1.getInterceptor(), o2.getInterceptor());
			}
			
		});
	}

	public void setListeners(List<HandlerMappingListener> listeners) {
		this.listeners = listeners;
	}
	
}
