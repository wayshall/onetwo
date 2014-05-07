package org.onetwo.plugins.security.client;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.sso.SSOService;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.web.sso.DefaultSSOServiceImpl;
import org.onetwo.common.web.sso.SSOUserService;
import org.onetwo.plugins.security.client.controller.SsoLoginController;
import org.onetwo.plugins.security.client.service.ClientSSOUserServiceImpl;
import org.onetwo.plugins.security.common.SsoConfig;
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
	private SsoClientConfig ssoClientConfig;
	
	@Bean
	public PropertiesFactoryBean ssoClientConfig() {
		String envLocation = SSO_CLIENT_BASE + "-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring(new SsoClientConfig(), SSO_CLIENT_CONFIG_PATH, envLocation);
	}
	
	@Bean
	public SsoConfig getSsoConfig(){
		return ssoClientConfig;
	}
	
	@Bean
	public SsoLoginController ssoLoginController(){
		return new SsoLoginController();
	}
	
	@Bean
	public SSOService ssoService(){
		DefaultSSOServiceImpl ssoservice = new DefaultSSOServiceImpl();
		ssoservice.setSSOUserService(ssoUserClientServiceImpl());
		return ssoservice;
	}

	@Bean
	public SSOUserService ssoUserClientServiceImpl(){
		return new ClientSSOUserServiceImpl();
	}
	
	
	@Bean
	public HttpInvokerProxyFactoryBean ssoUserServiceProxy(){
		HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
		fb.setServiceInterface(SSOUserService.class);
		fb.setServiceUrl(ssoClientConfig.getSSOUserServiceUrl());
		return fb;
	}
	
}
