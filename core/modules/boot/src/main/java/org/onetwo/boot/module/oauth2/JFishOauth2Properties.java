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
	
	
	/****
	 * auth server
	 */
	AuthorizationServerProps authorizationServer = new AuthorizationServerProps();
	ResourceServerProps resourceServer = new ResourceServerProps();
	
	@Data
	public static class AuthorizationServerProps {
		boolean allowFormAuthenticationForClients;
		boolean sslOnly;
		String realm;
		String tokenKeyAccess;
		String checkTokenAccess;
		Map<String, MemoryUser> clientDetails = Maps.newHashMap();
	}
	
	@Data
	public static class ResourceServerProps {
		public static final String ENABLED_KEY = CONFIG_PREFIX + ".resourceServer.enabled";
		
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
}
