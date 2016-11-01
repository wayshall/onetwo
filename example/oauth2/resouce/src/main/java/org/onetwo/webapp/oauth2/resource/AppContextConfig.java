package org.onetwo.webapp.oauth2.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableResourceServer
public class AppContextConfig {


	/*@Bean
	@ConditionalOnMissingBean(RbacWebSecurityConfigurerAdapter.class)
	public SimpleWebSecurityConfigurerAdapter simpleWebSecurityConfigurerAdapter(){
		return new SimpleWebSecurityConfigurerAdapter();
	}*/

}
