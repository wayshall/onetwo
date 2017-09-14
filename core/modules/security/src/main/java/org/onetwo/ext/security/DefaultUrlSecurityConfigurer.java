package org.onetwo.ext.security;

import java.util.Map.Entry;

import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

public class DefaultUrlSecurityConfigurer extends DefaultMethodSecurityConfigurer {

	@Autowired
	private DatabaseSecurityMetadataSource databaseSecurityMetadataSource;
	
	private AccessDecisionManager accessDecisionManager;
	
	
	public DefaultUrlSecurityConfigurer(AccessDecisionManager accessDecisionManager) {
		super();
		this.accessDecisionManager = accessDecisionManager;
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
			@Override
			public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
				databaseSecurityMetadataSource.setFilterSecurityInterceptor(fsi);
				databaseSecurityMetadataSource.buildSecurityMetadataSource();
				return fsi;
			}
		});
		
		for(Entry<String[], String> entry : this.securityConfig.getIntercepterUrls().entrySet()){
			http.authorizeRequests().antMatchers(entry.getKey()).access(entry.getValue());
		}

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		//其它未标记管理的功能的默认权限
		http.authorizeRequests()
			.anyRequest()
			.authenticated()//需要登录
	//		.fullyAuthenticated()//需要登录，并且不是rememberme的方式登录
			;
		
		webConfigure(http);
		defaultConfigure(http);
	}

	protected void webConfigure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.accessDecisionManager(accessDecisionManager)
			/*.antMatchers(getSecurityConfig().getLoginUrl(), 
							getSecurityConfig().getLoginProcessUrl(), 
							getSecurityConfig().getLogoutUrl())
			.permitAll()
			.anyRequest()
			.fullyAuthenticated()*/;
	}
}
