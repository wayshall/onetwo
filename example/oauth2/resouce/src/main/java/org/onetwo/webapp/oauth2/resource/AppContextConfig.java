package org.onetwo.webapp.oauth2.resource;

import org.onetwo.boot.module.security.oauth2.OAuth2SsoClientConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Configuration
@EnableOAuth2Sso
public class AppContextConfig extends OAuth2SsoClientConfiguration {

	
	public AppContextConfig(ApplicationContext applicationContext, OAuth2SsoProperties sso) {
		super(applicationContext, sso);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.antMatcher("/**")
			.authorizeRequests()
				.antMatchers("/article**").authenticated()
			.anyRequest().permitAll();
	}

}
