package org.onetwo.boot.core.web.socket;

import java.util.Map;

import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.LangUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * @author wayshall
 * <br/>
 */
public class SocketBeanConfigurator extends Configurator {

	@Override
	public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
		T endpoint = null;
		ApplicationContext appContext = Springs.getInstance().getAppContext();
		Component ann = AnnotationUtils.findAnnotation(endpointClass, Component.class);
		if (ann != null && appContext.containsBean(ann.value())) {
			endpoint = appContext.getBean(ann.value(), endpointClass);
			return endpoint;
		}
		
		Map<String, T> socketEndpointMap = Springs.getInstance().getBeansMap(endpointClass);
		if(LangUtils.isEmpty(socketEndpointMap)){
			endpoint = ReflectUtils.newInstance(endpointClass);
			SpringUtils.injectAndInitialize(appContext, ReflectUtils.newInstance(endpointClass));
			return endpoint;
		}
		if(socketEndpointMap.size()==1){
			endpoint = LangUtils.getFirst(socketEndpointMap);
		}else{
			String beanName = ClassUtils.getShortNameAsProperty(endpointClass);
			if (socketEndpointMap.containsKey(beanName)) {
				endpoint = socketEndpointMap.get(beanName);
				return endpoint;
			}
		}
		return endpoint;
	}
}
