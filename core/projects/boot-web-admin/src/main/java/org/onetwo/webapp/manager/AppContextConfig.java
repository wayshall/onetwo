package org.onetwo.webapp.manager;

import org.onetwo.boot.plugins.permission.web.controller.AdminController;
import org.onetwo.boot.plugins.security.method.EnableJFishMethodSecurity;
import org.onetwo.common.jfishdbm.spring.EnableJFishDbm;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.plugins.admin.WebAdminContextConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableJFishDbm
@EnableJFishMethodSecurity
@Import({WebAdminContextConfig.class})
public class AppContextConfig {

	@Bean
	public AdminController<PermisstionTreeModel> adminController(){
		return new AdminController<>();
	}
	

	/*@Bean
	@ConditionalOnMissingBean(RbacWebSecurityConfigurerAdapter.class)
	public SimpleWebSecurityConfigurerAdapter simpleWebSecurityConfigurerAdapter(){
		return new SimpleWebSecurityConfigurerAdapter();
	}*/
	
}
