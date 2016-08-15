package org.onetwo.webapp.manager;

import org.onetwo.boot.plugins.security.method.EnableJFishMethodSecurity;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableJFishMethodSecurity
public class AppContextConfig {


	/*@Bean
	@ConditionalOnMissingBean(RbacWebSecurityConfigurerAdapter.class)
	public SimpleWebSecurityConfigurerAdapter simpleWebSecurityConfigurerAdapter(){
		return new SimpleWebSecurityConfigurerAdapter();
	}*/

}
