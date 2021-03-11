package org.onetwo.boot.module.oauth2.ssoclient.tokeninfo;

import org.onetwo.boot.module.oauth2.ssoclient.EnableOauth2SsoCondition;
import org.onetwo.boot.module.oauth2.ssoclient.OAuth2SsoClientProperties;
import org.onetwo.common.apiclient.impl.RestExecutorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@ConditionalOnClass(EnableOAuth2Sso.class)
@Conditional(EnableOauth2SsoCondition.class)
@ConditionalOnProperty(OAuth2SsoClientProperties.ENABLED_TOKEN_INFO_CUSTOM)
@Import(RestExecutorConfiguration.class)
@EnableConfigurationProperties(OAuth2SsoClientProperties.class)
public class SsoClientCustomTokenInfoUriConfiguration {
	
	private final ResourceServerProperties resource;
	@Autowired
	private SSoUserDetailsService userDetailsService;

	protected SsoClientCustomTokenInfoUriConfiguration(ResourceServerProperties resource) {
		this.resource = resource;
	}

	@Bean
	public RemoteTokenServices remoteTokenServices() {
		DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
		CustomSsoClientUserAuthenticationConverter userTokenConverter = new CustomSsoClientUserAuthenticationConverter();
		userTokenConverter.setSsoUserDetailService(userDetailsService);
//		userTokenConverter.setUserDetailsService(new UserDetailsService() {
//			@Override
//			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//				return userDetailsService.loadUserByUsername(username);
//			}
//		});
		tokenConverter.setUserTokenConverter(userTokenConverter);
		
		RemoteTokenServices services = new RemoteTokenServices();
		services.setCheckTokenEndpointUrl(this.resource.getTokenInfoUri());
		services.setClientId(this.resource.getClientId());
		services.setClientSecret(this.resource.getClientSecret());
		services.setAccessTokenConverter(tokenConverter);
		return services;
	}
	

}
