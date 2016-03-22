package org.onetwo.boot.plugins.security.config;

import java.util.List;

import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.onetwo.ext.security.method.JFishMethodSecurityMetadataSource;
import org.onetwo.ext.security.method.MethodWebExpressionVoter;
import org.onetwo.ext.security.method.RelaodableDelegatingMethodSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableGlobalMethodSecurity(securedEnabled=true)
@Configuration
@Import({RbacSecurityXmlContextConfigSupport.class})
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
	
	@Autowired
	private BootSpringConfig bootSpringConfig;
	
	@Bean
	public JFishMethodSecurityMetadataSource jfishMethodSecurityMetadataSource(){
		return new JFishMethodSecurityMetadataSource();
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


	@Override
	protected AccessDecisionManager accessDecisionManager() {
		AccessDecisionManager decisionManager = super.accessDecisionManager();
		List<AccessDecisionVoter<? extends Object>> decisionVoters = (List<AccessDecisionVoter<? extends Object>>)ReflectUtils.getFieldValue(decisionManager, "decisionVoters");
		decisionVoters.add(new MethodWebExpressionVoter());
		return decisionManager;
	}


	@Override
	protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
		return jfishMethodSecurityMetadataSource();
	}

	/*@Order(Ordered.HIGHEST_PRECEDENCE)
	@Configuration
	protected static class AuthenticationSecurity
			extends GlobalAuthenticationConfigurerAdapter {

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("admin").password("admin")
					.roles("ADMIN", "USER").and().withUser("user").password("user")
					.roles("USER");
		}

	}*/

	@Bean
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
	public DefaultMethodSecurityConfigurer defaultSecurityConfigurer(){
		return new DefaultMethodSecurityConfigurer();
	}
	
}
