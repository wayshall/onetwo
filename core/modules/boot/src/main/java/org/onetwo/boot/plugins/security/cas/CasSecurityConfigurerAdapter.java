package org.onetwo.boot.plugins.security.cas;

import org.onetwo.boot.plugins.security.DatabaseSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;

public class CasSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter  {

	/*@Autowired
	private FilterInvocationSecurityMetadataSource securityMetadataSource;*/
	@Autowired(required=false)
	private DatabaseSecurityMetadataSource databaseSecurityMetadataSource;

	
//	@Autowired
//	private UserDetailsService userDetailsService;
	
	@Autowired
	private CasAuthenticationFilter casFilter;
	
	@Autowired
	private CasAuthenticationProvider casAuthenticationProvider;
	@Autowired
	private CasAuthenticationEntryPoint casEntryPoint;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		.jdbcAuthentication().authoritiesByUsernameQuery(query)
//		.authenticationProvider(authenticationProvider)
		/*DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService);
		daoProvider.afterPropertiesSet();
		auth.authenticationProvider(daoProvider);*/
		auth.authenticationProvider(casAuthenticationProvider);
    }

	@Override
    public void configure(WebSecurity web) throws Exception {
	    super.configure(web);
    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		//DefaultFilterInvocationSecurityMetadataSource
//		AjaxAuthenticationHandler authHandler = new AjaxAuthenticationHandler("/login", "/plugins/permission/admin");

		casFilter.setAuthenticationManager(authenticationManager());
	    http
	    	.headers()
				.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
			.and()
			.exceptionHandling()
				.authenticationEntryPoint(casEntryPoint)
			.and()
//			.authenticationProvider(casAuthenticationProvider)
			.addFilter(casFilter)
	    	.authorizeRequests()
	    		.anyRequest().authenticated()//去掉会启动失败，原因未知
	    		.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

					@Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
						if(databaseSecurityMetadataSource!=null){
							object.setSecurityMetadataSource(databaseSecurityMetadataSource.convertTo(object.getSecurityMetadataSource()));
						}
	                    return object;
                    }
	    			
				})
			.and()
	    	.sessionManagement()
	    		.maximumSessions(1)
	    		.maxSessionsPreventsLogin(true);
    }
	
	

}
