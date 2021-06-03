package org.onetwo.ext.security;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.security.metadata.SecurityMetadataSourceBuilder;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.onetwo.ext.security.utils.SecurityConfig.InterceptersConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.cors.CorsUtils;

public class DefaultUrlSecurityConfigurer extends DefaultMethodSecurityConfigurer {

	@Autowired(required=false)
	private SecurityMetadataSourceBuilder securityMetadataSourceBuilder;
	
	private AccessDecisionManager accessDecisionManager;
	
	@Autowired(required=false)
	private List<AuthenticationProvider> authenticationProviders;
	
	
	public DefaultUrlSecurityConfigurer(AccessDecisionManager accessDecisionManager) {
		super();
		this.accessDecisionManager = accessDecisionManager;
	}

	protected void configure(HttpSecurity http) throws Exception {
		if(LangUtils.isNotEmpty(authenticationProviders)){
			authenticationProviders.forEach(authProvider->http.authenticationProvider(authProvider));
		}
		http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
			@Override
			public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
				fsi.setRejectPublicInvocations(securityConfig.isRejectPublicInvocations());
				if(securityMetadataSourceBuilder!=null){
					securityMetadataSourceBuilder.setFilterSecurityInterceptor(fsi);
					securityMetadataSourceBuilder.buildSecurityMetadataSource();
				}
				return fsi;
			}
		});
		
		/*for(Entry<String[], String> entry : this.securityConfig.getIntercepterUrls().entrySet()){
			http.authorizeRequests().antMatchers(entry.getKey()).access(entry.getValue());
		}
		
		for(InterceptersConfig interConfig : this.securityConfig.getIntercepters()){
			http.authorizeRequests().antMatchers(interConfig.getPathPatterns()).access(interConfig.getAccess());
		}*/
		if (securityConfig.getCors().isPermitAllPreFlightRequest()) {
			http.authorizeRequests().requestMatchers(req -> CorsUtils.isPreFlightRequest(req)).permitAll();
		}
		// permitAll
		if (this.securityConfig.isCheckAnyUrlpermitAll()) {
			for(Entry<String[], String> entry : securityConfig.getIntercepterUrls().entrySet()) {
				if (ArrayUtils.contains(entry.getKey(), "/**") && 
						("permitAll".equals(entry.getValue()) || "authenticated".equals(entry.getValue()))
					) {
					throw new BaseException("do not config /** -> permitAll or authenticated, it's very danger!");
				}
			}
		}
		configIntercepterUrls(http, securityConfig.getIntercepterUrls(), securityConfig.getIntercepters());

//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		configureAnyRequest(http);
		
		webConfigure(http);
		defaultConfigure(http);
	}
	
	public static void configIntercepterUrls(HttpSecurity http, Map<String[], String> intercepterUrls, List<InterceptersConfig> intercepters) throws Exception {
		if(LangUtils.isNotEmpty(intercepterUrls)){
			for(Entry<String[], String> entry : intercepterUrls.entrySet()){
				http.authorizeRequests().antMatchers(entry.getKey()).access(entry.getValue());
			}
		}
		
		if(LangUtils.isNotEmpty(intercepters)){
			for(InterceptersConfig interConfig : intercepters){
				http.authorizeRequests().antMatchers(interConfig.getPathPatterns()).access(interConfig.getAccess());
			}
		}
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
