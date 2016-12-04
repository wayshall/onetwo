package org.onetwo.webapp.manager;

import org.onetwo.boot.module.security.url.EnableOnetwoUrlSecurity;
import org.onetwo.plugins.admin.utils.WebAdminPermissionConfig.RootMenuClassProvider;
import org.onetwo.webapp.manager.utils.Apps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableOnetwoUrlSecurity
public class AppContextConfig {

	@Bean
	public RootMenuClassProvider menuConfig(){
		return ()->Apps.class;
	}
	
}
