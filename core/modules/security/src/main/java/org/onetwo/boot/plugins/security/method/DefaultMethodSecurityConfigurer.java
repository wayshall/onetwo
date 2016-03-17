package org.onetwo.boot.plugins.security.method;

import lombok.Getter;

import org.onetwo.boot.plugins.security.AjaxAuthenticationHandler;
import org.onetwo.boot.plugins.security.BootSecurityConfig;
import org.onetwo.boot.plugins.security.CsrfRequestUrlMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class DefaultMethodSecurityConfigurer extends WebSecurityConfigurerAdapter {


	@Getter
	@Autowired
	private AjaxAuthenticationHandler ajaxAuthenticationHandler;

	@Getter
	@Autowired
	private AccessDeniedHandler ajaxSupportedAccessDeniedHandler;

	@Getter
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Getter
	@Autowired(required=false)
	private UserDetailsService userDetailsService;

	@Getter
	@Autowired
	private BootSecurityConfig securityConfig;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*auth
			.inMemoryAuthentication()
				.withUser("test")
				.password("$2a$10$OqXCwkP74VpdrnmAyv.sLeJqQZExSI7H/yLJb4c5zxl79qNxW6hlK")
				.roles("ADMIN")
				.and()
			.passwordEncoder(passwordEncoder);*/
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
		
		/*DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService);
		daoProvider.setPasswordEncoder(passwordEncoder);
		daoProvider.afterPropertiesSet();
		auth.authenticationProvider(daoProvider);*/
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest()
			.permitAll();
		this.defaultConfigure(http);
	}
	
	protected void defaultConfigure(HttpSecurity http) throws Exception {
			http
				.formLogin()
				.loginPage(securityConfig.getLoginUrl())
				.loginProcessingUrl(securityConfig.getLoginProcessUrl())
				.usernameParameter("username")
				.passwordParameter("password")
				.failureUrl(securityConfig.getLoginUrl()+"?error=true")
				.failureHandler(ajaxAuthenticationHandler)
				.successHandler(ajaxAuthenticationHandler)
			.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher(securityConfig.getLogoutUrl()))
			.and()
				.httpBasic()
				.disable()
				.csrf()
				.requireCsrfProtectionMatcher(CsrfRequestUrlMatcher.excludeUrls("/login*"))
			.and()
				.headers()
					.frameOptions()
					.sameOrigin()
					.xssProtection()
					.xssProtectionEnabled(true)
				.and()
			.and()
				.exceptionHandling()
				.accessDeniedPage("/access?error=true")
				.accessDeniedHandler(ajaxSupportedAccessDeniedHandler)
			.and()
				.rememberMe()
				.tokenValiditySeconds(securityConfig.getRememberMe().getTokenValiditySeconds())
				//must be config
				.key(securityConfig.getRememberMe().getKey());
	}

}
