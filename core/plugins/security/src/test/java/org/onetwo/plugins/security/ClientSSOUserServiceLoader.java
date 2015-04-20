package org.onetwo.plugins.security;

import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;
import org.onetwo.common.web.sso.SSOUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class ClientSSOUserServiceLoader extends SpringConfigApplicationContextLoader {

	@Configuration
	public static class ClientSSOUserServiceContext {

		@Bean
		public HttpInvokerProxyFactoryBean ssoUserServiceProxy(){
			HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
			fb.setServiceInterface(SSOUserService.class);
			fb.setServiceUrl("http://www.loginserver.com:9080/iccardsso/plugin-security/ssoUserServiceExporter");
			return fb;
		}
	}
	
	@Override
	protected AnnotationConfigWebApplicationContext createContext(){
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(ClientSSOUserServiceContext.class);
		context.refresh();
		return context;
	}

}
