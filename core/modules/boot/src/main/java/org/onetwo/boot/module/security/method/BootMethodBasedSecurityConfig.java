package org.onetwo.boot.module.security.method;

import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.boot.module.security.config.BootSecurityCommonContextConfig;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.onetwo.ext.security.method.MethodBasedSecurityConfig;
import org.onetwo.ext.security.method.RelaodableDelegatingMethodSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.method.MethodSecurityMetadataSource;

//@EnableGlobalMethodSecurity(securedEnabled=true)
@Configuration
@Import(BootSecurityCommonContextConfig.class)
public class BootMethodBasedSecurityConfig extends MethodBasedSecurityConfig {
	
	/**
	 * 复制自sb1.x版本的SecurityProperties，此属性在2.x被移除
	 * 复制至此兼容
	 * 
	 * Order before the basic authentication access control provided by Boot. This is a
	 * useful place to put user-defined access rules if you want to override the default
	 * access rules.
	 */
	public static final int ACCESS_OVERRIDE_ORDER = SecurityProperties.BASIC_AUTH_ORDER - 2;
	
	@Autowired
	private BootSpringConfig bootSpringConfig;
	

	@ConditionalOnMissingBean(AccessDecisionManager.class)
	@Bean
	public AccessDecisionManager accessDecisionManager(){
		return super.accessDecisionManager();
	}

	@Override
	@Bean
	public MethodSecurityMetadataSource methodSecurityMetadataSource() {
		if(bootSpringConfig.isDev()){
			return new RelaodableDelegatingMethodSecurityMetadataSource(super.methodSecurityMetadataSource());
		}else{
			return super.methodSecurityMetadataSource();
		}
	}


	@Bean
	@Order(BootMethodBasedSecurityConfig.ACCESS_OVERRIDE_ORDER)
	@ConditionalOnMissingBean(DefaultMethodSecurityConfigurer.class)
	public DefaultMethodSecurityConfigurer defaultSecurityConfigurer(){
		return super.defaultSecurityConfigurer();
	}

	/*protected RunAsManager runAsManager() {
		return new RunAsManagerImpl();
	}*/
	
}
