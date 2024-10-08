package org.onetwo.ext.security.login;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

/**
 * @author weishao zeng
 * <br/>
 */
public class WebFormLoginSecurityConfigurer extends LoginSecurityConfigurer {
	

	public WebFormLoginSecurityConfigurer() {
	}
	
	@SuppressWarnings("unchecked")
	public void applyConfig(HttpSecurity http, SecurityConfig securityConfig) throws Exception {
		FormLoginConfigurer<HttpSecurity> formConfig = http.formLogin();

		
		if(StringUtils.isNotBlank(securityConfig.getDefaultLoginPage())){
			//if set default page, cannot set authenticationEntryPoint, see DefaultLoginPageConfigurer#configure
//			securityConfig.setLoginUrl(DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL);
			http.getConfigurer(DefaultLoginPageConfigurer.class).withObjectPostProcessor(new ObjectPostProcessor<DefaultLoginPageGeneratingFilter>(){
				@Override
				public <O extends DefaultLoginPageGeneratingFilter> O postProcess(O filter) {
					filter.setLoginPageUrl(securityConfig.getDefaultLoginPage());
					filter.setLogoutSuccessUrl(securityConfig.getLogoutSuccessUrl());
					filter.setFailureUrl(securityConfig.getFailureUrl());
					filter.setLoginPageUrl(securityConfig.getLoginUrl());
					return filter;
				}
			});
			this.configExceptionTranslationFilter(http, securityConfig);
			
			/*formConfig.loginPage(securityConfig.getLoginUrl())
						.permitAll();
			PropertyAccessorFactory.forDirectFieldAccess(formConfig)
									.setPropertyValue("customLoginPage", false);*/
		}else{
//			http.exceptionHandling()
//				.authenticationEntryPoint(authenticationEntryPoint);
			http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint);
		}

		// 若设置了loginPage，相当于设置了自定义登录页，会导致默认登录页(DefaultLoginPageGeneratingFilter)失效
		// 设置loginPage，可放到 withObjectPostProcessor 里设置
//		formConfig.loginPage(securityConfig.getLoginUrl())
//					.permitAll();
		

		formConfig.loginProcessingUrl(securityConfig.getLoginProcessUrl())
					.usernameParameter("username")
					.passwordParameter("password")
					.failureUrl(securityConfig.getFailureUrl())
					.failureHandler(ajaxAuthenticationHandler)
					.successHandler(ajaxAuthenticationHandler)
					.permitAll(); // 设置 loginPage, loginProcessingUrl, failureUrl三个url允许访问
	}

	
}
