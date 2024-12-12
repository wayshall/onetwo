package org.onetwo.boot.module.oauth2.ssoclient.tokeninfo;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.module.oauth2.ssoclient.EnableOauth2SsoCondition;
import org.onetwo.boot.module.oauth2.ssoclient.OAuth2SsoClientProperties;
import org.onetwo.boot.module.oauth2.ssoclient.OAuth2SsoClientProperties.TokenInfoProps;
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
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

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
	@Autowired
	private OAuth2SsoClientProperties ssoClientProperties;

	protected SsoClientCustomTokenInfoUriConfiguration(ResourceServerProperties resource) {
		this.resource = resource;
	}

	@Bean
	public ResourceServerTokenServices remoteTokenServices() {
		TokenInfoProps tokenInfo = ssoClientProperties.getTokenInfo();
		
		DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
		CustomSsoClientUserAuthenticationConverter userTokenConverter = new CustomSsoClientUserAuthenticationConverter();
		userTokenConverter.setSsoUserDetailService(userDetailsService);
		tokenConverter.setUserTokenConverter(userTokenConverter);
		
		boolean useCustomerTokenService = tokenInfo.isUseCustomMode();
		if (useCustomerTokenService) {
			SSORemoteTokenServices services = new SSORemoteTokenServices();
			services.setCheckTokenEndpointUrl(this.resource.getTokenInfoUri());
			services.setClientId(this.resource.getClientId());
			services.setClientSecret(this.resource.getClientSecret());
			services.setAccessTokenConverter(tokenConverter);
			services.setAuthorizationHeader(tokenInfo.isAuthorizationHeader());
			services.setAuthorizationHeaderScheme(tokenInfo.getAuthorizationHeaderScheme());
			if (StringUtils.isNotBlank(tokenInfo.getTokenName())) {
				services.setTokenName(tokenInfo.getTokenName());
			}
			if (StringUtils.isNotBlank(tokenInfo.getHttpMethod())) {
				services.setHttpMethod(HttpMethod.resolve(tokenInfo.getHttpMethod().toUpperCase()));
			}
			return services;
		} else {
			RemoteTokenServices services = new RemoteTokenServices();
			services.setCheckTokenEndpointUrl(this.resource.getTokenInfoUri());
			services.setClientId(this.resource.getClientId());
			services.setClientSecret(this.resource.getClientSecret());
			services.setAccessTokenConverter(tokenConverter);
			return services;
		}
	}
	

}
