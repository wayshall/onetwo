package projects.manager;

import org.onetwo.boot.module.security.url.EnableUrlSecurity;
import org.onetwo.plugins.admin.utils.WebAdminPermissionConfig.RootMenuClassProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import projects.manager.utils.Products;

@Configuration
@EnableUrlSecurity
public class AppContextConfig {

	@Bean
	public RootMenuClassProvider menuConfig(){
		return ()->Products.class;
	}
}
