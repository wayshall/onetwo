package org.onetwo.boot.core;

import javax.validation.Validator;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.onetwo.common.spring.validator.EmptyTraversableResolver;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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
	
	@Configuration
	@ConditionalOnClass(name={"javax.el.ExpressionFactory", ValidatorConfiguration.LOCAL_VALIDATOR_CLASS})
	public static class ValidatorConfiguration {
		public static final String LOCAL_VALIDATOR_CLASS = "org.springframework.validation.beanvalidation.LocalValidatorFactoryBean";

		@Bean
		@Autowired
//		public ValidatorWrapper validatorWrapper(Validator validator, @Qualifier(ExceptionMessageAccessor.BEAN_EXCEPTION_MESSAGE) MessageSource messageSource){
		public ValidatorWrapper validatorWrapper(Validator validator){
//			if (validator instanceof LocalValidatorFactoryBean) {
//				LocalValidatorFactoryBean vfb = (LocalValidatorFactoryBean) validator;
//				vfb.setValidationMessageSource(messageSource);
//				vfb.setTraversableResolver(new EmptyTraversableResolver());
//			}
			return ValidatorWrapper.wrap(validator);
		}

		/***
		 * override ValidationAutoConfiguration#defaultValidator()
		 * @author weishao zeng
		 * @return
		 */
		@Bean
		@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//		@ConditionalOnMissingBean(Validator.class)
		public static LocalValidatorFactoryBean springValidator(@Qualifier(ExceptionMessageAccessor.BEAN_EXCEPTION_MESSAGE) MessageSource messageSource) {
			LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
			MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
			factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
			factoryBean.setValidationMessageSource(messageSource);
			factoryBean.setTraversableResolver(new EmptyTraversableResolver());
			return factoryBean;
		}
		
		/*@Bean
		@ConditionalOnMissingBean(Validator.class)
		public Validator validator(@Qualifier(ExceptionMessageAccessor.BEAN_EXCEPTION_MESSAGE) MessageSource messageSource) {
			Validator validator = null;
//			if (ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader())) {
			Class<?> clazz;
			try {
				String className = LOCAL_VALIDATOR_CLASS;
				clazz = ClassUtils.forName(className, org.onetwo.common.utils.ClassUtils.getDefaultClassLoader());
			} catch (ClassNotFoundException e) {
				throw new BeanInitializationException("Could not find default validator", e);
			} catch (LinkageError e) {
				throw new BeanInitializationException("Could not find default validator", e);
			}
			validator = (Validator) BeanUtils.instantiate(clazz);
			LocalValidatorFactoryBean vfb = (LocalValidatorFactoryBean) validator;
			vfb.setValidationMessageSource(messageSource);
			vfb.setTraversableResolver(new EmptyTraversableResolver());
			return validator;
		}*/
	}

	
	/*@Bean(name=ExceptionMessageAccessor.BEAN_EXCEPTION_MESSAGE)
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
	}*/
	
}
