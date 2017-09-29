package org.onetwo.ext.security;

import java.util.Map.Entry;

import org.onetwo.ext.security.metadata.SecurityMetadataSourceBuilder;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

public class DefaultUrlSecurityConfigurer extends DefaultMethodSecurityConfigurer {

	@Autowired(required=false)
	private SecurityMetadataSourceBuilder securityMetadataSourceBuilder;
	
	private AccessDecisionManager accessDecisionManager;
	
	
	public DefaultUrlSecurityConfigurer(AccessDecisionManager accessDecisionManager) {
		super();
		this.accessDecisionManager = accessDecisionManager;
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
			@Override
			public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
				if(securityMetadataSourceBuilder!=null){
					securityMetadataSourceBuilder.setFilterSecurityInterceptor(fsi);
					securityMetadataSourceBuilder.buildSecurityMetadataSource();
				}
				return fsi;
			}
		});
		
		for(Entry<String[], String> entry : this.securityConfig.getIntercepterUrls().entrySet()){
			http.authorizeRequests().antMatchers(entry.getKey()).access(entry.getValue());
		}

//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		configureAnyRequest(http);
		
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
