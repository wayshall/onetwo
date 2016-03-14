package org.onetwo.boot.core.web.mvc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**********
 * 
 * @author wayshall
 *
 */
public class RequestMappingHandlerMappingListenable implements InitializingBean {

	@Autowired(required=false)
	private List<HandlerMappingListener> listeners;
	
	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	

	/*@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		try {
			Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
			RequestMapping ann = method.getAnnotation(RequestMapping.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
		JFishWebMvcPluginMeta plugin = pluginManager.getJFishPluginMetaOf(handlerType);
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
	}*/

	@Override
    public void afterPropertiesSet() throws Exception {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.requestMappingHandlerMapping.getHandlerMethods();
		handlerMethodsInitialized(handlerMethods);
    }

	protected void handlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		if(listeners!=null){
			for(HandlerMappingListener l : listeners)
				l.onHandlerMethodsInitialized(handlerMethods);
		}
	}

	public void setListeners(List<HandlerMappingListener> listeners) {
		this.listeners = listeners;
	}
	
}
