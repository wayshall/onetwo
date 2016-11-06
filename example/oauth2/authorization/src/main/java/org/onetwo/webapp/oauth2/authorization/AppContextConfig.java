package org.onetwo.webapp.oauth2.authorization;

import org.onetwo.boot.module.security.method.EnableJFishMethodSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableOAuth2Client
@EnableAuthorizationServer
@EnableJFishMethodSecurity
public class AppContextConfig extends WebSecurityConfigurerAdapter {


	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.antMatcher("/**")                                       
	    .authorizeRequests()
	      .antMatchers("/", "/login**", "/webjars/**").permitAll() 
	      .anyRequest().authenticated()                            
	    .and().exceptionHandling()
	      .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
	}
	
}
