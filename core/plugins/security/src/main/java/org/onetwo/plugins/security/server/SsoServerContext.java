package org.onetwo.plugins.security.server;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.sso.SSOService;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.plugins.security.server.service.SsoServerServiceImpl;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;


@Configuration
public class SsoServerContext {

	private static final String SSO_SERVER_BASE = "/sso/server-config";
	public static final String SSO_SERVER_CONFIG_PATH = SSO_SERVER_BASE + ".properties";

	@Resource
	private AppConfig appConfig;
	
	@Bean
	public PropertiesFactoryBean ssoServerConfig() {
		String envLocation = SSO_SERVER_BASE + "-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring(SSO_SERVER_CONFIG_PATH, envLocation);
	}
	
	@Bean
	public SSOService ssoServerServiceImpl(){
		return new SsoServerServiceImpl();
	}
	
	@Bean
	public HttpInvokerServiceExporter ssoServerService(){
		HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
		exporter.setService(ssoServerServiceImpl());
		exporter.setServiceInterface(SSOService.class);
		return exporter;
	}
}
