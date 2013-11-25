package org.onetwo.common.spring.context;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.plugin.ContextPlugin;
import org.onetwo.common.spring.plugin.ContextPluginManager;
import org.onetwo.common.spring.plugin.DefaultContextPluginMeta;
import org.onetwo.common.spring.plugin.SpringContextPluginManager;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.JFishList;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/***
 * service上下文初始化
 * initialize in web app start 
 * config in web.xml
 * plugin init, 
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public class SpringProfilesWebApplicationContext extends AnnotationConfigWebApplicationContext {

	public SpringProfilesWebApplicationContext(){
	}
	
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
