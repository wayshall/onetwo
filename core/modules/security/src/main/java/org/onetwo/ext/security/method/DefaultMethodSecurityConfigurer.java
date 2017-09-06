package org.onetwo.ext.security.method;

import lombok.Getter;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.ext.security.ajax.AjaxAuthenticationHandler;
import org.onetwo.ext.security.matcher.MatcherUtils;
import org.onetwo.ext.security.utils.IgnoreCsrfProtectionRequestUrlMatcher;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class DefaultMethodSecurityConfigurer extends WebSecurityConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Getter
	@Autowired
	private AjaxAuthenticationHandler ajaxAuthenticationHandler;

	@Getter
	@Autowired
	private AccessDeniedHandler ajaxSupportedAccessDeniedHandler;
	
	@Autowired(required=false)
	private AuthenticationEntryPoint authenticationEntryPoint;
	

	@Getter
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Getter
	@Autowired(required=false)
	private UserDetailsService userDetailsService;

	@Getter
	@Autowired
	protected SecurityConfig securityConfig;
	
	//redis
	@Autowired(required=false)
	private SecurityContextRepository securityContextRepository;
	@Autowired(required=false)
	private LogoutSuccessHandler logoutSuccessHandler;
	

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/webjars/**", "/images/**", "/oauth/uncache_approvals", "/oauth/cache_approvals");
        web.ignoring().antMatchers("/webjars/**", "/images/**", "/static/**");
    }
    
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if(userDetailsService==null){
			logger.warn("no userDetailsService found!");
		}
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
		//if basic method interceptor, ignore all url interceptor
		http.authorizeRequests()
			.anyRequest()
			.permitAll();
		this.defaultConfigure(http);
	}
	
	protected void configureCsrf(HttpSecurity http) throws Exception{
		CsrfConfigurer<HttpSecurity> csrf = http.csrf();
		if(securityConfig.getCsrf().isDisable()){
			csrf.disable();
			return ;
		}
		if(ArrayUtils.isNotEmpty(securityConfig.getCsrf().getIgnoringPaths())){
			csrf.ignoringAntMatchers(securityConfig.getCsrf().getIgnoringPaths());
		}
		if(ArrayUtils.isNotEmpty(securityConfig.getCsrf().getRequirePaths())){
			csrf.requireCsrfProtectionMatcher(MatcherUtils.matchAntPaths(securityConfig.getCsrf().getRequirePaths()));
		}else{
			csrf.requireCsrfProtectionMatcher(IgnoreCsrfProtectionRequestUrlMatcher.ignoreUrls("/login*"));
		}
	}
	
	protected void defaultConfigure(HttpSecurity http) throws Exception {
		if(securityContextRepository!=null){
			http.securityContext().securityContextRepository(securityContextRepository);
		}
		if(logoutSuccessHandler!=null){
			http.logout().logoutSuccessHandler(logoutSuccessHandler);
		}
		http
			.formLogin()
			.loginPage(securityConfig.getLoginUrl()).permitAll()
			.loginProcessingUrl(securityConfig.getLoginProcessUrl()).permitAll()
			.usernameParameter("username")
			.passwordParameter("password")
			.failureUrl(securityConfig.getLoginUrl()+"?error=true")
			.failureHandler(ajaxAuthenticationHandler)
			.successHandler(ajaxAuthenticationHandler)
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher(securityConfig.getLogoutUrl()))
			.logoutSuccessUrl(securityConfig.getLogoutSuccessUrl()).permitAll()
		.and()
			.httpBasic()
			.disable()
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
			.authenticationEntryPoint(authenticationEntryPoint)
		;
		
		if(securityConfig.getRememberMe().isEnabled()){
			http
				.rememberMe()
				.tokenValiditySeconds(securityConfig.getRememberMe().getTokenValiditySeconds())
				//must be config
				.key(securityConfig.getRememberMe().getKey());
		}
		configureCsrf(http);
	}

}
