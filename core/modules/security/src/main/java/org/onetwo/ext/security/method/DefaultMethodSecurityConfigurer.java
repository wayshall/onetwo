package org.onetwo.ext.security.method;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.security.ajax.AjaxAuthenticationHandler;
import org.onetwo.ext.security.ajax.AjaxLogoutSuccessHandler;
import org.onetwo.ext.security.ajax.AjaxSupportedAccessDeniedHandler;
import org.onetwo.ext.security.ajax.AjaxSupportedAuthenticationEntryPoint;
import org.onetwo.ext.security.matcher.MatcherUtils;
import org.onetwo.ext.security.utils.IgnoreCsrfProtectionRequestUrlMatcher;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.ext.security.utils.SecurityConfig.StrictHttpFirewallConfig;
import org.onetwo.ext.security.utils.SimpleThrowableAnalyzer;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
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
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import lombok.Getter;



public class DefaultMethodSecurityConfigurer extends WebSecurityConfigurerAdapter {
//	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Getter
	@Autowired
	private AjaxAuthenticationHandler ajaxAuthenticationHandler;

	@Getter
	@Autowired
	private AjaxSupportedAccessDeniedHandler ajaxSupportedAccessDeniedHandler;
	
	@Autowired(required=false)
	private AjaxSupportedAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired(required=false)
	private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;
	

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
    	//= new DefaultSecurityFilterChain(ignoredRequest) see: WebSecurity#performBuild
    	if(securityConfig.isIgnoringDefautStaticPaths()){
    		web.ignoring().antMatchers("/webjars/**", "/images/**", "/static/**");
    	}
    	if(!LangUtils.isEmpty(securityConfig.getIgnoringUrls())){
    		web.ignoring().antMatchers(securityConfig.getIgnoringUrls());
    	}
    	web.debug(securityConfig.isDebug()); 
    	
    	StrictHttpFirewallConfig strictHttpFirewall = securityConfig.getStrictHttpFirewall();
    	if (strictHttpFirewall!=null) {
        	StrictHttpFirewall httpFirewall = new StrictHttpFirewall();
        	/****
        	 * 是否允许url路径带有分号
        	 */
        	httpFirewall.setAllowSemicolon(strictHttpFirewall.isAllowSemicolon());
        	web.httpFirewall(httpFirewall);
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
//			InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemory = auth.apply(new ExtInMemoryUserDetailsManagerConfigurer());
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
		http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<MethodSecurityInterceptor>() {
			@Override
			public <O extends MethodSecurityInterceptor> O postProcess(O fsi) {
				fsi.setRejectPublicInvocations(securityConfig.isRejectPublicInvocations());
				fsi.setValidateConfigAttributes(securityConfig.isValidateConfigAttributes());
				return fsi;
			}
		});
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
			http.headers().frameOptions().disable();
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
					ConfigurablePropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(filter);
					accessor.setPropertyValue("authenticationEntryPoint", authenticationEntryPoint);
					// throwableAnalyzer
//					ThrowableAnalyzer analyzer = (ThrowableAnalyzer)accessor.getPropertyValue("throwableAnalyzer");
					if(securityConfig.isDebug()){
						SimpleThrowableAnalyzer analyzer = new SimpleThrowableAnalyzer();
						filter.setThrowableAnalyzer(analyzer);
					}
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
		
		LogoutConfigurer<HttpSecurity> logoutConf = http.logout()
										.logoutRequestMatcher(new AntPathRequestMatcher(securityConfig.getLogoutUrl()))
										.logoutSuccessUrl(securityConfig.getLogoutSuccessUrl()).permitAll();

		if (ajaxLogoutSuccessHandler!=null) {
			logoutConf.logoutSuccessHandler(ajaxLogoutSuccessHandler);
		}
		
		http.httpBasic()
			.disable()
			.headers()
				// https://blog.csdn.net/zhuyiquan/article/details/52173735
				// 常浏览器会根据响应头的Content-Type字段来分辨它们的类型。例如：”text/html”代表html文档，”image/png”是PNG图片，”text/css”是CSS样式文档。然而，有些资源的Content-Type是错的或者未定义。这时，某些浏览器会启用MIME-sniffing来猜测该资源的类型，解析内容并执行。
				// 例如，我们即使给一个html文档指定Content-Type为”text/plain”，在IE8-中这个文档依然会被当做html来解析。利用浏览器的这个特性，攻击者甚至可以让原本应该解析为图片的请求被解析为JavaScript。
				// 通过下面这个响应头可以禁用浏览器的类型猜测行为：
				// X-Content-Type-Options: nosniff
				.contentTypeOptions().disable()
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
		configureCors(http);
	}


	protected void configureCors(HttpSecurity http) throws Exception{
		// disable只是移除cors的配置类
		if (securityConfig.getCors().isDisable()) {
			http.cors().disable();
		}
		if (securityConfig.getCors().isPermitAllPreFlightRequest()) {
			http.authorizeRequests().requestMatchers(req -> CorsUtils.isPreFlightRequest(req)).permitAll();
		}
	}
}
