package org.onetwo.plugins.security.server;

import java.util.Properties;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.sso.SSOService;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.web.sso.SSOUserService;
import org.onetwo.plugins.security.SecurityPlugin;
import org.onetwo.plugins.security.server.service.impl.ServerSSOUserServiceImpl;
import org.onetwo.plugins.security.sso.DefaultSSOServiceImpl;
import org.onetwo.plugins.security.sso.SsoConfig;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;


@Configuration
public class SsoServerContext implements InitializingBean {

	private static final String SSO_SERVER_BASE = "/plugins/security/server-config";
	public static final String SSO_SERVER_CONFIG_PATH = SSO_SERVER_BASE + ".properties";
	public static final String SSO_USERSERVICE_EXPORTER_NAME = SecurityPluginUtils.SSO_USERSERVICE_EXPORTER_NAME;
	
	@Resource
	private AppConfig appConfig;
	
	@Resource
	private SsoServerConfig ssoServerConfig;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		SecurityPlugin.getInstance().setSecurityConfig(ssoServerConfig);
	}
	
	@Bean
	public PropertiesFactoryBean ssoServerConfig() {
		String envLocation = SSO_SERVER_BASE + "-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring(new SsoServerConfig(), SSO_SERVER_CONFIG_PATH, envLocation);
	}
	
	@Bean
	public SSOUserService ssoUserService(){
		return new ServerSSOUserServiceImpl();
	}
	
	@Bean
	public SsoConfig getSsoConfig(){
		return ssoServerConfig;
	}

	/*@Bean
	public SessionStorer sessionStorer(){
		return new MemorySessionStorer();
	}*/
	
	/****
	 * the implementor of SSOUserService is {@linkplain ServerSSOUserServiceImpl ServerSSOUserServiceImpl}
	 * @return
	 */
	@Bean
	public SSOService ssoService(){
		DefaultSSOServiceImpl ssoservice = new DefaultSSOServiceImpl();
//		ssoservice.setSsoUserService(ssoUserService());
		return ssoservice;
	}
	
	@Bean(name=SSO_USERSERVICE_EXPORTER_NAME)
	public HttpInvokerServiceExporter ssoUserServiceExporter(){
		HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
		exporter.setService(ssoUserService());
		exporter.setServiceInterface(SSOUserService.class);
		return exporter;
	}
	
	
	@Bean
	public SimpleUrlHandlerMapping httpServiceUrlHandlerMapping(){
		Properties mappings = new Properties();
		String ssoUserServiceExporterUrl = ssoServerConfig.getService(SSO_USERSERVICE_EXPORTER_NAME);
		if(StringUtils.isBlank(ssoUserServiceExporterUrl))
			ssoUserServiceExporterUrl = SecurityPluginUtils.getSsoUserServiceExporterDefaultUrl(""); 
		mappings.setProperty(ssoUserServiceExporterUrl, SSO_USERSERVICE_EXPORTER_NAME);
		SimpleUrlHandlerMapping urlMapping = new SimpleUrlHandlerMapping();
		urlMapping.setMappings(mappings);
		urlMapping.setOrder(0);
		return urlMapping;
	}
	
}
