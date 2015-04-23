package org.onetwo.plugins.admin.model;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.sso.SecurityService;
import org.onetwo.plugins.admin.AdminConfigInitializer;
import org.onetwo.plugins.admin.model.app.service.impl.DefaultNotSSOServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminModelContext  implements InitializingBean {


	@Resource
	private ApplicationContext applicationContext;
	@Bean
	public AdminConfigInitializer adminConfigInitializer(){
		return new AdminConfigInitializer();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(SpringUtils.getBeans(applicationContext, SecurityService.class).isEmpty()){
			SpringUtils.registerBean(applicationContext, "defaultNotSSOServiceImpl", DefaultNotSSOServiceImpl.class);
		}
	}

	
}
