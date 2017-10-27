package org.onetwo.ext.security.method;

import lombok.Getter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.security.ajax.AjaxAuthenticationHandler;
import org.onetwo.ext.security.matcher.MatcherUtils;
import org.onetwo.ext.security.utils.IgnoreCsrfProtectionRequestUrlMatcher;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



public class DefaultMethodSecurityConfigurer extends WebSecurityConfigurerAdapter {
//	private Logger logger = LoggerFactory.getLogger(this.getClass());

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
    	if(securityConfig.isIgnoringDefautStaticPaths()){
    		web.ignoring().antMatchers("/webjars/**", "/images/**", "/static/**");
    	}
    	if(!LangUtils.isEmpty(securityConfig.getIgnoringUrls())){
    		web.ignoring().antMatchers(securityConfig.getIgnoringUrls());
    	}
    }
    
	@SuppressWarnings("rawtypes")
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if(userDetailsService!=null){
			auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder);
		}else{
			InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemory = auth.inMemoryAuthentication();
			securityConfig.getMemoryUsers().forEach((user, config)->{
				UserDetailsBuilder udb = inMemory.withUser(user).password(config.getPassword());
				if(!LangUtils.isEmpty(config.getRoles())){
					udb.roles(config.getRoles());
				}
				if(!LangUtils.isEmpty(config.getAuthorities())){
					udb.authorities(config.getAuthorities());
				}
			});
			inMemory.passwordEncoder(passwordEncoder);
		}
		
		/*DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService);
		daoProvider.setPasswordEncoder(passwordEncoder);
		daoProvider.afterPropertiesSet();
		auth.authenticationProvider(daoProvider);*/
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//if basic method interceptor, ignore all url interceptor
		configureAnyRequest(http);
		defaultConfigure(http);
	}
	

	protected void configureAnyRequest(HttpSecurity http) throws Exception {
		defaultAnyRequest(http, securityConfig.getAnyRequest());
	}
	
	public static void defaultAnyRequest(HttpSecurity http, String anyRequest) throws Exception {
		//其它未标记管理的功能的默认权限
		if(StringUtils.isBlank(anyRequest)){
			http.authorizeRequests()
				.anyRequest()
				.authenticated()//需要登录
				//		.fullyAuthenticated()//需要登录，并且不是rememberme的方式登录
				;
		}else if(SecurityConfig.ANY_REQUEST_NONE.equals(anyRequest)){
			//not config anyRequest
		}else{
			http.authorizeRequests()
				.anyRequest()
				.access(anyRequest);
		}
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
	
	@SuppressWarnings("unchecked")
	protected void defaultConfigure(HttpSecurity http) throws Exception {
		if(securityContextRepository!=null){
			http.securityContext().securityContextRepository(securityContextRepository);
		}
		if(logoutSuccessHandler!=null){
			http.logout().logoutSuccessHandler(logoutSuccessHandler);
		}
		
		/*http
		.formLogin()
		.loginPage(securityConfig.getLoginUrl()).permitAll()
		.loginProcessingUrl(securityConfig.getLoginProcessUrl()).permitAll()
		.usernameParameter("username")
		.passwordParameter("password")
		.failureUrl(securityConfig.getLoginUrl()+"?error=true")
		.failureHandler(ajaxAuthenticationHandler)
		.successHandler(ajaxAuthenticationHandler);
		*/
		
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
					return filter;
				}
			});
			http.getConfigurer(ExceptionHandlingConfigurer.class).withObjectPostProcessor(new ObjectPostProcessor<ExceptionTranslationFilter>(){
				@Override
				public <O extends ExceptionTranslationFilter> O postProcess(O filter) {
					PropertyAccessorFactory.forDirectFieldAccess(filter).setPropertyValue("authenticationEntryPoint", authenticationEntryPoint);
					return filter;
				}
			});
			
			/*formConfig.loginPage(securityConfig.getLoginUrl())
						.permitAll();
			PropertyAccessorFactory.forDirectFieldAccess(formConfig)
									.setPropertyValue("customLoginPage", false);*/
		}else{
			formConfig.loginPage(securityConfig.getLoginUrl())
						.permitAll();
			http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint);
		}
		formConfig.loginProcessingUrl(securityConfig.getLoginProcessUrl()).permitAll()
					.usernameParameter("username")
					.passwordParameter("password")
					.failureUrl(securityConfig.getFailureUrl())
					.failureHandler(ajaxAuthenticationHandler)
					.successHandler(ajaxAuthenticationHandler);
		
		http
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
//			.accessDeniedPage("/access?error=true")
			.accessDeniedHandler(ajaxSupportedAccessDeniedHandler)
//			.authenticationEntryPoint(authenticationEntryPoint)
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
