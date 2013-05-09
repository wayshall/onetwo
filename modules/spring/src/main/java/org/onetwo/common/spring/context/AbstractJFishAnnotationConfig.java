package org.onetwo.common.spring.context;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@SuppressWarnings("unchecked")
abstract public class AbstractJFishAnnotationConfig extends AnnotationConfigWebApplicationContext {
	
	public AbstractJFishAnnotationConfig(){
	}
	
	final public BeanDefinitionRegistry getBeanDefinitionRegistry() {
		BeanFactory bf = this.getBeanFactory();
		if(!BeanDefinitionRegistry.class.isInstance(bf)){
			throw new BaseException("this context can not rigister spring bean : " + bf);
		}
		return (BeanDefinitionRegistry) bf;
	}

	public BeanDefinitionRegistry registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		BeanDefinitionRegistry bdr = getBeanDefinitionRegistry();
		bdr.registerBeanDefinition(beanName, beanDefinition);
		return bdr;
	}

	public <T> T registerAndGetBean(Class<?> beanClass, Object...params) {
		String beanName = StringUtils.uncapitalize(beanClass.getSimpleName());
		return registerAndGetBean(beanName, beanClass, params);
	}
	
	public <T> T registerAndGetBean(String beanName, Class<?> beanClass, Object...params) {
		registerBeanDefinition(beanName, SpringUtils.createBeanDefinition(beanClass, params));
		T bean = (T) getBean(beanName);
		if(bean==null)
			throw new BaseException("register spring bean error : " + beanClass);
		return bean;
	}
}
