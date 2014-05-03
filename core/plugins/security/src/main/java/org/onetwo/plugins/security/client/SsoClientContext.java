package org.onetwo.plugins.security.client;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.sso.SSOService;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.plugins.security.client.controller.SsoLoginController;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;


@Configuration
public class SsoClientContext {

	private static final String SSO_CLIENT_BASE = "/sso/client-config";
	public static final String SSO_CLIENT_CONFIG_PATH = SSO_CLIENT_BASE + ".properties";

	@Resource
	private AppConfig appConfig;

	@Resource
	private JFishProperties ssoClientConfig;
	
	@Bean
	public PropertiesFactoryBean ssoClientConfig() {
		String envLocation = SSO_CLIENT_BASE + "-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring(SSO_CLIENT_CONFIG_PATH, envLocation);
	}
	
	@Bean
	public SsoLoginController ssoLoginController(){
		return new SsoLoginController();
	}
	
	@Bean
	public HttpInvokerProxyFactoryBean ssoServerServiceProxy(){
		HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
		fb.setServiceInterface(SSOService.class);
		fb.setServiceUrl(ssoClientConfig.getProperty("server.service.url"));
		return fb;
	}
	
}
