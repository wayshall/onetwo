package org.onetwo.boot.plugin;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.JFishList;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/***
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public class BootPluginableWebApplicationContext extends AnnotationConfigWebApplicationContext {

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
//	private PluginManagerInitializer pluginManagerInitializer = new ContextPluginManagerInitializer();
	private String appEnvironment;
	private Class<?>[] annotatedClasses;
	
	public BootPluginableWebApplicationContext(){	
	}

	public String getAppEnvironment() {
		return appEnvironment;
	}

	public void setAppEnvironment(String appEnvironment) {
		this.appEnvironment = appEnvironment;
	}


	public Class<?>[] getAnnotatedClasses() {
		return annotatedClasses;
	}

	public void setAnnotatedClasses(Class<?>... annotatedClasses) {
		this.annotatedClasses = annotatedClasses;
	}

	protected void prepareRefresh() {
		JFishList<Class<?>> configClasseList = JFishList.create();
//		this.pluginManagerInitializer.initPluginContext(getAppEnvironment(), configClasseList);
		configClasseList.addArray(annotatedClasses);
		if(configClasseList.isNotEmpty())
			register(configClasseList.toArray(new Class<?>[0]));
		super.prepareRefresh();
	}

	@Override
	protected void finishRefresh() {
		super.finishRefresh();
//		getPluginManagerInitializer().finishedInitPluginContext();
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
