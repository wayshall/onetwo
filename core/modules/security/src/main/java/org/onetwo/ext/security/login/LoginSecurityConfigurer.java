package org.onetwo.ext.security.login;

import org.onetwo.ext.security.ajax.AjaxAuthenticationHandler;
import org.onetwo.ext.security.ajax.AjaxSupportedAccessDeniedHandler;
import org.onetwo.ext.security.ajax.AjaxSupportedAuthenticationEntryPoint;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.ext.security.utils.SimpleThrowableAnalyzer;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import lombok.Setter;

/**
 * @author weishao zeng
 * <br/>
 */
public class LoginSecurityConfigurer {
	
	@Setter
	protected AjaxSupportedAccessDeniedHandler ajaxSupportedAccessDeniedHandler;
	@Setter
	protected AjaxSupportedAuthenticationEntryPoint authenticationEntryPoint;
//	@Setter
//	protected AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;
	@Setter
	protected AjaxAuthenticationHandler ajaxAuthenticationHandler;

	public LoginSecurityConfigurer() {
	}
	
	public void applyConfig(HttpSecurity http, SecurityConfig securityConfig) throws Exception {
	}
	

	@SuppressWarnings("unchecked")
	protected void configExceptionTranslationFilter(HttpSecurity http, SecurityConfig securityConfig) {
		http.getConfigurer(ExceptionHandlingConfigurer.class).withObjectPostProcessor(new ObjectPostProcessor<ExceptionTranslationFilter>(){
			@Override
			public <O extends ExceptionTranslationFilter> O postProcess(O filter) {
				ConfigurablePropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(filter);
				accessor.setPropertyValue("authenticationEntryPoint", authenticationEntryPoint);
				// throwableAnalyzer
//				ThrowableAnalyzer analyzer = (ThrowableAnalyzer)accessor.getPropertyValue("throwableAnalyzer");
				if(securityConfig.isDebug()){
					SimpleThrowableAnalyzer analyzer = new SimpleThrowableAnalyzer();
					filter.setThrowableAnalyzer(analyzer);
				}
				return filter;
			}
		});
	}
}
