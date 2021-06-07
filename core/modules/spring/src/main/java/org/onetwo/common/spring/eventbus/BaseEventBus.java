package org.onetwo.common.spring.eventbus;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.eventbus.EventBus;

/**
 * @author wayshall
 * <br/>
 */
abstract public class BaseEventBus implements InitializingBean {
	@Autowired
	private ApplicationContext applicationContext;
	private EventBus eventBus;
	private Class<? extends Annotation> listennerClass;
	
	public BaseEventBus(String name) {
		this(name, null);
	}
	
	public BaseEventBus(String name, Class<? extends Annotation> listennerType) {
		this.eventBus = new EventBus(name + "EventBus");
		this.listennerClass = listennerType;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (listennerClass!=null) {
			Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(listennerClass);
			beanMap.forEach((name, bean)->{
				register(bean);
			});
		}
	}

	final public void register(Object listener){
		eventBus.register(listener);
	}
	
	final public void post(Object event){
		this.eventBus.post(event);
	}

	final protected EventBus getEventBus() {
		return eventBus;
	}

}
