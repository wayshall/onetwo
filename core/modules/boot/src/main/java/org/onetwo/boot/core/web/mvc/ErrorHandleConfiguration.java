package org.onetwo.boot.core.web.mvc;

import java.util.List;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionHandler;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@EnableConfigurationProperties({BootSiteConfig.class, BootSpringConfig.class, BootJFishConfig.class, ServerProperties.class})
public class ErrorHandleConfiguration {

//	@Autowired
//	private ServerProperties serverProperties;


	@Autowired(required = false)
	private List<ErrorViewResolver> errorViewResolvers;
	/***
	 * 异常解释
	 * @return
	 */
//	@Bean(BootWebCommonAutoConfig.BEAN_NAME_EXCEPTION_RESOLVER)
	@Bean
	public ResponseEntityExceptionHandler responseEntityExceptionHandler(){
		BootWebExceptionHandler handler = new BootWebExceptionHandler();
		return handler;
	}
	
	@Bean
	@ConditionalOnMissingBean(DataResultErrorController.class)
	public ErrorController dataResultErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties){
		return new DataResultErrorController(errorAttributes, serverProperties.getError(),
				this.errorViewResolvers);
	}
	
	@Bean(name=ExceptionMessageAccessor.BEAN_EXCEPTION_MESSAGE)
	public MessageSource exceptionMessageSource(@Autowired BootJFishConfig bootJFishConfig){
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setCacheSeconds(bootJFishConfig.getMessageSource().getCacheSeconds());
		ms.setBasenames("classpath:messages/exception-messages", 
				"classpath:messages/default-exception-messages", 
				"org/hibernate/validator/ValidationMessages");
		return ms;
	}
	
	@Bean
	public ExceptionMessageAccessor exceptionMessageAccessor(@Autowired @Qualifier(ExceptionMessageAccessor.BEAN_EXCEPTION_MESSAGE) MessageSource exceptionMessageSource){
		ExceptionMessageAccessor exceptionMessageAccessor = new ExceptionMessageAccessor(exceptionMessageSource);
		return exceptionMessageAccessor;
	}
	
}
