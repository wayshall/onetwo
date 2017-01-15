package org.onetwo.webapp.manager;

import org.onetwo.boot.module.security.url.EnableUrlSecurity;
import org.onetwo.plugins.admin.utils.WebAdminPermissionConfig.RootMenuClassProvider;
import org.onetwo.webapp.manager.utils.Systems;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration
@EnableUrlSecurity
@EnableAuthorizationServer
//@EnableUserInfoResource
public class AppContextConfig  {

	@Bean
	public RootMenuClassProvider menuConfig(){
		return ()->Systems.class;
	}

}
