package org.onetwo.boot.module.oauth2.resouce;

import static org.onetwo.ext.security.DefaultUrlSecurityConfigurer.configIntercepterUrls;
import static org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer.defaultAnyRequest;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties;
import org.onetwo.boot.module.oauth2.JFishOauth2Properties.ResourceServerProps;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableResourceServer
@EnableConfigurationProperties(JFishOauth2Properties.class)
@Configuration
@ConditionalOnProperty(name=ResourceServerProps.ENABLED_KEY, matchIfMissing=true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private JFishOauth2Properties oauth2Properties;
	@Autowired(required=false)
	private TokenStore tokenStore;
	
	//for error

	@Autowired(required=false)
	private OAuth2ExceptionRenderer oauth2ExceptionRenderer;
	@Autowired(required=false)
	private OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint;
	@Autowired(required=false)
	private OAuth2AccessDeniedHandler oauth2AccessDeniedHandler;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		ResourceServerProps resourceServerProps = oauth2Properties.getResourceServer();
		if(!LangUtils.isEmpty(resourceServerProps.getRequestMatchers())){
			http.requestMatchers()
				.antMatchers(resourceServerProps.getRequestMatchers());
		}
		/*for(Entry<String[], String> entry : resourceServerProps.getIntercepterUrls().entrySet()){
			http.authorizeRequests().antMatchers(entry.getKey()).access(entry.getValue());
		}*/
		configIntercepterUrls(http, resourceServerProps.getIntercepterUrls(), null);
		defaultAnyRequest(http, resourceServerProps.getAnyRequest());
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		if(tokenStore!=null){
			resources.tokenStore(tokenStore);
		}
		String resourceId = oauth2Properties.getResourceServer().getResourceId();
		if(resourceId!=null){
			resources.resourceId(resourceId);//see OAuth2AuthenticationProcessingFilter#doFilter -> OAuth2AuthenticationManager#authenticate
		}
		if(oauth2AuthenticationEntryPoint!=null){
			resources.authenticationEntryPoint(oauth2AuthenticationEntryPoint);
		}
		if(oauth2AccessDeniedHandler!=null){
			resources.accessDeniedHandler(oauth2AccessDeniedHandler);
		}
	}
	
}
