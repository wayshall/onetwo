package org.onetwo.boot.module.oauth2;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(TokenStore.class)
@ConditionalOnProperty(name=JFishOauth2Properties.TOKEN_STORE_ENABLED_KEY)
public class Oauth2TokenStoreConfiguration {

	@Configuration
	@ConditionalOnProperty(name=JFishOauth2Properties.TOKEN_STORE_ENABLED_KEY, havingValue=JFishOauth2Properties.KEYS_REDIS)
	protected static class  RedisTokenStoreConfiguration {
		@Bean
		public RedisTokenStore redisTokenStore(@Autowired RedisConnectionFactory redisConnectionFactory){
			RedisTokenStore store = new RedisTokenStore(redisConnectionFactory);
			return store;
		}
	}

	@Configuration
	@ConditionalOnProperty(name=JFishOauth2Properties.TOKEN_STORE_ENABLED_KEY, havingValue=JFishOauth2Properties.KEYS_JDBC)
	protected static class  JdbcTokenStoreConfiguration {
		@Bean
		public JdbcTokenStore jdbcTokenStore(@Autowired DataSource dataSource){
			JdbcTokenStore store = new JdbcTokenStore(dataSource);
			return store;
		}
	}

	/***
	 * security.oauth2.resource.jwt.
	 * @author wayshall
	 *
	 */
	/*@Configuration
	@ConditionalOnProperty(name=JFishOauth2Properties.TOKEN_STORE_ENABLED_KEY, havingValue=JFishOauth2Properties.KEYS_JWT)
	protected static class  JwtTokenStoreConfiguration {
		@Bean
		public JwtTokenStore redisTokenStore(@Autowired JwtAccessTokenConverter jwtTokenEnhancer){
			JwtTokenStore store = new JwtTokenStore(jwtTokenEnhancer);
			return store;
		}
	}*/
}
