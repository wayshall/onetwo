package org.onetwo.ext.security.method;

import java.util.List;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.security.config.SecurityCommonContextConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

//@EnableGlobalMethodSecurity(securedEnabled=true)
@Configuration
@Import(SecurityCommonContextConfig.class)
public class MethodBasedSecurityConfig extends GlobalMethodSecurityConfiguration {
	
	/*@Bean
	public SecurityCommonContextConfig securityCommonContextConfig(){
		return new SecurityCommonContextConfig();
	}*/
	
	@Bean
	public JFishMethodSecurityMetadataSource jfishMethodSecurityMetadataSource(){
		return new JFishMethodSecurityMetadataSource();
	}


	/***
	 * 对应的方法决策器
	 */
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

	@Bean
	public DefaultMethodSecurityConfigurer defaultSecurityConfigurer(){
		return new DefaultMethodSecurityConfigurer();
	}
	
}
