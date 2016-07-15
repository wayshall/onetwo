package org.onetwo.webapp.manager;

import org.onetwo.boot.plugins.security.method.EnableJFishMethodSecurity;
import org.onetwo.common.jfishdbm.spring.EnableJFishDbm;
import org.onetwo.plugins.admin.WebAdminPluginContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableJFishDbm
@EnableJFishMethodSecurity
@Import({WebAdminPluginContext.class})
public class AppContextConfig {


	/*@Bean
	@ConditionalOnMissingBean(RbacWebSecurityConfigurerAdapter.class)
	public SimpleWebSecurityConfigurerAdapter simpleWebSecurityConfigurerAdapter(){
		return new SimpleWebSecurityConfigurerAdapter();
	}*/
	
}
