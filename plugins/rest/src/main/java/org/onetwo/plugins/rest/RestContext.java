package org.onetwo.plugins.rest;

import org.onetwo.plugins.rest.RestConstant.RestBeanNames;
import org.onetwo.plugins.rest.exception.RestExceptionResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@ComponentScan(basePackageClasses=RestPlugin.class)
public class RestContext {
	
	@Bean
	public RestJsonView restMVPostInterceptor(){
		RestJsonView post = new RestJsonView();
		post.setExceptionMessages(exceptionMessageSource());
		return post;
	}
	
	@Bean
	public RestExceptionResolver restExceptionResolver(){
		RestExceptionResolver rest = new RestExceptionResolver();
		rest.setExceptionMessage(exceptionMessageSource());
		return rest;
	}

	@Bean(name=RestBeanNames.EXCEPTION_MESSAGE)
	public MessageSource exceptionMessageSource(){
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasenames("classpath:messages/ExceptionMessages", "classpath:messages/DefaultExceptionMessages");
		return ms;
	}
	
	@Bean
	public RestRequestMappingHandlerAdapterFactory restRequestMappingHandlerAdapterFactory(){
		return new RestRequestMappingHandlerAdapterFactory();
	}
	
	/*@Bean
	public RestRequestLogInterceptor restRequestLogInterceptor(){
		RestRequestLogInterceptor rest = new RestRequestLogInterceptor();
		return rest;
	}*/
}
