package org.onetwo.boot.groovy;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import groovy.lang.Binding;

/**
 * @author weishao zeng
 * <br/>
 */
public class GroovyBindingFactoryBean implements FactoryBean<Binding>, ApplicationContextAware, InitializingBean {
	
	private ApplicationContext applicationContext;
	@Autowired(required = false)
	private DbmSessionFactory sessionFactory;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public Binding getObject() throws Exception {
		Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(GroovyBindingBean.class);
		Binding binding = new Binding();
		binding.setVariable("sessionFactory", sessionFactory);
		beanMap.forEach((name, bean) -> {
			String varName = name;
			GroovyBindingBean gbb = AnnotationUtils.getAnnotation(bean.getClass(), GroovyBindingBean.class);
			if (StringUtils.isNoneBlank(gbb.alias())) {
				varName = gbb.alias();
			}
			binding.setVariable(varName, bean);
		});
		return binding;
	}

	@Override
	public Class<?> getObjectType() {
		return Binding.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
