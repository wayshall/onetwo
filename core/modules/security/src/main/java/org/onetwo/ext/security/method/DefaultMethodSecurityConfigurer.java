package org.onetwo.ext.security.method;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.security.ajax.AjaxAuthenticationHandler;
import org.onetwo.ext.security.ajax.AjaxLogoutSuccessHandler;
import org.onetwo.ext.security.ajax.AjaxSupportedAccessDeniedHandler;
import org.onetwo.ext.security.ajax.AjaxSupportedAuthenticationEntryPoint;
import org.onetwo.ext.security.config.S6MethodWebSecurityConfigurer;
import org.onetwo.ext.security.config.SecurityConfigUtils;
import org.onetwo.ext.security.login.LoginSecurityConfigurer;
import org.onetwo.ext.security.matcher.MatcherUtils;
import org.onetwo.ext.security.utils.IgnoreCsrfProtectionRequestUrlMatcher;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import com.google.common.collect.Lists;

import lombok.Getter;

@Configuration
public class DefaultMethodSecurityConfigurer implements ApplicationContextAware
//extends WebSecurityConfigurerAdapter
{
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
	@Autowired
	private List<LoginSecurityConfigurer> loginConfigurers;
	@Autowired(required=false)
	private List<AuthenticationProvider> authenticationProviders;
	private ApplicationContext applicationContext;
	

	/***
	 * 代替：configure(WebSecurity web)
	 * @return
	 */
    @Bean
    public WebSecurityCustomizer configureWebSecurity() {
        return new S6MethodWebSecurityConfigurer();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    	authenticationManagerBuilder.userDetailsService(createUserDetailsService())
    								.passwordEncoder(passwordEncoder);
    	configure(http);
//    	http.authenticationManager(null)
    	
    	return http.build();
    }
    
    
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    protected UserDetailsService createUserDetailsService() {
		if(userDetailsService!=null){
			return userDetailsService;
		} else {
			InMemoryUserDetailsManager inMemoryUser = new InMemoryUserDetailsManager();
//			InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemory = auth.apply(new ExtInMemoryUserDetailsManagerConfigurer());
			securityConfig.getMemoryUsers().forEach((user, config)->{
				UserAttribute attrs = new UserAttribute();
				attrs.setPassword(config.getPassword());
				
				if(!LangUtils.isEmpty(config.getRoles())){
//					udb.roles(config.getRoles());
					attrs.setAuthoritiesAsString(Arrays.asList(config.getRoles()));
				}
				if(!LangUtils.isEmpty(config.getAuthorities())){
//					udb.authorities(config.getAuthorities());
					attrs.setAuthoritiesAsString(Arrays.asList(config.getAuthorities()));
				}
				inMemoryUser.createUser(newUser(user, attrs));
			});
			return inMemoryUser;
//			inMemory.passwordEncoder(passwordEncoder);
		}
		
		/*DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService);
		daoProvider.setPasswordEncoder(passwordEncoder);
		daoProvider.afterPropertiesSet();
		auth.authenticationProvider(daoProvider);*/
    }
    
    private User newUser(String name, UserAttribute attr) {
    	return new User(name, attr.getPassword(), attr.isEnabled(), true, true, true, attr.getAuthorities());
    }
	

    protected void configAuthenticationProviders(HttpSecurity http) {
		if(LangUtils.isNotEmpty(authenticationProviders)){
			authenticationProviders.forEach(authProvider->http.authenticationProvider(authProvider));
		}
    }
//	@Override
	protected void configure(HttpSecurity http) throws Exception {
		configAuthenticationProviders(http);
		
//		http.authorizeHttpRequests(authz -> {
//			authz.withObjectPostProcessor(new ObjectPostProcessor<AuthorizationManagerBeforeMethodInterceptor>() {
//				@Override
//				public <O extends AuthorizationManagerBeforeMethodInterceptor> O postProcess(O before) {
//					return before;
//				}
//			});
//		});
		
		// TODO
//		http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<MethodSecurityInterceptor>() {
//			@Override
//			public <O extends MethodSecurityInterceptor> O postProcess(O fsi) {
//				fsi.setRejectPublicInvocations(securityConfig.isRejectPublicInvocations());
//				fsi.setValidateConfigAttributes(securityConfig.isValidateConfigAttributes());
//				return fsi;
//			}
//		});
		//if basic method interceptor, ignore all url interceptor
		checkAndConfigIntercepterUrls(http);
		configureAnyRequest(http);
		defaultConfigure(http);
	}
	
	protected void configureAnyRequest(HttpSecurity http) throws Exception {
		SecurityConfigUtils.defaultAnyRequest(http, securityConfig.getAnyRequest());
//		defaultAnyRequest(http, securityConfig.getAnyRequest());
	}
	
	protected void configureCsrf(HttpSecurity http) throws Exception{
//		CsrfConfigurer<HttpSecurity> csrf = http.csrf();
		if(securityConfig.getCsrf().isDisable()){
//			csrf.disable();
//			http.headers().frameOptions().disable();
			http.csrf(csrf -> {
				csrf.disable();
			});
			http.headers(header -> {
				header.disable();
			});
			return ;
		}
		
		http.csrf(csrf -> {
			if(ArrayUtils.isNotEmpty(securityConfig.getCsrf().getIgnoringPaths())){
				csrf.ignoringRequestMatchers(securityConfig.getCsrf().getIgnoringPaths());
			}

			if(ArrayUtils.isNotEmpty(securityConfig.getCsrf().getRequirePaths())){
				csrf.requireCsrfProtectionMatcher(MatcherUtils.matchAntPaths(securityConfig.getCsrf().getRequirePaths()));
			}else{
				csrf.requireCsrfProtectionMatcher(IgnoreCsrfProtectionRequestUrlMatcher.ignoreUrls("/login*"));
			}
		});
//		if(ArrayUtils.isNotEmpty(securityConfig.getCsrf().getIgnoringPaths())){
//			csrf.ignoringAntMatchers(securityConfig.getCsrf().getIgnoringPaths());
//		}
//		if(ArrayUtils.isNotEmpty(securityConfig.getCsrf().getRequirePaths())){
//			csrf.requireCsrfProtectionMatcher(MatcherUtils.matchAntPaths(securityConfig.getCsrf().getRequirePaths()));
//		}else{
//			csrf.requireCsrfProtectionMatcher(IgnoreCsrfProtectionRequestUrlMatcher.ignoreUrls("/login*"));
//		}
	}
	
//	@SuppressWarnings("unchecked")
	protected void defaultConfigure(HttpSecurity http) throws Exception {
		if(securityContextRepository!=null){
//			http.securityContext().securityContextRepository(securityContextRepository);
			http.securityContext(conf -> {
				conf.securityContextRepository(securityContextRepository);
			});
		}
//		if(logoutSuccessHandler!=null){
////			http.logout().logoutSuccessHandler(logoutSuccessHandler);
//		}
		
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

//		LogoutConfigurer<HttpSecurity> logoutConf = http.logout()
//										.logoutRequestMatcher(new AntPathRequestMatcher(securityConfig.getLogoutUrl()))
//										// 删除sid cookies
//										.deleteCookies(cookieNames.toArray(new String[0]))
//										.logoutSuccessUrl(securityConfig.getLogoutSuccessUrl()).permitAll();
//		if (ajaxLogoutSuccessHandler!=null) {
//		logoutConf.logoutSuccessHandler(ajaxLogoutSuccessHandler);
//	}
		http.logout(conf -> {
			if (ajaxLogoutSuccessHandler!=null) {
				conf.logoutSuccessHandler(ajaxLogoutSuccessHandler);
			}
			conf.logoutRequestMatcher(new AntPathRequestMatcher(securityConfig.getLogoutUrl()))
				.deleteCookies(cookieNames.toArray(new String[0])) // 删除sid cookies
				.logoutSuccessUrl(securityConfig.getLogoutSuccessUrl()).permitAll();
		});

		
		http.httpBasic(conf -> conf.disable());
		http.headers(header -> {
			// https://blog.csdn.net/zhuyiquan/article/details/52173735
			// 常浏览器会根据响应头的Content-Type字段来分辨它们的类型。例如：”text/html”代表html文档，”image/png”是PNG图片，”text/css”是CSS样式文档。然而，有些资源的Content-Type是错的或者未定义。这时，某些浏览器会启用MIME-sniffing来猜测该资源的类型，解析内容并执行。
			// 例如，我们即使给一个html文档指定Content-Type为”text/plain”，在IE8-中这个文档依然会被当做html来解析。利用浏览器的这个特性，攻击者甚至可以让原本应该解析为图片的请求被解析为JavaScript。
			// 通过下面这个响应头可以禁用浏览器的类型猜测行为：
			// X-Content-Type-Options: nosniff
			header.contentTypeOptions(conf -> conf.disable());
			header.frameOptions(conf -> conf.sameOrigin());
//			header.xssProtection();
		});
		http.exceptionHandling(exConf -> {
			exConf.accessDeniedHandler(ajaxSupportedAccessDeniedHandler);
		});
		
//		http.httpBasic()
//			.disable()
//			.headers()
//				// https://blog.csdn.net/zhuyiquan/article/details/52173735
//				// 常浏览器会根据响应头的Content-Type字段来分辨它们的类型。例如：”text/html”代表html文档，”image/png”是PNG图片，”text/css”是CSS样式文档。然而，有些资源的Content-Type是错的或者未定义。这时，某些浏览器会启用MIME-sniffing来猜测该资源的类型，解析内容并执行。
//				// 例如，我们即使给一个html文档指定Content-Type为”text/plain”，在IE8-中这个文档依然会被当做html来解析。利用浏览器的这个特性，攻击者甚至可以让原本应该解析为图片的请求被解析为JavaScript。
//				// 通过下面这个响应头可以禁用浏览器的类型猜测行为：
//				// X-Content-Type-Options: nosniff
//				.contentTypeOptions().disable()
//				.frameOptions()
//				.sameOrigin()
//				.xssProtection()
////				.xssProtectionEnabled(true)
//			.and()
//		.and()
//			.exceptionHandling()
////			.accessDeniedPage("/access?error=true")
//			.accessDeniedHandler(ajaxSupportedAccessDeniedHandler)
////			.authenticationEntryPoint(authenticationEntryPoint)
//		;
		
		if(securityConfig.getRememberMe().isEnabled()){
			http.rememberMe(rememberMe -> {
				rememberMe.tokenValiditySeconds(securityConfig.getRememberMe().getTokenValiditySeconds())
						.key(securityConfig.getRememberMe().getKey());
			});
//			http
//				.rememberMe()
//				.tokenValiditySeconds(securityConfig.getRememberMe().getTokenValiditySeconds())
//				//must be config
//				.key(securityConfig.getRememberMe().getKey());
		}
		configureCsrf(http);
		configureCors(http);
		configureSession(http);
	}

	protected void configureSession(HttpSecurity http) throws Exception{
		SessionCreationPolicy creationPolicy = securityConfig.getSession().getCreationPolicy();
		if (creationPolicy!=null) {
//			http.sessionManagement().sessionCreationPolicy(creationPolicy);
			http.sessionManagement(session -> {
				session.sessionCreationPolicy(creationPolicy);
			});
		}
	}

	protected void configureCors(HttpSecurity http) throws Exception{
		// disable只是移除cors的配置类
		if (securityConfig.getCors().isDisable()) {
//			http.cors().disable();
			http.cors(cors -> {
				cors.disable();
			});
		}
		if (securityConfig.getCors().isPermitAllPreFlightRequest()) {
//			http.authorizeRequests().requestMatchers(req -> CorsUtils.isPreFlightRequest(req)).permitAll();
			http.authorizeHttpRequests(reqConf -> {
				reqConf.requestMatchers(req -> CorsUtils.isPreFlightRequest(req)).permitAll();
			});
		}
	}

	protected void checkAndConfigIntercepterUrls(HttpSecurity http) throws Exception {
		SecurityConfigUtils.checkAndConfigIntercepterUrls(http, securityConfig, applicationContext);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
