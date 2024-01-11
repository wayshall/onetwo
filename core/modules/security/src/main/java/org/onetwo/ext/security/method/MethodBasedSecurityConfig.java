package org.onetwo.ext.security.method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.ext.permission.MenuInfoParserFactory;
import org.onetwo.ext.permission.SimplePermission;
import org.onetwo.ext.security.config.SecurityCommonContextConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import io.micrometer.observation.ObservationRegistry;

/*****
 * 此注解必须写在GlobalMethodSecurityConfiguration子类，否则无法起作用
 * 
 * SecuredAnnotationSecurityMetadataSource
 * Jsr250MethodSecurityMetadataSource, Jsr250Voter
 * @author way
 *
 */
@EnableMethodSecurity(
		prePostEnabled=true,
		securedEnabled=true, // SecuredAnnotationSecurityMetadataSource
		jsr250Enabled = true // Jsr250MethodSecurityMetadataSource, Jsr250Voter
)
@Import({
	SecurityCommonContextConfig.class,
	DefaultMethodSecurityConfigurer.class
})
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class MethodBasedSecurityConfig {
	
	@Bean
	public MenuInfoParserFactory<SimplePermission> menuInfoParserFactory(){
		MenuInfoParserFactory<SimplePermission> facotry = new MenuInfoParserFactory<>(SimplePermission.class);
		return facotry;
	}
	
	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	static MethodInterceptor byPermissionAuthorizationMethodInterceptor(
			ObjectProvider<SecurityContextHolderStrategy> strategyProvider,
			ObjectProvider<ObservationRegistry> registryProvider) {
		JFishMethodSecurityMetadataSource secured = new JFishMethodSecurityMetadataSource();
		SecurityContextHolderStrategy strategy = strategyProvider
			.getIfAvailable(SecurityContextHolder::getContextHolderStrategy);
		AuthorizationManager<MethodInvocation> manager = new DeferringObservationAuthorizationManagerDelegate<>(
				registryProvider, secured);
		AuthorizationManagerBeforeMethodInterceptor interceptor = AuthorizationManagerBeforeMethodInterceptor
			.secured(manager);
		interceptor.setSecurityContextHolderStrategy(strategy);
		return interceptor;
	}
	
}
