package com.yooyo.zhiyetong;

import org.onetwo.boot.plugins.permission.web.controller.AdminController;
import org.onetwo.common.jfishdbm.spring.EnableJFishDbm;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableJFishDbm
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
