package org.onetwo.webapp.oauth2.resource;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoDefaultConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;


@Configuration
@EnableOAuth2Sso
public class AppContextConfig extends OAuth2SsoDefaultConfiguration {

	private final ApplicationContext applicationContext;
	/*@Autowired
	OAuth2ClientContext oauth2ClientContext;*/
	
	public AppContextConfig(ApplicationContext applicationContext, OAuth2SsoProperties sso) {
		super(applicationContext, sso);
		this.applicationContext = applicationContext;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		super.configure(http);
		http.antMatcher("/**")
			.authorizeRequests()
				.antMatchers("/article**").authenticated()
			.anyRequest().permitAll();
		http.apply(new OAuth2ClientAuthenticationConfigurer(oauth2SsoFilter(this.applicationContext)));
	}
	
	protected static OAuth2ClientAuthenticationProcessingFilter oauth2SsoFilter(ApplicationContext applicationContext) {
		OAuth2SsoProperties sso = applicationContext.getBean(OAuth2SsoProperties.class);
		OAuth2RestOperations restTemplate = applicationContext.getBean(UserInfoRestTemplateFactory.class).getUserInfoRestTemplate();
		ResourceServerTokenServices tokenServices = applicationContext.getBean(ResourceServerTokenServices.class);
		OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(sso.getLoginPath());
		filter.setRestTemplate(restTemplate);
		filter.setTokenServices(tokenServices);
		filter.setApplicationEventPublisher(applicationContext);
		return filter;
	}
	
	protected static class OAuth2ClientAuthenticationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	
		private OAuth2ClientAuthenticationProcessingFilter filter;
		
		OAuth2ClientAuthenticationConfigurer(OAuth2ClientAuthenticationProcessingFilter filter) {
			this.filter = filter;
		}
		
		@Override
		public void configure(HttpSecurity builder) throws Exception {
			OAuth2ClientAuthenticationProcessingFilter ssoFilter = this.filter;
			ssoFilter.setSessionAuthenticationStrategy(
					builder.getSharedObject(SessionAuthenticationStrategy.class));
			builder.addFilterAfter(ssoFilter,
					AbstractPreAuthenticatedProcessingFilter.class);
		}
	
	}

	
	/* 如果有多个验证中心，可自定义配置支持多个
	 * private Filter oauth2SsoFilter(ClientResources client, String path) {
		OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
				path);
		OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
		filter.setRestTemplate(template);
		filter.setTokenServices(new UserInfoTokenServices(
				client.getResource().getUserInfoUri(), client.getClient().getClientId()));
		return filter;
	}
	
	class ClientResources {

		@NestedConfigurationProperty
		private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

		@NestedConfigurationProperty
		private ResourceServerProperties resource = new ResourceServerProperties();

		public AuthorizationCodeResourceDetails getClient() {
			return client;
		}

		public ResourceServerProperties getResource() {
			return resource;
		}
	}*/

}
