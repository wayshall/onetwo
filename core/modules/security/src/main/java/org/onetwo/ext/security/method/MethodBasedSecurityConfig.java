package org.onetwo.ext.security.method;

import java.util.List;

import org.onetwo.ext.security.config.SecurityCommonContextConfig;
import org.onetwo.ext.security.url.MultiWebExpressionVoter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/*****
 * 此注解必须写在GlobalMethodSecurityConfiguration子类，否则无法起作用
 * @author way
 *
 */
@EnableGlobalMethodSecurity(securedEnabled=true)
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
//		List<AccessDecisionVoter<? extends Object>> decisionVoters = (List<AccessDecisionVoter<? extends Object>>)ReflectUtils.getFieldValue(decisionManager, "decisionVoters");
		AbstractAccessDecisionManager adm = (AbstractAccessDecisionManager) decisionManager;
		List<AccessDecisionVoter<? extends Object>> decisionVoters = adm.getDecisionVoters();

//		decisionVoters.add(new MethodWebExpressionVoter());
		decisionVoters.add(new MultiWebExpressionVoter());
		
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
