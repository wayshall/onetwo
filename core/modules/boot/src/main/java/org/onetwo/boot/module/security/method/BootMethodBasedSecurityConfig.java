package org.onetwo.boot.module.security.method;

import org.onetwo.boot.core.config.BootSpringConfig;
//import org.onetwo.boot.module.oauth2.ssoclient.DisabledOauth2SsoCondition;
import org.onetwo.boot.module.security.config.BootSecurityCommonContextConfig;
import org.onetwo.ext.security.config.SecurityCommonContextConfig;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.onetwo.ext.security.method.MethodBasedSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@EnableMethodSecurity(
		prePostEnabled=true,
		securedEnabled=true, // SecuredAnnotationSecurityMetadataSource
		jsr250Enabled = true // Jsr250MethodSecurityMetadataSource, Jsr250Voter
)
@Import({
	SecurityCommonContextConfig.class,
	DefaultMethodSecurityConfigurer.class,
	BootSecurityCommonContextConfig.class // add
	})
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
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
	

//	@ConditionalOnMissingBean(AccessDecisionManager.class)
//	@Bean
//	public AccessDecisionManager accessDecisionManager(){
//		return super.accessDecisionManager();
//	}
//
//	@Override
//	@Bean
//	public MethodSecurityMetadataSource methodSecurityMetadataSource() {
//		if(bootSpringConfig.isDev()){
//			return new RelaodableDelegatingMethodSecurityMetadataSource(super.methodSecurityMetadataSource());
//		}else{
//			return super.methodSecurityMetadataSource();
//		}
//	}


//	@Bean
//	@Order(ACCESS_OVERRIDE_ORDER)
////	@ConditionalOnMissingBean(DefaultMethodSecurityConfigurer.class)
//	@Conditional(DisabledOauth2SsoCondition.class)
//	public DefaultMethodSecurityConfigurer defaultSecurityConfigurer(){
//		return super.defaultSecurityConfigurer();
//	}

	/*protected RunAsManager runAsManager() {
		return new RunAsManagerImpl();
	}*/
	
}
