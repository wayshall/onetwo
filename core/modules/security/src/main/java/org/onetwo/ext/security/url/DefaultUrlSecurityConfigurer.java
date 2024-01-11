package org.onetwo.ext.security.url;

import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class DefaultUrlSecurityConfigurer extends DefaultMethodSecurityConfigurer {
	@Autowired(required = false)
	private DatabaseAuthorizationManager databaseAuthorizationManager;
	
	public DefaultUrlSecurityConfigurer() {
	}

	protected void configure(HttpSecurity http) throws Exception {
		configAuthenticationProviders(http);
		
//		http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//			@Override
//			public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
//				fsi.setRejectPublicInvocations(securityConfig.isRejectPublicInvocations());
//				fsi.setValidateConfigAttributes(securityConfig.isValidateConfigAttributes());
//				if(securityMetadataSourceBuilder!=null){
//					securityMetadataSourceBuilder.setFilterSecurityInterceptor(fsi);
//					securityMetadataSourceBuilder.buildSecurityMetadataSource();
//				}
//				return fsi;
//			}
//		});
		
		/*for(Entry<String[], String> entry : this.securityConfig.getIntercepterUrls().entrySet()){
			http.authorizeRequests().antMatchers(entry.getKey()).access(entry.getValue());
		}
		
		for(InterceptersConfig interConfig : this.securityConfig.getIntercepters()){
			http.authorizeRequests().antMatchers(interConfig.getPathPatterns()).access(interConfig.getAccess());
		}*/
		
//		configureCors(http);

		webConfigure(http);
		checkAndConfigIntercepterUrls(http);

//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		configureAnyRequest(http);
		
//		webConfigure(http);
		defaultConfigure(http);
	}
	
	
	/****
	 * 属于url特有的配置部分在这里实现
	 * @author wayshall
	 * @param http
	 * @throws Exception
	 */
	protected void webConfigure(HttpSecurity http) throws Exception {
		if (databaseAuthorizationManager!=null) {
			http.authorizeHttpRequests(authz -> {
				authz.anyRequest().access(databaseAuthorizationManager);
			});
		}
//		http.authorizeRequests()
//			.accessDecisionManager(accessDecisionManager);
	}
}
