package org.onetwo.boot.core;

import javax.validation.Validator;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ClassUtils;

/****
 * 非web环境的配置
 * @author wayshall
 *
 */
@Configuration
//@EnableConfigurationProperties({JFishBootConfig.class, SpringBootConfig.class})
@EnableConfigurationProperties({BootJFishConfig.class})
public class BootContextConfig {
	
	
	/*@Autowired
	private BootJFishConfig bootJFishConfig;*/

	@Bean
	@Autowired
	public ValidatorWrapper validatorWrapper(Validator validator){
		return ValidatorWrapper.wrap(validator);
	}
	
	@Bean
	@ConditionalOnMissingBean(Validator.class)
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
	public MessageSource exceptionMessageSource(@Autowired BootJFishConfig bootJFishConfig){
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setCacheSeconds(bootJFishConfig.getMessageSource().getCacheSeconds());
		ms.setBasenames("classpath:messages/exception-messages", "classpath:messages/default-exception-messages");
		return ms;
	}
	
	@Bean
	public ExceptionMessageAccessor exceptionMessageAccessor(@Autowired @Qualifier(ExceptionMessageAccessor.BEAN_EXCEPTION_MESSAGE) MessageSource exceptionMessageSource){
		ExceptionMessageAccessor exceptionMessageAccessor = new ExceptionMessageAccessor(exceptionMessageSource);
		return exceptionMessageAccessor;
	}
	
}
