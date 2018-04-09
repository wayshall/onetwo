package org.onetwo.boot.module.oauth2;

import java.util.Map;

import lombok.Data;

import org.onetwo.boot.module.oauth2.util.PasswordEncoders;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

/**
 * 统一在网关验证授权，因此后端的api项目除了需要获取auth2而配置tokenStore外，
 * 一般可把security简单配置为：
 * security: 
   		ignored: /**
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
	 * oauth2认证security的拦截器链，所以当auth server项目里有其它不需要验证的rest api，或者后台管理系统需要security的过滤器时，
	 * 不能简单地配置为： 
	 * security: 
			ignored: /**
			
	 要手动配置需要忽略的api路径，如：
	 security:
	 		ignored: /swagger**, /api/**
	 		
	   也可以把security的相关配置disabled：
	    management: 
		    security: 
		        enabled: false
		security: 
		    basic: 
		        enabled: false
	 */
	AuthorizationServerProps authorizationServer = new AuthorizationServerProps();
	ResourceServerProps resourceServer = new ResourceServerProps();
	JwtProps jwt = new JwtProps();
	String passwordEncoder = PasswordEncoders.NoOp.name();
	
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

		String[] requestMatchers;
		Map<String[], String> intercepterUrls = Maps.newHashMap();
		String anyRequest;
		
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
		public String[] getRequestMatchers() {
			return requestMatchers;
		}
		
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
