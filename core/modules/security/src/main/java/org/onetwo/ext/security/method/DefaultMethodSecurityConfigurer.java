package org.onetwo.ext.security.method;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.security.ajax.AjaxAuthenticationHandler;
import org.onetwo.ext.security.ajax.AjaxLogoutSuccessHandler;
import org.onetwo.ext.security.ajax.AjaxSupportedAccessDeniedHandler;
import org.onetwo.ext.security.ajax.AjaxSupportedAuthenticationEntryPoint;
import org.onetwo.ext.security.login.LoginSecurityConfigurer;
import org.onetwo.ext.security.matcher.MatcherUtils;
import org.onetwo.ext.security.utils.IgnoreCsrfProtectionRequestUrlMatcher;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.ext.security.utils.SecurityConfig.InterceptersConfig;
import org.onetwo.ext.security.utils.SecurityConfig.StrictHttpFirewallConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import com.google.common.collect.Lists;

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
	@Autowired
	private List<LoginSecurityConfigurer> loginConfigurers;
	@Autowired(required=false)
	private List<AuthenticationProvider> authenticationProviders;
	

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
        	httpFirewall.setAllowBackSlash(strictHttpFirewall.isAllowBackSlash());
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
	

	protected void configAuthenticationProviders(HttpSecurity http) throws Exception {
		if(LangUtils.isNotEmpty(authenticationProviders)){
			authenticationProviders.forEach(authProvider->http.authenticationProvider(authProvider));
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		configAuthenticationProviders(http);
		
		http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<MethodSecurityInterceptor>() {
			@Override
			public <O extends MethodSecurityInterceptor> O postProcess(O fsi) {
				fsi.setRejectPublicInvocations(securityConfig.isRejectPublicInvocations());
				fsi.setValidateConfigAttributes(securityConfig.isValidateConfigAttributes());
				return fsi;
			}
		});
		//if basic method interceptor, ignore all url interceptor
		checkAndConfigIntercepterUrls(http);
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
	
//	@SuppressWarnings("unchecked")
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
		
		for (LoginSecurityConfigurer loginConfigurer : loginConfigurers) {
//			WebFormLoginConfigurer webFormLoginConfigurer = new WebFormLoginConfigurer();
			loginConfigurer.setAjaxAuthenticationHandler(ajaxAuthenticationHandler);
//			loginConfigurer.setAjaxLogoutSuccessHandler(ajaxLogoutSuccessHandler);
			loginConfigurer.setAjaxSupportedAccessDeniedHandler(ajaxSupportedAccessDeniedHandler);
			loginConfigurer.setAuthenticationEntryPoint(authenticationEntryPoint);
			loginConfigurer.applyConfig(http, securityConfig);
		}
		
		
		
		// 配置登录时，删除cookies
		List<String> cookieNames = Lists.newArrayList(); 
		cookieNames.add(securityConfig.getCookie().getName());
		if (securityConfig.getJwt().isEnabled() && securityConfig.getJwt().getAuthStore().isCookieStore()) {
			cookieNames.add(securityConfig.getJwt().getAuthKey());
		}

//		http.exceptionHandling()
//			.authenticationEntryPoint(authenticationEntryPoint);
		
		LogoutConfigurer<HttpSecurity> logoutConf = http.logout()
										.logoutRequestMatcher(new AntPathRequestMatcher(securityConfig.getLogoutUrl()))
										// 删除sid cookies
										.deleteCookies(cookieNames.toArray(new String[0]))
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
		configureSession(http);
	}

	protected void configureSession(HttpSecurity http) throws Exception{
		SessionCreationPolicy creationPolicy = securityConfig.getSession().getCreationPolicy();
		if (creationPolicy!=null) {
			http.sessionManagement().sessionCreationPolicy(creationPolicy);
		}
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

	protected void checkAndConfigIntercepterUrls(HttpSecurity http) throws Exception {
		// permitAll
		if (this.securityConfig.isCheckAnyUrlpermitAll()) {
			for(Entry<String[], String> entry : securityConfig.getIntercepterUrls().entrySet()) {
				if (ArrayUtils.contains(entry.getKey(), "/**") && 
						("permitAll".equals(entry.getValue()) || "authenticated".equals(entry.getValue()))
					) {
					throw new BaseException("do not config /** -> permitAll or authenticated, it's very danger!");
				}
			}
		}
		configIntercepterUrls(http, securityConfig.getIntercepterUrls(), securityConfig.getIntercepters());
	}

	public static void configIntercepterUrls(HttpSecurity http, Map<String[], String> intercepterUrls, List<InterceptersConfig> intercepters) throws Exception {
		if(LangUtils.isNotEmpty(intercepterUrls)){
			for(Entry<String[], String> entry : intercepterUrls.entrySet()){
				http.authorizeRequests().antMatchers(entry.getKey()).access(entry.getValue());
			}
		}
		
		if(LangUtils.isNotEmpty(intercepters)){
			for(InterceptersConfig interConfig : intercepters){
				http.authorizeRequests().antMatchers(interConfig.getPathPatterns()).access(interConfig.getAccess());
			}
		}
	}
}
