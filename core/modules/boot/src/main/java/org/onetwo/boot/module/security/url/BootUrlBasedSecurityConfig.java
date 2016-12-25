package org.onetwo.boot.module.security.url;

import org.onetwo.boot.module.security.config.BootSecurityCommonContextConfig;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.onetwo.ext.security.url.UrlBasedSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;

@Configuration
@Import(BootSecurityCommonContextConfig.class)
public class BootUrlBasedSecurityConfig extends UrlBasedSecurityConfig {

	@Bean
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	@ConditionalOnMissingBean(DefaultMethodSecurityConfigurer.class)
	@Autowired
	public DefaultMethodSecurityConfigurer defaultSecurityConfigurer(AccessDecisionManager accessDecisionManager){
		return super.defaultSecurityConfigurer(accessDecisionManager);
	}
}
