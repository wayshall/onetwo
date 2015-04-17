package org.onetwo.plugins.admin;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.plugins.admin.model.menu.service.MenuItemRegistry;
import org.onetwo.plugins.admin.model.menu.service.impl.MenuItemServiceImpl;
import org.onetwo.plugins.admin.web.controller.AdminController;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminIndexWebContext implements InitializingBean {
	
	@Resource
	private ApplicationContext applicationContext;

	@Override
	public void afterPropertiesSet() throws Exception {
		if(SpringUtils.getBeans(applicationContext, MenuItemRegistry.class).isEmpty()){
			SpringUtils.registerBean(applicationContext, "menuItemRegistry", MenuItemServiceImpl.class);
		}
	}

	@Bean
	public AdminController adminController(){
		return new AdminController();
	}
	
	/*@Bean
	public MenuItemRegistry menuItemServiceImpl(){
		return new MenuItemServiceImpl();
	}*/
}
