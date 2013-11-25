package org.onetwo.common.spring.context;

import javax.validation.Validator;

import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.propconf.AppConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ClassUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/****
 * WebMvcConfigurationSupport
 * @author weishao
 *
 */
public class BaseApplicationContextSupport implements ApplicationContextAware {

	protected ApplicationContext applicationContex;

	private AppConfig appConfig;
	
	
	/*public BaseApplicationContextSupport(AppConfig appConfig) {
		super();
		this.appConfig = appConfig;
	}*/

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContex = applicationContext;
		// listenerManager.addListener(pluginManager());
	}

	public ApplicationContext getApplicationContex() {
		return applicationContex;
	}
	
	@Bean
	public AppConfig appConfig(){
		return appConfig;
	}
	

	@Bean
	public Validator beanValidator() {
		Validator validator = null;
		if (ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader())) {
			Class<?> clazz;
			try {
				String className = "org.springframework.validation.beanvalidation.LocalValidatorFactoryBean";
				clazz = ClassUtils.forName(className, org.onetwo.common.utils.ClassUtils.getDefaultClassLoader());
			} catch (ClassNotFoundException e) {
				throw new BeanInitializationException("Could not find default validator", e);
			} catch (LinkageError e) {
				throw new BeanInitializationException("Could not find default validator", e);
			}
			validator = (Validator) BeanUtils.instantiate(clazz);
			LocalValidatorFactoryBean vfb = (LocalValidatorFactoryBean) validator;
			vfb.setValidationMessageSource(validateMessageSource());
//			vfb.setTraversableResolver(new EmptyTraversableResolver());
		}
		return validator;
	}
	
	@Bean
	public ValidatorWrapper validatorWrapper(){
		return ValidatorWrapper.wrap(beanValidator());
	}

	@Bean
	public ReloadableResourceBundleMessageSource validateMessageSource() {
		ReloadableResourceBundleMessageSource ms = null;
		if(this.applicationContex.containsBean("validationMessages")){
			ms = this.applicationContex.getBean("validationMessages", ReloadableResourceBundleMessageSource.class);
		}else{
			ms = new ReloadableResourceBundleMessageSource();
			ms.setBasename("classpath*:messages/ValidationMessages");
		}
//		ms.setCacheSeconds(60);
		return ms;
	}
	

	@Bean(name = "cacheManager")
	public CacheManager cacheManager() {
		CacheManager cache = ehcacheCacheManager();
		if(cache==null){
			cache = jfishSimpleCacheManager();
		}
		
		return cache;
	}

	@Bean
	public CacheManager jfishSimpleCacheManager() {
		JFishSimpleCacheManagerImpl cache = new JFishSimpleCacheManagerImpl();
		return cache;
	}
	
	protected CacheManager ehcacheCacheManager(){
		return null;
	}
}
