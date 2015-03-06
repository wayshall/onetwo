package org.onetwo.plugins.security.common;

import org.onetwo.common.spring.web.authentic.SecurityWebExceptionResolver;
import org.onetwo.common.spring.web.authentic.SpringAuthenticationInvocation;
import org.onetwo.common.spring.web.authentic.SpringSecurityInterceptor;
import org.onetwo.common.spring.web.authentic.SsoSpringSecurityInterceptor;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfig.MvcBeanNames;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;


@Configuration
@ComponentScan(basePackageClasses = { SpringAuthenticationInvocation.class })
public class SecurityWebContext {
	
	@Bean
	public SpringSecurityInterceptor springSecurityInterceptor(){
		if(SecurityPluginUtils.existServerConfig() || SecurityPluginUtils.existClientConfig())
			return new SsoSpringSecurityInterceptor();
		//SimpleNotSSOServiceImpl
		return new SpringSecurityInterceptor();
	}

	@Bean(name=DispatcherServlet.HANDLER_EXCEPTION_RESOLVER_BEAN_NAME)
	public SecurityWebExceptionResolver webExceptionResolver(@Qualifier(MvcBeanNames.EXCEPTION_MESSAGE)MessageSource exceptionMessage){
		SecurityWebExceptionResolver webExceptionResolver = new SecurityWebExceptionResolver();
		webExceptionResolver.setExceptionMessage(exceptionMessage);
		return webExceptionResolver;
	}
	
	/*@Bean
	public SSOService simpleNotSSOServiceImpl(){
	}*/
	
}
