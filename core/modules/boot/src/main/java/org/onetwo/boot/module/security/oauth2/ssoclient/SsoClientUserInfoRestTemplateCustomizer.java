package org.onetwo.boot.module.security.oauth2.ssoclient;

import java.util.Arrays;

import org.onetwo.common.apiclient.impl.DefaultRestExecutorFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.RequestEnhancer;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.util.MultiValueMap;

/**
 * DefaultUserInfoRestTemplateFactory#getUserInfoRestTemplate
 * @author weishao zeng
 * <br/>
 */
public class SsoClientUserInfoRestTemplateCustomizer implements UserInfoRestTemplateCustomizer {

	private OAuth2SsoClientProperties properties;
	
	public SsoClientUserInfoRestTemplateCustomizer(OAuth2SsoClientProperties properties) {
		super();
		this.properties = properties;
	}

	@Override
	public void customize(OAuth2RestTemplate template) {
//		ConfigurablePropertyAccessor accessor = SpringUtils.newPropertyAccessor(template, true);
//		accessor.getPropertyValue("accessTokenProvider");
		AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider();
		accessTokenProvider.setTokenRequestEnhancer(new AcceptJsonRequestEnhancer());
		accessTokenProvider.setStateMandatory(properties.isStateMandatory());
		template.setAccessTokenProvider(accessTokenProvider);
		
		ClientHttpRequestFactory requestFactory = DefaultRestExecutorFactory.createClientHttpRequestFactory(properties.getResttemplate());
		template.setRequestFactory(requestFactory);
		
		accessTokenProvider.setRequestFactory(requestFactory);
		
	}

	static class AcceptJsonRequestEnhancer implements RequestEnhancer {

		@Override
		public void enhance(AccessTokenRequest request,
				OAuth2ProtectedResourceDetails resource,
				MultiValueMap<String, String> form, HttpHeaders headers) {
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		}

	}
	
}
