package org.onetwo.boot.module.oauth2.authorize;

import static org.onetwo.ext.security.DefaultUrlSecurityConfigurer.configIntercepterUrls;
import static org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer.defaultAnyRequest;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties;
import org.onetwo.boot.module.oauth2.JFishOauth2Properties.AuthorizationServerProps;
import org.onetwo.boot.module.oauth2.JFishOauth2Properties.ClientDetailStore;
import org.onetwo.boot.module.oauth2.JFishOauth2Properties.MemoryUser;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder.ClientBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
@EnableAuthorizationServer
@Configuration
@EnableConfigurationProperties(JFishOauth2Properties.class)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private JFishOauth2Properties oauth2Properties;
	@Autowired(required=false)
	private DataSource dataSource;
	@Autowired(required=false)
	private PasswordEncoder passwordEncoder;
	@Autowired(required=false)
	private TokenStore tokenStore;
	//for jwt
	/*@Autowired(required=false)
	private TokenEnhancerChain tokenEnhancer;*/
	@Autowired(required=false)
	List<TokenEnhancer> tokenEnhancers;
	

	//for error

	@Autowired(required=false)
	private OAuth2ExceptionRenderer oauth2ExceptionRenderer;
	@Autowired(required=false)
	private OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint;
	@Autowired(required=false)
	private OAuth2AccessDeniedHandler oauth2AccessDeniedHandler;
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.and().requestMatchers()
		AuthorizationServerProps authProps = oauth2Properties.getAuthorizationServer();
		if(authProps.isAllowFormAuthenticationForClients()){
			security.allowFormAuthenticationForClients();
			//FIX: AuthorizationServerSecurityConfigurer创建form验证filter的时，没有使用配置的oauth2AuthenticationEntryPoint
			security.addObjectPostProcessor(new ClientCredentialsTokenEndpointFilterPostProcessor());
		}
		if(authProps.isSslOnly()){
			security.sslOnly();
		}
		if(StringUtils.isNotBlank(authProps.getRealm())){
			security.realm(authProps.getRealm());
		}
		if(StringUtils.isNotBlank(authProps.getCheckTokenAccess())){
			security.checkTokenAccess(authProps.getCheckTokenAccess());
		}
		if(StringUtils.isNotBlank(authProps.getTokenKeyAccess())){
			security.tokenKeyAccess(authProps.getTokenKeyAccess());
		}
		
		if(oauth2AuthenticationEntryPoint!=null){
			security.authenticationEntryPoint(oauth2AuthenticationEntryPoint);
		}
		if(oauth2AccessDeniedHandler!=null){
			security.accessDeniedHandler(oauth2AccessDeniedHandler);
		}
		
	}
	
	protected class ClientCredentialsTokenEndpointFilterPostProcessor implements ObjectPostProcessor<ClientCredentialsTokenEndpointFilter> {
		@Override
		public <O extends ClientCredentialsTokenEndpointFilter> O postProcess(O filter) {
			if(oauth2ExceptionRenderer!=null){
				SpringUtils.newPropertyAccessor(filter, true).setPropertyValue("authenticationEntryPoint.exceptionRenderer", oauth2ExceptionRenderer);
			}
			return filter;
		}
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		ClientDetailStore store = oauth2Properties.getAuthorizationServer().getClientDetailStore();
		if(store==ClientDetailStore.JDBC){
			configJdbc(clients);
		}else if(store==ClientDetailStore.IN_MEMORY){
			configInMemory(clients);
		}
	}

	protected void configJdbc(ClientDetailsServiceConfigurer clients) throws Exception{
		Assert.notNull(dataSource, "dataSource is required!");
		clients.jdbc(dataSource)
				.passwordEncoder(passwordEncoder)
				.build();
	}

	@SuppressWarnings("rawtypes")
	protected void configInMemory(ClientDetailsServiceConfigurer clients) throws Exception{
		Map<String, MemoryUser> clientUsers = oauth2Properties.getAuthorizationServer().getClientDetails();
		InMemoryClientDetailsServiceBuilder inMemory = clients.inMemory();
		clientUsers.forEach((user, config)->{
			ClientBuilder cb = inMemory.withClient(user).secret(config.getSecret());
			
			if(!LangUtils.isEmpty(config.getScopes())){
				cb.scopes(config.getScopes());
			}
			if(!LangUtils.isEmpty(config.getAuthorities())){
				cb.authorities(config.getAuthorities());
			}
			if(config.getAccessTokenValiditySeconds()!=null){
				cb.accessTokenValiditySeconds(config.getAccessTokenValiditySeconds());
			}
			cb.autoApprove(config.isAutoApprove());
			if(!LangUtils.isEmpty(config.getAutoApproveScopes())){
				cb.autoApprove(config.getAutoApproveScopes());
			}
			if(!LangUtils.isEmpty(config.getResourceIds())){
				cb.resourceIds(config.getResourceIds());
			}
			if(!LangUtils.isEmpty(config.getAuthorizedGrantTypes())){
				cb.authorizedGrantTypes(config.getAuthorizedGrantTypes());
			}
			if(config.getRefreshTokenValiditySeconds()!=null){
				cb.refreshTokenValiditySeconds(config.getRefreshTokenValiditySeconds());
			}
			if(!LangUtils.isEmpty(config.getRegisteredRedirectUris())){
				cb.redirectUris(config.getRegisteredRedirectUris());
			}
		});
		inMemory.build();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		if(tokenStore!=null){
			endpoints.tokenStore(tokenStore);
		}
		endpoints.tokenEnhancer(tokenEnhancerChain());
	}
	
	@Configuration
	protected class AuthorizationWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			AuthorizationServerProps authorizationServer = oauth2Properties.getAuthorizationServer();
			if(!LangUtils.isEmpty(authorizationServer.getRequestMatchers())){
				http.requestMatchers()
					.antMatchers(authorizationServer.getRequestMatchers());
			}
			configIntercepterUrls(http, authorizationServer.getIntercepterUrls(), null);
			defaultAnyRequest(http, authorizationServer.getAnyRequest());
		}
		
	}
	
	protected TokenEnhancerChain tokenEnhancerChain(){
		TokenEnhancerChain chain = new TokenEnhancerChain();
		if(tokenEnhancers!=null){
			chain.setTokenEnhancers(tokenEnhancers);
		}
		return chain;
	}
}
