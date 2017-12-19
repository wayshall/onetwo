package org.onetwo.boot.module.oauth2;

import java.util.Map;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
@Data
@ConfigurationProperties(prefix=JFishOauth2Properties.CONFIG_PREFIX)
public class JFishOauth2Properties {
	public static final String CONFIG_PREFIX = "jfish.oauth2";
	public static final String TOKEN_STORE_ENABLED_KEY = CONFIG_PREFIX + ".tokenStore";
	
	public static final String KEYS_REDIS = "redis";
	public static final String KEYS_JDBC = "jdbc";
	public static final String KEYS_JWT = "jwt";
	public static final String KEYS_IN_MEMORY = "in_memory";
	
	
	/****
	 * auth server
	 */
	AuthorizationServerProps authorizationServer = new AuthorizationServerProps();
	ResourceServerProps resourceServer = new ResourceServerProps();
	JwtProps jwt = new JwtProps();
	
	@Data
	public static class JwtProps {
		String signingKey;
	}
	
	@Data
	public static class AuthorizationServerProps {
		boolean allowFormAuthenticationForClients;
		boolean sslOnly;
		String realm;
		String tokenKeyAccess;
		String checkTokenAccess;
		
		ClientDetailStore clientDetailStore = ClientDetailStore.IN_MEMORY;
		
		Map<String, MemoryUser> clientDetails = Maps.newHashMap();
	}
	
	@Data
	public static class ResourceServerProps {
		public static final String ENABLED_KEY = CONFIG_PREFIX + ".resourceServer.enabled";
		
		String resourceId;
		/***
		 * resource server
		 */
		String[] requestMatchers;
		Map<String[], String> intercepterUrls = Maps.newHashMap();
		String anyRequest;
	}


	@Data
	public static class MemoryUser {
		String clientId;
		String secret;
		String[] scopes;
		String[] authorities;
		boolean autoApprove;
		String[] autoApproveScopes;
		
		Integer accessTokenValiditySeconds;
		Integer refreshTokenValiditySeconds;
		String[] registeredRedirectUris;
		String[] resourceIds;
		String[] authorizedGrantTypes;
	}
	
	public static enum ClientDetailStore {
		JDBC,
		IN_MEMORY
	}
	
	public static enum TokenStores {
		REDIS,
		JDBC,
		JWT,
		IN_MEMORY
	}
}
