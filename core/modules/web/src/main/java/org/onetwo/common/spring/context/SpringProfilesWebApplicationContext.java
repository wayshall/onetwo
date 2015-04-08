package org.onetwo.common.spring.context;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.plugin.ContextPluginManagerInitializer;
import org.onetwo.common.spring.plugin.PluginManagerInitializer;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.JFishList;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/***
 * webapp(not jfish)上下文初始化
 * initialize in web app start 
 * config in web.xml
 * plugin init, 
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public class SpringProfilesWebApplicationContext extends AnnotationConfigWebApplicationContext {

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private PluginManagerInitializer pluginManagerInitializer = new ContextPluginManagerInitializer();
	private String appEnvironment;
	private Class<?>[] annotatedClasses;
//	private ContextPluginManager<?> contextPluginManager;
	
	public SpringProfilesWebApplicationContext(){
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
		this.getPluginManagerInitializer().initPluginContext(getAppEnvironment(), configClasseList);
		configClasseList.addArray(annotatedClasses);
		if(configClasseList.isNotEmpty())
			register(configClasseList.toArray(new Class<?>[0]));
		super.prepareRefresh();
	}
	

	protected void finishRefresh() {
		super.finishRefresh();
	}
	
	public PluginManagerInitializer getPluginManagerInitializer() {
		return pluginManagerInitializer;
	}

	final public void setPluginManagerInitializer(PluginManagerInitializer pluginManagerInitializer) {
		this.pluginManagerInitializer = pluginManagerInitializer;
	}
/*
	final protected void initPluginContext(String appEnvironment, Class<?>... outerContextClasses){
		SpringUtils.setProfiles(appEnvironment);
		
		ContextPluginManager jpm = createPluginManager(appEnvironment);
		jpm.scanPlugins();
		
		final JFishList<Class<?>> contextClasses = JFishList.create();
//		contextClasses.add(ClassPathApplicationContext.class);
		contextClasses.addArray(outerContextClasses);
		jpm.registerPluginJFishContextClasses(contextClasses);

		this.register(contextClasses.toArray(new Class[contextClasses.size()]));
	}
	
	protected ContextPluginManager createPluginManager(String appEnvironment){
		return new SpringContextPluginManager<DefaultContextPluginMeta<ContextPlugin>>(appEnvironment);
	}*/
	
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
