package org.onetwo.boot.core;

import javax.validation.Validator;

import org.onetwo.boot.core.config.BootBusinessConfig;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.onetwo.boot.plugin.PluginContextConfig;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.dbm.mapping.DataBaseConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ClassUtils;

@Configuration
@Import(PluginContextConfig.class)
//@EnableConfigurationProperties({JFishBootConfig.class, SpringBootConfig.class})
@EnableConfigurationProperties({BootJFishConfig.class, BootSpringConfig.class, BootBusinessConfig.class, BootSiteConfig.class})
public class BootContextConfig {
	
	
	@Autowired
	private BootJFishConfig bootJFishConfig;

	@Bean
	public ValidatorWrapper validatorWrapper(){
		return ValidatorWrapper.wrap(validator());
	}
	
	@Bean
	public Validator validator() {
		Validator validator = null;
//		if (ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader())) {
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
//		LocalValidatorFactoryBean vfb = (LocalValidatorFactoryBean) validator;
//		vfb.setValidationMessageSource(messageSource());
//			vfb.setTraversableResolver(new EmptyTraversableResolver());
		return validator;
	}
	
	@Bean(name=ExceptionMessageAccessor.BEAN_EXCEPTION_MESSAGE)
	public MessageSource exceptionMessageSource(){
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setCacheSeconds(bootJFishConfig.getMessageSource().getCacheSeconds());
		ms.setBasenames("classpath:messages/exception-messages", "classpath:messages/default-exception-messages");
		return ms;
	}
	@Bean
	public ExceptionMessageAccessor exceptionMessageAccessor(){
		ExceptionMessageAccessor exceptionMessageAccessor = new ExceptionMessageAccessor(exceptionMessageSource());
		return exceptionMessageAccessor;
	}
	
	@Bean
//	@ConditionalOnMissingBean(DataBaseConfig.class)
	public DataBaseConfig dataBaseConfig(){
		return bootJFishConfig.getDbm();
	}
}
