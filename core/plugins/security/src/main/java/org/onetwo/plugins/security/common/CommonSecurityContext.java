package org.onetwo.plugins.security.common;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.plugins.security.SecurityPlugin;
import org.onetwo.plugins.security.client.SsoClientConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CommonSecurityContext implements InitializingBean {

	private static final String SECURITY_CONFIG_BASE = "/plugins/security/security-config";
	public static final String SECURITY_CONFIG_PATH = SECURITY_CONFIG_BASE + ".properties";

	@Resource
	private AppConfig appConfig;

	@Resource
	private SecurityConfig securityConfig;

	@Resource
	private ApplicationContext applicationContext;
	
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		SecurityPlugin.getInstance().setSecurityConfig(securityConfig);
	}

	@Bean
	public PropertiesFactoryBean securityConfigFactory() {
		String envLocation = SECURITY_CONFIG_BASE + "-" + appConfig.getAppEnvironment() + ".properties";
		return SpringUtils.createPropertiesBySptring(new SsoClientConfig(), SECURITY_CONFIG_PATH, envLocation);
	}
	
	@Bean
	public SecurityConfig securityConfig(){
		return securityConfig;
	}
	
	
}
