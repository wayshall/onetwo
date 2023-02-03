package org.onetwo.boot.groovy;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.springframework.aop.support.AopUtils;
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
			Class<?> clazz = null;
			GroovyBindingBean gbb = null;
			boolean proxy = AopUtils.isJdkDynamicProxy(bean);
			if (proxy) {
				for (Class<?> c : bean.getClass().getInterfaces()) {
					gbb = AnnotationUtils.getAnnotation(c, GroovyBindingBean.class);
					if (gbb!=null) {
						clazz = c;
						break;
					}
				}
			} else {
				clazz =  AopUtils.getTargetClass(bean);
				gbb = AnnotationUtils.getAnnotation(clazz, GroovyBindingBean.class);
			}
			if (StringUtils.isNotBlank(gbb.alias())) {
				varName = gbb.alias();
			}
			if (binding.hasVariable(varName)) {
				Object var = binding.getVariable(varName);
				throw new BaseException("error var name: "+ varName + ", groovy binding var conflict, the var name has binding to : " + var);
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
