package org.onetwo.boot.groovy;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.apiclient.RestExecutor;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import com.google.common.collect.Lists;

import groovy.lang.Binding;

/**
 * @author weishao zeng
 * <br/>
 */
public class GroovyBindingFactoryBean implements FactoryBean<Binding>, ApplicationContextAware, InitializingBean {
	
	private ApplicationContext applicationContext;
	
	/****
	 * 需要绑定到groovy binding到bean类型
	 */
	private List<Class<?>> bindingBeanClassList;
	private Binding binding;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.binding = createBinding();
	}
	
	public Binding createBinding() {
		List<Class<?>> bindingBeanClassList = Lists.newArrayList();
		if (this.bindingBeanClassList!=null) {
			bindingBeanClassList.addAll(this.bindingBeanClassList);
		}
//		bindingBeanClassList.add(GroovyJdbcTemplate.class);
		bindingBeanClassList.add(RestExecutor.class);
		

		Binding binding = new Binding();
		
		bindingBeanClassList.forEach(clazz -> {
			Object bean = SpringUtils.getBean(applicationContext, clazz);
			String varName = StringUtils.uncapitalize(clazz.getSimpleName());
			setBinding(binding, varName, bean);
		});
		
		Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(GroovyBindingBean.class);
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
			setBinding(binding, varName, bean);
		});
		
		return binding;
	}
	
	private void setBinding(Binding binding, String varName, Object bean) {
		if (binding.hasVariable(varName)) {
			Object var = binding.getVariable(varName);
			throw new BaseException("error var name: "+ varName + ", groovy binding var conflict, the var name has binding to : " + var);
		}
		binding.setVariable(varName, bean);
	}
	
	@Override
	public Binding getObject() throws Exception {
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
