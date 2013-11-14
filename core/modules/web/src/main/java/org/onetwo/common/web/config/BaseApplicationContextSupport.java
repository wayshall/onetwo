package org.onetwo.common.web.config;

import javax.validation.Validator;

import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.propconf.AppConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ClassUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/****
 * WebMvcConfigurationSupport
 * @author weishao
 *
 */
public class BaseApplicationContextSupport implements ApplicationContextAware {

	protected ApplicationContext applicationContex;

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
		return BaseSiteConfig.getInstance();
	}
	

	@Bean
	public Validator beanValidator() {
		Validator validator = null;
		if (ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader())) {
			Class<?> clazz;
			try {
				String className = "org.springframework.validation.beanvalidation.LocalValidatorFactoryBean";
				clazz = ClassUtils.forName(className, WebMvcConfigurationSupport.class.getClassLoader());
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
}
