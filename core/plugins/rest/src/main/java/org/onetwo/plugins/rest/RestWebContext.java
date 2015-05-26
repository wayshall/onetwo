package org.onetwo.plugins.rest;

import javax.annotation.Resource;

import org.onetwo.common.fish.spring.config.JFishContextConfig.ContextBeanNames;
import org.onetwo.common.spring.web.mvc.MvcSetting;
import org.onetwo.plugins.rest.web.RestExceptionResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ComponentScan(basePackageClasses=RestPlugin.class)
public class RestWebContext {

	@Resource
	private MvcSetting mvcSetting;

	@Bean(name=DispatcherServlet.HANDLER_EXCEPTION_RESOLVER_BEAN_NAME)
	public RestExceptionResolver restExceptionResolver(@Qualifier(ContextBeanNames.EXCEPTION_MESSAGE)MessageSource exceptionMessage){
		RestExceptionResolver rest = new RestExceptionResolver();
		rest.setExceptionMessage(exceptionMessage);
		rest.setMvcSetting(mvcSetting);
		return rest;
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
