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
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	@ConditionalOnMissingBean(DefaultMethodSecurityConfigurer.class)
	public DefaultMethodSecurityConfigurer defaultSecurityConfigurer(){
		return super.defaultSecurityConfigurer();
	}

	/*protected RunAsManager runAsManager() {
		return new RunAsManagerImpl();
	}*/
	
}
