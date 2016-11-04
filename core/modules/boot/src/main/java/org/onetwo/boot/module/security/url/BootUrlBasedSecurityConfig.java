package org.onetwo.boot.module.security.url;

import org.onetwo.boot.module.security.config.BootSecurityCommonContextConfig;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.onetwo.ext.security.method.JFishMethodSecurityMetadataSource;
import org.onetwo.ext.security.url.SecurityBeanPostProcessor;
import org.onetwo.ext.security.url.UrlBasedSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Import(BootSecurityCommonContextConfig.class)
public class BootUrlBasedSecurityConfig extends UrlBasedSecurityConfig {
	
	/*** 
	 * 如果不是基于方法拦截（即url匹配），需要用后处理器重新配置SecurityMetadataSource
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(JFishMethodSecurityMetadataSource.class)
	public SecurityBeanPostProcessor securityBeanPostProcessor(){
		return super.securityBeanPostProcessor();
	}

	@Bean
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
	@Autowired
	public DefaultMethodSecurityConfigurer defaultSecurityConfigurer(AccessDecisionManager accessDecisionManager){
		return super.defaultSecurityConfigurer(accessDecisionManager);
	}
}
