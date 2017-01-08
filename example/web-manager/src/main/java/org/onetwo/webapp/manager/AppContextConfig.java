package org.onetwo.webapp.manager;

import org.onetwo.boot.module.security.url.EnableUrlSecurity;
import org.onetwo.plugins.admin.utils.WebAdminPermissionConfig.RootMenuClassProvider;
import org.onetwo.webapp.manager.utils.Systems;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableUrlSecurity
@EnableAuthorizationServer
@EnableResourceServer
public class AppContextConfig extends ResourceServerConfigurerAdapter {

	@Bean
	public RootMenuClassProvider menuConfig(){
		return ()->Systems.class;
	}

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/web-admin/me")
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/web-admin/me").access("#oauth2.hasScope('read')");
    }
}
