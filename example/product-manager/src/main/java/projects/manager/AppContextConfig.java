package projects.manager;

import org.onetwo.boot.module.security.oauth2.UserDetailPrincipalExtractor;
import org.onetwo.boot.module.security.url.EnableUrlSecurity;
import org.onetwo.ext.security.DefaultUrlSecurityConfigurer;
import org.onetwo.plugins.admin.utils.WebAdminPermissionConfig.RootMenuClassProvider;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;

import projects.manager.utils.Products;
import projects.manager.vo.LoginUserInfo;

@Configuration
@EnableUrlSecurity
@EnableOAuth2Sso
public class AppContextConfig extends DefaultUrlSecurityConfigurer {

	public AppContextConfig(AccessDecisionManager accessDecisionManager) {
		super(accessDecisionManager);
	}
	
	@Bean
	public RootMenuClassProvider menuConfig(){
		return ()->Products.class;
	}
	
	@Bean
	public PrincipalExtractor userDetailPrincipalExtractor(){
		return new UserDetailPrincipalExtractor(LoginUserInfo.class);
	}

}
