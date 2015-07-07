package org.onetwo.boot.plugins.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;

public class RbacWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter  {

	/*@Autowired
	private FilterInvocationSecurityMetadataSource securityMetadataSource;*/
	@Autowired(required=false)
	private DatabaseSecurityMetadataSource databaseSecurityMetadataSource;

	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		.jdbcAuthentication().authoritiesByUsernameQuery(query)
//		.authenticationProvider(authenticationProvider)
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService);
		daoProvider.afterPropertiesSet();
		auth.authenticationProvider(daoProvider);
    }

	@Override
    public void configure(WebSecurity web) throws Exception {
	    super.configure(web);
    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		//DefaultFilterInvocationSecurityMetadataSource
		AjaxAuthenticationHandler authHandler = new AjaxAuthenticationHandler("/login", "/plugins/permission/admin");
	    http
	    	.headers()
				.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
				.and()
	    	.authorizeRequests()
	    		.anyRequest().authenticated()
	    		.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

					@Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//						object.setRejectPublicInvocations(true);
						/*if(securityMetadataSource!=null){
							object.setSecurityMetadataSource(securityMetadataSource);
						}*/
						if(databaseSecurityMetadataSource!=null){
							object.setSecurityMetadataSource(databaseSecurityMetadataSource.convertTo(object.getSecurityMetadataSource()));
						}
	                    return object;
                    }
	    			
				})
				.and()
			.formLogin()
	    		.loginPage("/login")
	    		.loginProcessingUrl("/dologin")
				.successHandler(authHandler)
				.failureHandler(authHandler)
	    		.and()
	    	.logout()
	    		.deleteCookies("JSESSIONID")
	    		.invalidateHttpSession(true)
	    		.and()
	    	.sessionManagement()
	    		.maximumSessions(1)
	    		.maxSessionsPreventsLogin(true);
//	    		.failureUrl("/login?loginError=1")
	    	;
    }
	
	

}
